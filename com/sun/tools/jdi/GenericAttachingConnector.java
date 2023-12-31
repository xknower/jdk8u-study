package com.sun.tools.jdi;

import java.io.IOException;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.*;
import com.sun.jdi.connect.spi.*;

/*
 * An AttachingConnector to attach to a running VM using any
 * TransportService.
 */

public class GenericAttachingConnector
        extends ConnectorImpl implements AttachingConnector
{
    /*
     * The arguments that this connector supports
     */
    static final String ARG_ADDRESS = "address";
    static final String ARG_TIMEOUT = "timeout";

    TransportService transportService;
    Transport transport;

    /*
     * Initialize a new instance of this connector. The connector
     * encapsulates a transport service and optionally has an
     * "address" connector argument.
     */
    private GenericAttachingConnector(TransportService ts,
                                      boolean addAddressArgument)
    {
        transportService = ts;
        transport = new Transport() {
                public String name() {
                    // delegate name to the transport service
                    return transportService.name();
                }
            };

        if (addAddressArgument) {
            addStringArgument(
                ARG_ADDRESS,
                getString("generic_attaching.address.label"),
                getString("generic_attaching.address"),
                "",
                true);
        }


        addIntegerArgument(
                ARG_TIMEOUT,
                getString("generic_attaching.timeout.label"),
                getString("generic_attaching.timeout"),
                "",
                false,
                0, Integer.MAX_VALUE);
    }

    /**
     * Initializes a new instance of this connector. This constructor
     * is used when sub-classing - the resulting connector will have
     * a "timeout" connector argument.
     */
    protected GenericAttachingConnector(TransportService ts) {
        this(ts, false);
    }

    /*
     * Create an instance of this connector. The resulting AttachingConnector
     * will have address and timeout connector arguments.
     */
    public static GenericAttachingConnector create(TransportService ts) {
        return new GenericAttachingConnector(ts, true);
    }

    /**
     * Attach to a target VM using the specified address and Connector arguments.
     */
    public VirtualMachine attach(String address, Map<String, ? extends Connector.Argument> args)
        throws IOException, IllegalConnectorArgumentsException
    {
        String ts  = argument(ARG_TIMEOUT, args).value();
        int timeout = 0;
        if (ts.length() > 0) {
            timeout = Integer.decode(ts).intValue();
        }
        Connection connection = transportService.attach(address, timeout, 0);
        return Bootstrap.virtualMachineManager().createVirtualMachine(connection);
    }

    /**
     * Attach to a target VM using the specified arguments - the address
     * of the target VM is specified by the <code>address</code> connector
     * argument.
     */
    public VirtualMachine
        attach(Map<String,? extends Connector.Argument> args)
        throws IOException, IllegalConnectorArgumentsException
    {
        String address = argument(ARG_ADDRESS, args).value();
        return attach(address, args);
    }

    public String name() {
        return transport.name() + "Attach";
    }

    public String description() {
        return transportService.description();
    }

    public Transport transport() {
        return transport;
    }

}
