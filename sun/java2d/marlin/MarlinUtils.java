package sun.java2d.marlin;


public final class MarlinUtils {
    // Marlin logger
    private static final sun.util.logging.PlatformLogger log;

    static {
        if (MarlinConst.useLogger) {
            log = sun.util.logging.PlatformLogger.getLogger("sun.java2d.marlin");
        } else {
            log = null;
        }
    }

    private MarlinUtils() {
        // no-op
    }

    public static void logInfo(final String msg) {
        if (MarlinConst.useLogger) {
            log.info(msg);
        } else if (MarlinConst.enableLogs) {
            System.out.print("INFO: ");
            System.out.println(msg);
        }
    }

    public static void logException(final String msg, final Throwable th) {
        if (MarlinConst.useLogger) {
            log.warning(msg, th);
        } else if (MarlinConst.enableLogs) {
            System.out.print("WARNING: ");
            System.out.println(msg);
            th.printStackTrace(System.err);
        }
    }
}
