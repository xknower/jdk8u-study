package jdk.jfr.internal;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import jdk.jfr.internal.SecuritySupport.SafePath;

public final class Repository {

    private static final int MAX_REPO_CREATION_RETRIES = 1000;
    private static final JVM jvm = JVM.getJVM();
    private static final Repository instance = new Repository();

    public final static DateTimeFormatter REPO_DATE_FORMAT = DateTimeFormatter
            .ofPattern("yyyy_MM_dd_HH_mm_ss");

    private final Set<SafePath> cleanupDirectories = new HashSet<>();
    private SafePath baseLocation;
    private SafePath repository;

    private Repository() {
    }

    public static Repository getRepository() {
        return instance;
    }

    public synchronized void setBasePath(SafePath baseLocation) throws Exception {
        // Probe to see if repository can be created, needed for fail fast
        // during JVM startup or JFR.configure
        this.repository = createRepository(baseLocation);
        try {
            // Remove so we don't "leak" repositories, if JFR is never started
            // and shutdown hook not added.
            SecuritySupport.delete(repository);
        } catch (IOException ioe) {
            Logger.log(LogTag.JFR, LogLevel.INFO, "Could not delete disk repository " + repository);
        }
        this.baseLocation = baseLocation;
    }

    synchronized void ensureRepository() throws Exception {
        if (baseLocation == null) {
            setBasePath(SecuritySupport.JAVA_IO_TMPDIR);
        }
    }

    synchronized RepositoryChunk newChunk(Instant timestamp) {
        try {
            if (!SecuritySupport.existDirectory(repository)) {
                this.repository = createRepository(baseLocation);
                jvm.setRepositoryLocation(repository.toString());
                cleanupDirectories.add(repository);
            }
            return new RepositoryChunk(repository, timestamp);
        } catch (Exception e) {
            String errorMsg = String.format("Could not create chunk in repository %s, %s", repository, e.getMessage());
            Logger.log(LogTag.JFR, LogLevel.ERROR, errorMsg);
            jvm.abort(errorMsg);
            throw new InternalError("Could not abort after JFR disk creation error");
        }
    }

    private static SafePath createRepository(SafePath basePath) throws Exception {
        SafePath canonicalBaseRepositoryPath = createRealBasePath(basePath);
        SafePath f = null;

        String basename = REPO_DATE_FORMAT.format(LocalDateTime.now()) + "_" + JVM.getJVM().getPid();
        String name = basename;

        int i = 0;
        for (; i < MAX_REPO_CREATION_RETRIES; i++) {
            f = new SafePath(canonicalBaseRepositoryPath.toPath().resolve(name));
            if (tryToUseAsRepository(f)) {
                break;
            }
            name = basename + "_" + i;
        }

        if (i == MAX_REPO_CREATION_RETRIES) {
            throw new Exception("Unable to create JFR repository directory using base location (" + basePath + ")");
        }
        SafePath canonicalRepositoryPath = SecuritySupport.toRealPath(f);
        return canonicalRepositoryPath;
    }

    private static SafePath createRealBasePath(SafePath safePath) throws Exception {
        if (SecuritySupport.exists(safePath)) {
            if (!SecuritySupport.isWritable(safePath)) {
                throw new IOException("JFR repository directory (" + safePath.toString() + ") exists, but isn't writable");
            }
            return SecuritySupport.toRealPath(safePath);
        }
        SafePath p = SecuritySupport.createDirectories(safePath);
        return SecuritySupport.toRealPath(p);
    }

    private static boolean tryToUseAsRepository(final SafePath path) {
        Path parent = path.toPath().getParent();
        if (parent == null) {
            return false;
        }
        try {
            try {
                SecuritySupport.createDirectories(path);
            } catch (Exception e) {
                // file already existed or some other problem occurred
            }
            if (!SecuritySupport.exists(path)) {
                return false;
            }
            if (!SecuritySupport.isDirectory(path)) {
                return false;
            }
            return true;
        } catch (IOException io) {
            return false;
        }
    }

    synchronized void clear() {
        for (SafePath p : cleanupDirectories) {
            try {
                SecuritySupport.clearDirectory(p);
                Logger.log(LogTag.JFR, LogLevel.INFO, "Removed repository " + p);
            } catch (IOException e) {
                Logger.log(LogTag.JFR, LogLevel.ERROR, "Repository " + p + " could not be removed at shutdown: " + e.getMessage());
            }
        }
    }

    public synchronized SafePath getRepositoryPath() {
        return repository;
    }
}
