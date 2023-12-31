package jdk.jfr.internal.dcmd;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import jdk.jfr.EventType;
import jdk.jfr.Recording;
import jdk.jfr.SettingDescriptor;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.Utils;

/**
 * JFR.check - invoked from native
 *
 */
final class DCmdCheck extends AbstractDCmd {
    /**
     * Execute JFR.check
     *
     * @param recordingText name or id of the recording to check, or
     *        <code>null</code> to show a list of all recordings.
     *
     * @param verbose if event settings should be included.
     *
     * @return result output
     *
     * @throws DCmdException if the check could not be completed.
     */
    public String execute(String recordingText, Boolean verbose) throws DCmdException {
        executeInternal(recordingText, verbose);
        return getResult();
    }

    private void executeInternal(String name, Boolean verbose) throws DCmdException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdCheck: name=" + name + ", verbose=" + verbose);
        }

        if (verbose == null) {
            verbose = Boolean.FALSE;
        }

        if (name != null) {
            printRecording(findRecording(name), verbose);
            return;
        }

        List<Recording> recordings = getRecordings();
        if (!verbose && recordings.isEmpty()) {
            println("No available recordings.");
            println();
            println("Use jcmd " + getPid() + " JFR.start to start a recording.");
            return;
        }
        boolean first = true;
        for (Recording recording : recordings) {
            // Print separation between recordings,
            if (!first) {
                println();
                if (Boolean.TRUE.equals(verbose)) {
                    println();
                }
            }
            first = false;
            printRecording(recording, verbose);
        }
    }

    private void printRecording(Recording recording, boolean verbose) {
        printGeneral(recording);
        if (verbose) {
            println();
            printSetttings(recording);
        }
    }

    private void printGeneral(Recording recording) {
        print("Recording " + recording.getId() + ": name=" + recording.getName());

        Duration duration = recording.getDuration();
        if (duration != null) {
            print(" duration=");
            printTimespan(duration, "");
        }

        long maxSize = recording.getMaxSize();
        if (maxSize != 0) {
            print(" maxsize=");
            print(Utils.formatBytesCompact(maxSize));
        }
        Duration maxAge = recording.getMaxAge();
        if (maxAge != null) {
            print(" maxage=");
            printTimespan(maxAge, "");
        }

        print(" (" + recording.getState().toString().toLowerCase() + ")");
        println();
    }

    private void printSetttings(Recording recording) {
        Map<String, String> settings = recording.getSettings();
        for (EventType eventType : sortByEventPath(getFlightRecorder().getEventTypes())) {
            StringJoiner sj = new StringJoiner(",", "[", "]");
            sj.setEmptyValue("");
            for (SettingDescriptor s : eventType.getSettingDescriptors()) {
                String settingsPath = eventType.getName() + "#" + s.getName();
                if (settings.containsKey(settingsPath)) {
                    sj.add(s.getName() + "=" + settings.get(settingsPath));
                }
            }
            String settingsText = sj.toString();
            if (!settingsText.isEmpty()) {
                print(" %s (%s)", eventType.getLabel(), eventType.getName());
                println();
                println("   " + settingsText);
            }
        }
    }

    private static List<EventType> sortByEventPath(Collection<EventType> events) {
        List<EventType> sorted = new ArrayList<>();
        sorted.addAll(events);
        Collections.sort(sorted, new Comparator<EventType>() {
            @Override
            public int compare(EventType e1, EventType e2) {
                return e1.getName().compareTo(e2.getName());
            }
        });
        return sorted;
    }
}
