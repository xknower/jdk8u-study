package sun.net.www;
import java.io.*;
import java.net.FileNameMap;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

public class MimeTable implements FileNameMap {
    /** Keyed by content type, returns MimeEntries */
    private Hashtable<String, MimeEntry> entries
        = new Hashtable<String, MimeEntry>();

    /** Keyed by file extension (with the .), returns MimeEntries */
    private Hashtable<String, MimeEntry> extensionMap
        = new Hashtable<String, MimeEntry>();

    // Will be reset if in the platform-specific data file
    private static String tempFileTemplate;

    static {
        java.security.AccessController.doPrivileged(
            new java.security.PrivilegedAction<Void>() {
                public Void run() {
                tempFileTemplate =
                    System.getProperty("content.types.temp.file.template",
                                       "/tmp/%s");

                mailcapLocations = new String[] {
                    System.getProperty("user.mailcap"),
                    System.getProperty("user.home") + "/.mailcap",
                    "/etc/mailcap",
                    "/usr/etc/mailcap",
                    "/usr/local/etc/mailcap",
                    System.getProperty("hotjava.home",
                                           "/usr/local/hotjava")
                        + "/lib/mailcap",
                };
                return null;
            }
        });
    }


    private static final String filePreamble = "sun.net.www MIME content-types table";
    private static final String fileMagic = "#" + filePreamble;

    MimeTable() {
        load();
    }

    private static class DefaultInstanceHolder {
        static final MimeTable defaultInstance = getDefaultInstance();

        static MimeTable getDefaultInstance() {
            return java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction<MimeTable>() {
                public MimeTable run() {
                    MimeTable instance = new MimeTable();
                    URLConnection.setFileNameMap(instance);
                    return instance;
                }
            });
        }
    }

    /**
     * Get the single instance of this class.  First use will load the
     * table from a data file.
     */
    public static MimeTable getDefaultTable() {
        return DefaultInstanceHolder.defaultInstance;
    }

    /**
     *
     */
    public static FileNameMap loadTable() {
        MimeTable mt = getDefaultTable();
        return (FileNameMap)mt;
    }

    public synchronized int getSize() {
        return entries.size();
    }

    public synchronized String getContentTypeFor(String fileName) {
        MimeEntry entry = findByFileName(fileName);
        if (entry != null) {
            return entry.getType();
        } else {
            return null;
        }
    }

    public synchronized void add(MimeEntry m) {
        entries.put(m.getType(), m);

        String exts[] = m.getExtensions();
        if (exts == null) {
            return;
        }

        for (int i = 0; i < exts.length; i++) {
            extensionMap.put(exts[i], m);
        }
    }

    public synchronized MimeEntry remove(String type) {
        MimeEntry entry = entries.get(type);
        return remove(entry);
    }

    public synchronized MimeEntry remove(MimeEntry entry) {
        String[] extensionKeys = entry.getExtensions();
        if (extensionKeys != null) {
            for (int i = 0; i < extensionKeys.length; i++) {
                extensionMap.remove(extensionKeys[i]);
            }
        }

        return entries.remove(entry.getType());
    }

    public synchronized MimeEntry find(String type) {
        MimeEntry entry = entries.get(type);
        if (entry == null) {
            // try a wildcard lookup
            Enumeration<MimeEntry> e = entries.elements();
            while (e.hasMoreElements()) {
                MimeEntry wild = e.nextElement();
                if (wild.matches(type)) {
                    return wild;
                }
            }
        }

        return entry;
    }

    /**
     * Locate a MimeEntry by the file extension that has been associated
     * with it. Parses general file names, and URLs.
     */
    public MimeEntry findByFileName(String fname) {
        String ext = "";

        int i = fname.lastIndexOf('#');

        if (i > 0) {
            fname = fname.substring(0, i - 1);
        }

        i = fname.lastIndexOf('.');
        // REMIND: OS specific delimters appear here
        i = Math.max(i, fname.lastIndexOf('/'));
        i = Math.max(i, fname.lastIndexOf('?'));

        if (i != -1 && fname.charAt(i) == '.') {
            ext = fname.substring(i).toLowerCase();
        }

        return findByExt(ext);
    }

    /**
     * Locate a MimeEntry by the file extension that has been associated
     * with it.
     */
    public synchronized MimeEntry findByExt(String fileExtension) {
        return extensionMap.get(fileExtension);
    }

    public synchronized MimeEntry findByDescription(String description) {
        Enumeration<MimeEntry> e = elements();
        while (e.hasMoreElements()) {
            MimeEntry entry = e.nextElement();
            if (description.equals(entry.getDescription())) {
                return entry;
            }
        }

        // We failed, now try treating description as type
        return find(description);
    }

    String getTempFileTemplate() {
        return tempFileTemplate;
    }

    public synchronized Enumeration<MimeEntry> elements() {
        return entries.elements();
    }

    // For backward compatibility -- mailcap format files
    // This is not currently used, but may in the future when we add ability
    // to read BOTH the properties format and the mailcap format.
    protected static String[] mailcapLocations;

    public synchronized void load() {
        Properties entries = new Properties();
        File file = null;
        try {
            InputStream is;
            // First try to load the user-specific table, if it exists
            String userTablePath =
                System.getProperty("content.types.user.table");
            if (userTablePath != null) {
                file = new File(userTablePath);
                if (!file.exists()) {
                    // No user-table, try to load the default built-in table.
                    file = new File(System.getProperty("java.home") +
                                    File.separator +
                                    "lib" +
                                    File.separator +
                                    "content-types.properties");
                }
            }
            else {
                // No user table, try to load the default built-in table.
                file = new File(System.getProperty("java.home") +
                                File.separator +
                                "lib" +
                                File.separator +
                                "content-types.properties");
            }

            is = new BufferedInputStream(new FileInputStream(file));
            entries.load(is);
            is.close();
        }
        catch (IOException e) {
            System.err.println("Warning: default mime table not found: " +
                               file.getPath());
            return;
        }
        parse(entries);
    }

    void parse(Properties entries) {
        // first, strip out the platform-specific temp file template
        String tempFileTemplate = (String)entries.get("temp.file.template");
        if (tempFileTemplate != null) {
            entries.remove("temp.file.template");
            MimeTable.tempFileTemplate = tempFileTemplate;
        }

        // now, parse the mime-type spec's
        Enumeration<?> types = entries.propertyNames();
        while (types.hasMoreElements()) {
            String type = (String)types.nextElement();
            String attrs = entries.getProperty(type);
            parse(type, attrs);
        }
    }

    //
    // Table format:
    //
    // <entry> ::= <table_tag> | <type_entry>
    //
    // <table_tag> ::= <table_format_version> | <temp_file_template>
    //
    // <type_entry> ::= <type_subtype_pair> '=' <type_attrs_list>
    //
    // <type_subtype_pair> ::= <type> '/' <subtype>
    //
    // <type_attrs_list> ::= <attr_value_pair> [ ';' <attr_value_pair> ]*
    //                       | [ <attr_value_pair> ]+
    //
    // <attr_value_pair> ::= <attr_name> '=' <attr_value>
    //
    // <attr_name> ::= 'description' | 'action' | 'application'
    //                 | 'file_extensions' | 'icon'
    //
    // <attr_value> ::= <legal_char>*
    //
    // Embedded ';' in an <attr_value> are quoted with leading '\' .
    //
    // Interpretation of <attr_value> depends on the <attr_name> it is
    // associated with.
    //

    void parse(String type, String attrs) {
        MimeEntry newEntry = new MimeEntry(type);

        // REMIND handle embedded ';' and '|' and literal '"'
        StringTokenizer tokenizer = new StringTokenizer(attrs, ";");
        while (tokenizer.hasMoreTokens()) {
            String pair = tokenizer.nextToken();
            parse(pair, newEntry);
        }

        add(newEntry);
    }

    void parse(String pair, MimeEntry entry) {
        // REMIND add exception handling...
        String name = null;
        String value = null;

        boolean gotName = false;
        StringTokenizer tokenizer = new StringTokenizer(pair, "=");
        while (tokenizer.hasMoreTokens()) {
            if (gotName) {
                value = tokenizer.nextToken().trim();
            }
            else {
                name = tokenizer.nextToken().trim();
                gotName = true;
            }
        }

        fill(entry, name, value);
    }

    void fill(MimeEntry entry, String name, String value) {
        if ("description".equalsIgnoreCase(name)) {
            entry.setDescription(value);
        }
        else if ("action".equalsIgnoreCase(name)) {
            entry.setAction(getActionCode(value));
        }
        else if ("application".equalsIgnoreCase(name)) {
            entry.setCommand(value);
        }
        else if ("icon".equalsIgnoreCase(name)) {
            entry.setImageFileName(value);
        }
        else if ("file_extensions".equalsIgnoreCase(name)) {
            entry.setExtensions(value);
        }

        // else illegal name exception
    }

    String[] getExtensions(String list) {
        StringTokenizer tokenizer = new StringTokenizer(list, ",");
        int n = tokenizer.countTokens();
        String[] extensions = new String[n];
        for (int i = 0; i < n; i++) {
            extensions[i] = tokenizer.nextToken();
        }

        return extensions;
    }

    int getActionCode(String action) {
        for (int i = 0; i < MimeEntry.actionKeywords.length; i++) {
            if (action.equalsIgnoreCase(MimeEntry.actionKeywords[i])) {
                return i;
            }
        }

        return MimeEntry.UNKNOWN;
    }

    public synchronized boolean save(String filename) {
        if (filename == null) {
            filename = System.getProperty("user.home" +
                                          File.separator +
                                          "lib" +
                                          File.separator +
                                          "content-types.properties");
        }

        return saveAsProperties(new File(filename));
    }

    public Properties getAsProperties() {
        Properties properties = new Properties();
        Enumeration<MimeEntry> e = elements();
        while (e.hasMoreElements()) {
            MimeEntry entry = e.nextElement();
            properties.put(entry.getType(), entry.toProperty());
        }

        return properties;
    }

    protected boolean saveAsProperties(File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            Properties properties = getAsProperties();
            properties.put("temp.file.template", tempFileTemplate);
            String tag;
            String user = System.getProperty("user.name");
            if (user != null) {
                tag = "; customized for " + user;
                properties.store(os, filePreamble + tag);
            }
            else {
                properties.store(os, filePreamble);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (os != null) {
                try { os.close(); } catch (IOException e) {}
            }
        }

        return true;
    }
    /*
     * Debugging utilities
     *
    public void list(PrintStream out) {
        Enumeration keys = entries.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            MimeEntry entry = (MimeEntry)entries.get(key);
            out.println(key + ": " + entry);
        }
    }

    public static void main(String[] args) {
        MimeTable testTable = MimeTable.getDefaultTable();

        Enumeration e = testTable.elements();
        while (e.hasMoreElements()) {
            MimeEntry entry = (MimeEntry)e.nextElement();
            System.out.println(entry);
        }

        testTable.save(File.separator + "tmp" +
                       File.separator + "mime_table.save");
    }
    */
}
