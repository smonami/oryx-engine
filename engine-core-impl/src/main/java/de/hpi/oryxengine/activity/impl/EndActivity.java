package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class EndActivity.
 * Just the activity which gets executed on the endevent.
 * So nothing is done, in the future maybe more should be done.
 */
public class EndActivity
extends AbstractActivity {

    /**
     * Instantiates a new end activity.
     */
    public EndActivity() {
        super();
    }

    @Override
    protected void executeIntern(@Nonnull Token instance) {
        // Doing nothing is the default behavior
        // TODO This must change.
    }
}
