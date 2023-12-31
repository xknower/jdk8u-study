package jdk.jfr.internal.dcmd;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import jdk.jfr.Recording;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.SecuritySupport.SafePath;

/**
 * JFR.stop
 *
 */
// Instantiated by native
final class DCmdStop extends AbstractDCmd {

    /**
     * Execute JFR.stop
     *
     * Requires that either <code>name or <code>id</code> is set.
     *
     * @param name name or id of the recording to stop.
     *
     * @param filename file path where data should be written after recording has
     *        been stopped, or <code>null</code> if recording shouldn't be written
     *        to disk.
     * @return result text
     *
     * @throws DCmdException if recording could not be stopped
     */
    public String execute(String name, String filename) throws DCmdException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdStart: name=" + name + ", filename=" + filename);
        }

        try {
            SafePath safePath = null;
            Recording recording = findRecording(name);
            if (filename != null) {
                try {
                    // Ensure path is valid. Don't generate safePath if filename == null, as a user may
                    // want to stop recording without a dump
                    safePath = resolvePath(null, filename);
                    recording.setDestination(Paths.get(filename));
                } catch (IOException | InvalidPathException  e) {
                    throw new DCmdException("Failed to stop %s. Could not set destination for \"%s\" to file %s", recording.getName(), filename, e.getMessage());
                }
            }
            recording.stop();
            reportOperationComplete("Stopped", recording.getName(), safePath);
            recording.close();
            return getResult();
        } catch (InvalidPathException | DCmdException e) {
            if (filename != null) {
                throw new DCmdException("Could not write recording \"%s\" to file. %s", name, e.getMessage());
            }
            throw new DCmdException(e, "Could not stop recording \"%s\".", name, e.getMessage());
        }
    }
}
