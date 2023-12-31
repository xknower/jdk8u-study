package jdk.jfr.internal.dcmd;



import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.Options;
import jdk.jfr.internal.Repository;
import jdk.jfr.internal.SecuritySupport.SafePath;

/**
 * JFR.configure - invoked from native
 *
 */
//Instantiated by native
final class DCmdConfigure extends AbstractDCmd {
    /**
     * Execute JFR.configure.
     *
     * @param repositoryPath the path
     * @param dumpPath path to dump to on fatal error (oom)
     * @param stackDepth depth of stack traces
     * @param globalBufferCount number of global buffers
     * @param globalBufferSize size of global buffers
     * @param threadBufferSize size of thread buffer for events
     * @param maxChunkSize threshold at which a new chunk is created in the disk repository
     * @param sampleThreads if thread sampling should be enabled
     *
     * @return result

     * @throws DCmdException
     *             if the dump could not be completed
     */
    public String execute
    (
            String repositoryPath,
            String dumpPath,
            Integer stackDepth,
            Long globalBufferCount,
            Long globalBufferSize,
            Long threadBufferSize,
            Long memorySize,
            Long maxChunkSize,
            Boolean sampleThreads

    ) throws DCmdException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdConfigure: repositorypath=" + repositoryPath +
                    ", dumppath=" + dumpPath +
                    ", stackdepth=" + stackDepth +
                    ", globalbuffercount=" + globalBufferCount +
                    ", globalbuffersize=" + globalBufferSize +
                    ", thread_buffer_size" + threadBufferSize +
                    ", memorysize" + memorySize +
                    ", maxchunksize=" + maxChunkSize +
                    ", samplethreads" + sampleThreads);
        }


        boolean updated = false;
        if (repositoryPath != null) {
            try {
                SafePath s = new SafePath(repositoryPath);
                Repository.getRepository().setBasePath(s);
                Logger.log(LogTag.JFR, LogLevel.INFO, "Base repository path set to " + repositoryPath);
            } catch (Exception e) {
                throw new DCmdException("Could not use " + repositoryPath + " as repository. " + e.getMessage(), e);
            }
            printRepositoryPath();
            updated = true;
        }

        if (dumpPath != null)  {
            Options.setDumpPath(new SafePath(dumpPath));
            Logger.log(LogTag.JFR, LogLevel.INFO, "Emergency dump path set to " + dumpPath);
            printDumpPath();
            updated = true;
        }

        if (stackDepth != null)  {
            Options.setStackDepth(stackDepth);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Stack depth set to " + stackDepth);
            printStackDepth();
            updated = true;
        }

        if (globalBufferCount != null)  {
            Options.setGlobalBufferCount(globalBufferCount);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Global buffer count set to " + globalBufferCount);
            printGlobalBufferCount();
            updated = true;
        }

        if (globalBufferSize != null)  {
            Options.setGlobalBufferSize(globalBufferSize);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Global buffer size set to " + globalBufferSize);
            printGlobalBufferSize();
            updated = true;
        }

        if (threadBufferSize != null)  {
            Options.setThreadBufferSize(threadBufferSize);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Thread buffer size set to " + threadBufferSize);
            printThreadBufferSize();
            updated = true;
        }

        if (memorySize != null) {
            Options.setMemorySize(memorySize);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Memory size set to " + memorySize);
            printMemorySize();
            updated = true;
        }

        if (maxChunkSize != null)  {
            Options.setMaxChunkSize(maxChunkSize);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Max chunk size set to " + maxChunkSize);
            printMaxChunkSize();
            updated = true;
        }

        if (sampleThreads != null)  {
            Options.setSampleThreads(sampleThreads);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Sample threads set to " + sampleThreads);
            printSampleThreads();
            updated = true;
        }

        if (!updated) {
            println("Current configuration:");
            println();
            printRepositoryPath();
            printStackDepth();
            printGlobalBufferCount();
            printGlobalBufferSize();
            printThreadBufferSize();
            printMemorySize();
            printMaxChunkSize();
            printSampleThreads();
        }
        return getResult();
    }

    private void printRepositoryPath() {
        print("Repository path: ");
        printPath(Repository.getRepository().getRepositoryPath());
        println();
    }

    private void printDumpPath() {
        print("Dump path: ");
        printPath(Options.getDumpPath());
        println();
    }

    private void printSampleThreads() {
        println("Sample threads: " + Options.getSampleThreads());
    }

    private void printStackDepth() {
        println("Stack depth: " +  Options.getStackDepth());
    }

    private void printGlobalBufferCount() {
        println("Global buffer count: " +  Options.getGlobalBufferCount());
    }

    private void printGlobalBufferSize() {
        print("Global buffer size: ");
        printBytes(Options.getGlobalBufferSize());
        println();
    }

    private void printThreadBufferSize() {
        print("Thread buffer size: ");
        printBytes(Options.getThreadBufferSize());
        println();
    }

    private void printMemorySize() {
        print("Memory size: ");
        printBytes(Options.getMemorySize());
        println();
    }

    private void printMaxChunkSize() {
        print("Max chunk size: ");
        printBytes(Options.getMaxChunkSize());
        println();
    }
}
