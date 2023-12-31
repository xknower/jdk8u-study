package sun.tools.native2ascii;

import java.io.*;
import java.util.*;
import java.text.MessageFormat;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Main program of the native2ascii
 */

public class Main {

    String inputFileName = null;
    String outputFileName = null;
    File tempFile = null;
    boolean reverse = false;
    static String encodingString = null;
    static String defaultEncoding = null;
    static CharsetEncoder encoder = null;

    /**
     * Run the converter
     */
    public synchronized boolean convert(String argv[]) {
        List<String> v = new ArrayList<>(2);
        File outputFile = null;
        boolean createOutputFile = false;

        // Parse arguments
        for (int i = 0; i < argv.length; i++) {
            if (argv[i].equals("-encoding")) {
                if ((i + 1) < argv.length) {
                    encodingString = argv[++i];
                } else {
                    error(getMsg("err.bad.arg"));
                    usage();
                    return false;
                }
            } else if (argv[i].equals("-reverse")) {
                reverse = true;
            } else {
                if (v.size() > 1) {
                    usage();
                    return false;
                }
                v.add(argv[i]);
            }
        }

        if (encodingString == null) {
            defaultEncoding = Charset.defaultCharset().name();
        }
        char[] lineBreak = System.getProperty("line.separator").toCharArray();

        try {
            initializeConverter();

            if (v.size() == 1) {
                inputFileName = v.get(0);
            }

            if (v.size() == 2) {
                inputFileName = v.get(0);
                outputFileName = v.get(1);
                createOutputFile = true;
            }

            if (createOutputFile) {
                outputFile = new File(outputFileName);
                if (outputFile.exists() && !outputFile.canWrite()) {
                    throw new Exception(formatMsg("err.cannot.write", outputFileName));
                }
            }

            if (reverse) {
                try (BufferedReader reader = getA2NInput(inputFileName);
                        Writer osw = getA2NOutput(outputFileName);) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        osw.write(line.toCharArray());
                        osw.write(lineBreak);
                        if (outputFileName == null) { // flush stdout
                            osw.flush();
                        }
                    }
                }
            } else {
                // N2A
                try (BufferedReader in = getN2AInput(inputFileName);
                        BufferedWriter out = getN2AOutput(outputFileName);) {
                    String inLine;
                    while ((inLine = in.readLine()) != null) {
                        out.write(inLine.toCharArray());
                        out.write(lineBreak);
                        if (outputFileName == null) { // flush stdout
                            out.flush();
                        }
                    }
                }
            }

            // Since we are done rename temporary file to desired output file
            if (createOutputFile) {
                if (outputFile.exists()) {
                    // Some win32 platforms can't handle atomic
                    // rename if source and target file paths are
                    // identical. To make things simple we just unconditionally
                    // delete the target file before calling renameTo()
                    outputFile.delete();
                }
                tempFile.renameTo(outputFile);
            }
        } catch (Exception e) {
            error(e.toString());
            return false;
        }

        return true;
    }

    private void error(String msg){
        System.out.println(msg);
    }

    private void usage(){
        System.out.println(getMsg("usage"));
    }


    private BufferedReader getN2AInput(String inFile) throws Exception {

        InputStream forwardIn;
        if (inFile == null)
            forwardIn = System.in;
        else {
            File f = new File(inFile);
            if (!f.canRead()){
                throw new Exception(formatMsg("err.cannot.read", f.getName()));
            }

            try {
                 forwardIn = new FileInputStream(inFile);
            } catch (IOException e) {
               throw new Exception(formatMsg("err.cannot.read", f.getName()));
            }
        }

        BufferedReader r = (encodingString != null) ?
            new BufferedReader(new InputStreamReader(forwardIn,
                                                     encodingString)) :
            new BufferedReader(new InputStreamReader(forwardIn));
        return r;
    }


    private BufferedWriter getN2AOutput(String outFile) throws Exception {
        Writer output;
        BufferedWriter n2aOut;

        if (outFile == null)
            output = new OutputStreamWriter(System.out,"US-ASCII");

        else {
            File f = new File(outFile);

            File tempDir = f.getParentFile();

            if (tempDir == null)
                tempDir = new File(System.getProperty("user.dir"));

            tempFile = File.createTempFile("_N2A",
                                           ".TMP",
                                            tempDir);
            tempFile.deleteOnExit();

            try {
                output = new FileWriter(tempFile);
            } catch (IOException e){
                throw new Exception(formatMsg("err.cannot.write", tempFile.getName()));
            }
        }

        n2aOut = new BufferedWriter(new N2AFilter(output));
        return n2aOut;
    }

    private BufferedReader getA2NInput(String inFile) throws Exception {
        Reader in;
        BufferedReader reader;

        if (inFile == null)
            in = new InputStreamReader(System.in, "US-ASCII");
        else {
            File f = new File(inFile);
            if (!f.canRead()){
                throw new Exception(formatMsg("err.cannot.read", f.getName()));
            }

            try {
                 in = new FileReader(inFile);
            } catch (Exception e) {
               throw new Exception(formatMsg("err.cannot.read", f.getName()));
            }
        }

        reader = new BufferedReader(new A2NFilter(in));
        return reader;
    }

    private Writer getA2NOutput(String outFile) throws Exception {

        OutputStreamWriter w = null;
        OutputStream output = null;

        if (outFile == null)
            output = System.out;
        else {
            File f = new File(outFile);

            File tempDir = f.getParentFile();
            if (tempDir == null)
                tempDir = new File(System.getProperty("user.dir"));
            tempFile =  File.createTempFile("_N2A",
                                            ".TMP",
                                            tempDir);
            tempFile.deleteOnExit();

            try {
                output = new FileOutputStream(tempFile);
            } catch (IOException e){
                throw new Exception(formatMsg("err.cannot.write", tempFile.getName()));
            }
        }

        w = (encodingString != null) ?
            new OutputStreamWriter(output, encodingString) :
            new OutputStreamWriter(output);

        return (w);
    }

    private static Charset lookupCharset(String csName) {
        if (Charset.isSupported(csName)) {
           try {
                return Charset.forName(csName);
           } catch (UnsupportedCharsetException x) {
                throw new Error(x);
           }
        }
        return null;
    }

    public static boolean canConvert(char ch) {
        return (encoder != null && encoder.canEncode(ch));
    }

    private static void initializeConverter() throws UnsupportedEncodingException {
        Charset cs = null;

        try {
            cs = (encodingString == null) ?
                lookupCharset(defaultEncoding):
                lookupCharset(encodingString);

            encoder =  (cs != null) ?
                cs.newEncoder() :
                null;
        } catch (IllegalCharsetNameException e) {
            throw new Error(e);
        }
    }

    private static ResourceBundle rsrc;

    static {
        try {
            rsrc = ResourceBundle.getBundle(
                     "sun.tools.native2ascii.resources.MsgNative2ascii");
        } catch (MissingResourceException e) {
            throw new Error("Missing message file.");
        }
    }

    private String getMsg(String key) {
        try {
            return (rsrc.getString(key));
        } catch (MissingResourceException e) {
            throw new Error("Error in  message file format.");
        }
    }

    private String formatMsg(String key, String arg) {
        String msg = getMsg(key);
        return MessageFormat.format(msg, arg);
    }


    /**
     * Main program
     */
    public static void main(String argv[]){
        Main converter = new Main();
        System.exit(converter.convert(argv) ? 0 : 1);
    }
}