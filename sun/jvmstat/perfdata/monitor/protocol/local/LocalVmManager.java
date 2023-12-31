package sun.jvmstat.perfdata.monitor.protocol.local;

import sun.jvmstat.monitor.*;
import sun.jvmstat.monitor.event.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;

/**
 * Class for managing the LocalMonitoredVm instances on the local system.
 * <p>
 * This class is responsible for the mechanism that detects the active
 * HotSpot Java Virtual Machines on the local host and possibly for a
 * specific user. The ability to detect all possible HotSpot Java Virtual
 * Machines on the local host may be limited by the permissions of the
 * principal running this JVM.
 *
 * @author Brian Doherty
 * @since 1.5
 */
public class LocalVmManager {
    private String userName;                 // user name for monitored jvm
    private File tmpdir;
    private Pattern userPattern;
    private Matcher userMatcher;
    private FilenameFilter userFilter;
    private Pattern filePattern;
    private Matcher fileMatcher;
    private FilenameFilter fileFilter;
    private Pattern tmpFilePattern;
    private Matcher tmpFileMatcher;
    private FilenameFilter tmpFileFilter;

    /**
     * Creates a LocalVmManager instance for the local system.
     * <p>
     * Manages LocalMonitoredVm instances for which the principal
     * has appropriate permissions.
     */
    public LocalVmManager() {
        this(null);
    }

    /**
     * Creates a LocalVmManager instance for the given user.
     * <p>
     * Manages LocalMonitoredVm instances for all JVMs owned by the specified
     * user.
     *
     * @param user the name of the user
     */
    public LocalVmManager(String user) {
        this.userName = user;

        if (userName == null) {
            tmpdir = new File(PerfDataFile.getTempDirectory());
            userPattern = Pattern.compile(PerfDataFile.userDirNamePattern);
            userMatcher = userPattern.matcher("");

            userFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    userMatcher.reset(name);
                    return userMatcher.lookingAt();
                }
            };
        } else {
            tmpdir = new File(PerfDataFile.getTempDirectory(userName));
        }

        filePattern = Pattern.compile(PerfDataFile.fileNamePattern);
        fileMatcher = filePattern.matcher("");

        fileFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                fileMatcher.reset(name);
                return fileMatcher.matches();
            }
        };

        tmpFilePattern = Pattern.compile(PerfDataFile.tmpFileNamePattern);
        tmpFileMatcher = tmpFilePattern.matcher("");

        tmpFileFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                tmpFileMatcher.reset(name);
                return tmpFileMatcher.matches();
            }
        };
    }

    /**
     * Return the current set of monitorable Java Virtual Machines.
     * <p>
     * The set returned by this method depends on the user name passed
     * to the constructor. If no user name was specified, then this
     * method will return all candidate JVMs on the system. Otherwise,
     * only the JVMs for the given user will be returned. This assumes
     * that principal associated with this JVM has the appropriate
     * permissions to access the target set of JVMs.
     *
     * @return Set - the Set of monitorable Java Virtual Machines
     */
    public synchronized Set<Integer> activeVms() {
        /*
         * This method is synchronized because the Matcher object used by
         * fileFilter is not safe for concurrent use, and this method is
         * called by multiple threads. Before this method was synchronized,
         * we'd see strange file names being matched by the matcher.
         */
        Set<Integer> jvmSet = new HashSet<Integer>();

        if (! tmpdir.isDirectory()) {
            return jvmSet;
        }

        if (userName == null) {
            /*
             * get a list of all of the user temporary directories and
             * iterate over the list to find any files within those directories.
             */
            File[] dirs = tmpdir.listFiles(userFilter);

            for (int i = 0 ; i < dirs.length; i ++) {
                if (!dirs[i].isDirectory()) {
                    continue;
                }

                // get a list of files from the directory
                File[] files = dirs[i].listFiles(fileFilter);

                if (files != null) {
                    for (int j = 0; j < files.length; j++) {
                        if (files[j].isFile() && files[j].canRead()) {
                            jvmSet.add(new Integer(
                                    PerfDataFile.getLocalVmId(files[j])));
                        }
                    }
                }
            }
        } else {
            /*
             * Check if the user directory can be accessed. Any of these
             * conditions may have asynchronously changed between subsequent
             * calls to this method.
             */

            // get the list of files from the specified user directory
            File[] files = tmpdir.listFiles(fileFilter);

            if (files != null) {
                for (int j = 0; j < files.length; j++) {
                    if (files[j].isFile() && files[j].canRead()) {
                        jvmSet.add(new Integer(
                                PerfDataFile.getLocalVmId(files[j])));
                    }
                }
            }
        }

        // look for any 1.4.1 files
        File[] files = tmpdir.listFiles(tmpFileFilter);
        if (files != null) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].isFile() && files[j].canRead()) {
                    jvmSet.add(new Integer(
                            PerfDataFile.getLocalVmId(files[j])));
                }
            }
        }

        return jvmSet;
    }
}
