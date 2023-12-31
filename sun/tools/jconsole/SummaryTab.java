package sun.tools.jconsole;

import java.awt.*;
import java.io.*;
import java.lang.management.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.*;


import static sun.tools.jconsole.Formatter.*;
import static sun.tools.jconsole.Utilities.*;

@SuppressWarnings("serial")
class SummaryTab extends Tab {
    private static final String cpuUsageKey = "cpu";

    private static final String newDivider =   "<tr><td colspan=4><font size =-1><hr>";
    private static final String newTable =     "<tr><td colspan=4 align=left><table cellpadding=1>";
    private static final String newLeftTable = "<tr><td colspan=2 align=left><table cellpadding=1>";
    private static final String newRightTable =  "<td colspan=2 align=left><table cellpadding=1>";
    private static final String endTable = "</table>";

    private static final int CPU_DECIMALS = 1;

    private CPUOverviewPanel overviewPanel;
    private DateFormat headerDateTimeFormat;
    private String pathSeparator = null;
    HTMLPane info;

    private static class Result {
        long upTime = -1L;
        long processCpuTime = -1L;
        long timeStamp;
        int nCPUs;
        String summary;
    }

    public static String getTabName() {
        return Messages.SUMMARY_TAB_TAB_NAME;
    }

    public SummaryTab(VMPanel vmPanel) {
        super(vmPanel, getTabName());

        setLayout(new BorderLayout());

        info = new HTMLPane();
        setAccessibleName(info, getTabName());
        add(new JScrollPane(info));

        headerDateTimeFormat =
            Formatter.getDateTimeFormat(Messages.SUMMARY_TAB_HEADER_DATE_TIME_FORMAT);
    }

    public SwingWorker<?, ?> newSwingWorker() {
        return new SwingWorker<Result, Object>() {
            public Result doInBackground() {
                return formatSummary();
            }


            protected void done() {
                try {
                    Result result = get();
                    if (result != null) {
                        info.setText(result.summary);
                        if (overviewPanel != null &&
                            result.upTime > 0L &&
                            result.processCpuTime >= 0L) {

                            overviewPanel.updateCPUInfo(result);
                        }
                    }
                } catch (InterruptedException ex) {
                } catch (ExecutionException ex) {
                    if (JConsole.isDebug()) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    StringBuilder buf;

    synchronized Result formatSummary() {
        Result result = new Result();
        ProxyClient proxyClient = vmPanel.getProxyClient();
        if (proxyClient.isDead()) {
            return null;
        }

        buf = new StringBuilder();
        append("<table cellpadding=1>");

        try {
            RuntimeMXBean         rmBean     = proxyClient.getRuntimeMXBean();
            CompilationMXBean     cmpMBean   = proxyClient.getCompilationMXBean();
            ThreadMXBean          tmBean     = proxyClient.getThreadMXBean();
            MemoryMXBean          memoryBean = proxyClient.getMemoryMXBean();
            ClassLoadingMXBean    clMBean    = proxyClient.getClassLoadingMXBean();
            OperatingSystemMXBean osMBean    = proxyClient.getOperatingSystemMXBean();
            com.sun.management.OperatingSystemMXBean sunOSMBean  =
               proxyClient.getSunOperatingSystemMXBean();

            append("<tr><td colspan=4>");
            append("<center><b>" + Messages.SUMMARY_TAB_TAB_NAME + "</b></center>");
            String dateTime =
                headerDateTimeFormat.format(System.currentTimeMillis());
            append("<center>" + dateTime + "</center>");

            append(newDivider);

            {  // VM info
                append(newLeftTable);
                append(Messages.CONNECTION_NAME, vmPanel.getDisplayName());
                append(Messages.VIRTUAL_MACHINE,
                       Resources.format(Messages.SUMMARY_TAB_VM_VERSION,
                                        rmBean.getVmName(), rmBean.getVmVersion()));
                append(Messages.VENDOR, rmBean.getVmVendor());
                append(Messages.NAME, rmBean.getName());
                append(endTable);

                append(newRightTable);
                result.upTime = rmBean.getUptime();
                append(Messages.UPTIME, formatTime(result.upTime));
                if (sunOSMBean != null) {
                    result.processCpuTime = sunOSMBean.getProcessCpuTime();
                    append(Messages.PROCESS_CPU_TIME, formatNanoTime(result.processCpuTime));
                }

                if (cmpMBean != null) {
                    append(Messages.JIT_COMPILER, cmpMBean.getName());
                    append(Messages.TOTAL_COMPILE_TIME,
                           cmpMBean.isCompilationTimeMonitoringSupported()
                                    ? formatTime(cmpMBean.getTotalCompilationTime())
                                    : Messages.UNAVAILABLE);
                } else {
                    append(Messages.JIT_COMPILER, Messages.UNAVAILABLE);
                }
                append(endTable);
            }

            append(newDivider);

            {  // Threads and Classes
                append(newLeftTable);
                int tlCount = tmBean.getThreadCount();
                int tdCount = tmBean.getDaemonThreadCount();
                int tpCount = tmBean.getPeakThreadCount();
                long ttCount = tmBean.getTotalStartedThreadCount();
                String[] strings1 = formatLongs(tlCount, tpCount,
                                                tdCount, ttCount);
                append(Messages.LIVE_THREADS, strings1[0]);
                append(Messages.PEAK, strings1[1]);
                append(Messages.DAEMON_THREADS, strings1[2]);
                append(Messages.TOTAL_THREADS_STARTED, strings1[3]);
                append(endTable);

                append(newRightTable);
                long clCount = clMBean.getLoadedClassCount();
                long cuCount = clMBean.getUnloadedClassCount();
                long ctCount = clMBean.getTotalLoadedClassCount();
                String[] strings2 = formatLongs(clCount, cuCount, ctCount);
                append(Messages.CURRENT_CLASSES_LOADED, strings2[0]);
                append(Messages.TOTAL_CLASSES_LOADED, strings2[2]);
                append(Messages.TOTAL_CLASSES_UNLOADED, strings2[1]);
                append(null, "");
                append(endTable);
            }

            append(newDivider);

            {  // Memory
                MemoryUsage u = memoryBean.getHeapMemoryUsage();

                append(newLeftTable);
                String[] strings1 = formatKByteStrings(u.getUsed(), u.getMax());
                append(Messages.CURRENT_HEAP_SIZE, strings1[0]);
                append(Messages.MAXIMUM_HEAP_SIZE, strings1[1]);
                append(endTable);

                append(newRightTable);
                String[] strings2 = formatKByteStrings(u.getCommitted());
                append(Messages.COMMITTED_MEMORY,  strings2[0]);
                append(Messages.SUMMARY_TAB_PENDING_FINALIZATION_LABEL,
                       Resources.format(Messages.SUMMARY_TAB_PENDING_FINALIZATION_VALUE,
                                        memoryBean.getObjectPendingFinalizationCount()));
                append(endTable);

                append(newTable);
                Collection<GarbageCollectorMXBean> garbageCollectors =
                                            proxyClient.getGarbageCollectorMXBeans();
                for (GarbageCollectorMXBean garbageCollectorMBean : garbageCollectors) {
                    String gcName = garbageCollectorMBean.getName();
                    long gcCount = garbageCollectorMBean.getCollectionCount();
                    long gcTime = garbageCollectorMBean.getCollectionTime();

                    append(Messages.GARBAGE_COLLECTOR,
                           Resources.format(Messages.GC_INFO, gcName, gcCount,
                                            (gcTime >= 0) ? formatTime(gcTime)
                                                 : Messages.UNAVAILABLE),
                           4);
                }
                append(endTable);
            }

            append(newDivider);

            {  // Operating System info
                append(newLeftTable);
                String osName = osMBean.getName();
                String osVersion = osMBean.getVersion();
                String osArch = osMBean.getArch();
                result.nCPUs = osMBean.getAvailableProcessors();
                append(Messages.OPERATING_SYSTEM, osName + " " + osVersion);
                append(Messages.ARCHITECTURE, osArch);
                append(Messages.NUMBER_OF_PROCESSORS, result.nCPUs+"");

                if (pathSeparator == null) {
                    // Must use separator of remote OS, not File.pathSeparator
                    // from this local VM. In the future, consider using
                    // RuntimeMXBean to get the remote system property.
                    pathSeparator = osName.startsWith("Windows ") ? ";" : ":";
                }

                if (sunOSMBean != null) {
                    String[] kbStrings1 =
                        formatKByteStrings(sunOSMBean.getCommittedVirtualMemorySize());

                    String[] kbStrings2 =
                        formatKByteStrings(sunOSMBean.getTotalPhysicalMemorySize(),
                                           sunOSMBean.getFreePhysicalMemorySize(),
                                           sunOSMBean.getTotalSwapSpaceSize(),
                                           sunOSMBean.getFreeSwapSpaceSize());

                    append(Messages.COMMITTED_VIRTUAL_MEMORY, kbStrings1[0]);
                    append(endTable);

                    append(newRightTable);
                    append(Messages.TOTAL_PHYSICAL_MEMORY, kbStrings2[0]);
                    append(Messages.FREE_PHYSICAL_MEMORY,  kbStrings2[1]);
                    append(Messages.TOTAL_SWAP_SPACE,      kbStrings2[2]);
                    append(Messages.FREE_SWAP_SPACE,       kbStrings2[3]);
                }

                append(endTable);
            }

            append(newDivider);

            {  // VM arguments and paths
                append(newTable);
                String args = "";
                java.util.List<String> inputArguments = rmBean.getInputArguments();
                for (String arg : inputArguments) {
                    args += arg + " ";
                }
                append(Messages.VM_ARGUMENTS, args, 4);
                append(Messages.CLASS_PATH,   rmBean.getClassPath(), 4);
                append(Messages.LIBRARY_PATH, rmBean.getLibraryPath(), 4);
                append(Messages.BOOT_CLASS_PATH,
                       rmBean.isBootClassPathSupported()
                                    ? rmBean.getBootClassPath()
                                    : Messages.UNAVAILABLE,
                       4);
                append(endTable);
            }
        } catch (IOException e) {
            if (JConsole.isDebug()) {
                e.printStackTrace();
            }
            proxyClient.markAsDead();
            return null;
        } catch (UndeclaredThrowableException e) {
            if (JConsole.isDebug()) {
                e.printStackTrace();
            }
            proxyClient.markAsDead();
            return null;
        }

        append("</table>");

        result.timeStamp = System.currentTimeMillis();
        result.summary = buf.toString();

        return result;
    }

    private synchronized void append(String str) {
        buf.append(str);
    }

    void append(String label, String value) {
        append(newRow(label, value));
    }

    private void append(String label, String value, int columnPerRow) {
        if (columnPerRow == 4 && pathSeparator != null) {
            value = value.replace(pathSeparator,
                                  "<b></b>" + pathSeparator);
        }
        append(newRow(label, value, columnPerRow));
    }

    OverviewPanel[] getOverviewPanels() {
        if (overviewPanel == null) {
            overviewPanel = new CPUOverviewPanel();
        }
        return new OverviewPanel[] { overviewPanel };
    }

    private static class CPUOverviewPanel extends OverviewPanel {
        private long prevUpTime, prevProcessCpuTime;

        CPUOverviewPanel() {
            super(Messages.CPU_USAGE, cpuUsageKey, Messages.CPU_USAGE, Plotter.Unit.PERCENT);
            getPlotter().setDecimals(CPU_DECIMALS);
        }

        public void updateCPUInfo(Result result) {
            if (prevUpTime > 0L && result.upTime > prevUpTime) {
                // elapsedCpu is in ns and elapsedTime is in ms.
                long elapsedCpu = result.processCpuTime - prevProcessCpuTime;
                long elapsedTime = result.upTime - prevUpTime;
                // cpuUsage could go higher than 100% because elapsedTime
                // and elapsedCpu are not fetched simultaneously. Limit to
                // 99% to avoid Plotter showing a scale from 0% to 200%.
                float cpuUsage =
                    Math.min(99F,
                             elapsedCpu / (elapsedTime * 10000F * result.nCPUs));

                cpuUsage = Math.max(0F, cpuUsage);

                getPlotter().addValues(result.timeStamp,
                                Math.round(cpuUsage * Math.pow(10.0, CPU_DECIMALS)));
                getInfoLabel().setText(Resources.format(Messages.CPU_USAGE_FORMAT,
                                               String.format("%."+CPU_DECIMALS+"f", cpuUsage)));
            }
            this.prevUpTime = result.upTime;
            this.prevProcessCpuTime = result.processCpuTime;
        }
    }
}
