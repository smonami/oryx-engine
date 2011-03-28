package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class AutomatedDummyNode.
 * It really is dumb. It just prints out whatever message is send to it.
 */
public class AutomatedDummyActivity extends AbstractActivity {

    /** This is the message the node prints out during its execution. */
    private String message;

    /**
     * Instantiates a new automated dummy node.
     *  
     * @param s the String which message gets set to and which gets printed out.
     */
    public AutomatedDummyActivity(@Nullable String s) {
        super();
        this.message = s;
    }

    @Override
    protected void executeIntern(@Nonnull Token instance) {
        System.out.println(this.message);
    }

}
