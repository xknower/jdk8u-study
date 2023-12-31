package sun.tools.jstat;

import java.util.List;
import sun.jvmstat.monitor.MonitorException;

/**
 * An interface for visitor object on a binary tree.
 *
 * @author Brian Doherty
 * @since 1.5
 */
interface Closure {
    void visit(Object o, boolean hasNext) throws MonitorException;
}
