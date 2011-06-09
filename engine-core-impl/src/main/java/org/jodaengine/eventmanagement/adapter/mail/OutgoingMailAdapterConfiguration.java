package org.jodaengine.eventmanagement.adapter.mail;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.EventAdapter;

/**
 * The configuration for our outgoing mail adapter with the necessary information to send an email via SMTP.
 */

// TODO well does this implementation more or less equal the one for the POP/IMAP stuff?
public class OutgoingMailAdapterConfiguration extends AbstractMailConfiguration {
    
    /** The protocol. */
    private MailProtocol protocol;
    

    /**
     * Instantiates a new outgoing mail adapter configuration.
     *
     * @param protocol the protocol
     * @param userName the user name
     * @param password the password
     * @param address the address
     * @param port the port
     */
    public OutgoingMailAdapterConfiguration(@Nonnull MailProtocol protocol,
                                            @Nonnull String userName,
                                            @Nonnull String password,
                                            @Nonnull String address,
                                            @Nonnegative int port) {

        super(userName, password, address);
        this.protocol = protocol;
        this.port = port;
        
    }

    /**
     * Gets the protocol used (will mostly be SMTP).
     *
     * @return the protocol
     */
    public MailProtocol getProtocol() {
        
        return protocol;
    }

    
    /**
     * Gets the port used, the default SMTP port is 25.
     *
     * @return the port
     */
    public int getPort() {
    
        return port;
    }

    /** The port. */
    private int port;
    
    private OutgoingMailAdapter createAdapter() {
        OutgoingMailAdapter outgoingMailAdapter = new OutgoingMailAdapter(this);
        return outgoingMailAdapter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        // TODO Auto-generated method stub
        return null;
    }

}
