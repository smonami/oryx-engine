package org.jodaengine.eventmanagement.processevent.incoming;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.AbstractProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * The Class ProcessEventImpl. Have a look at {@link IncomingProcessEvent}.
 */
public abstract class AbstractIncomingProcessEvent extends AbstractProcessEvent implements IncomingProcessEvent {

    private EventCondition condition;

    /**
     * Instantiates a new {@link IncomingProcessEvent}.
     * @param config
     *            the config
     * @param condition
     *            the conditions
     */
    protected AbstractIncomingProcessEvent(AdapterConfiguration config, EventCondition condition) {

        this.config = config;
        this.condition = condition;
    }

    @Override
    public EventCondition getCondition() {

        if (this.condition == null) {
            this.condition = new TrueEventCondition();
        }
        return condition;
    }

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return getCondition().evaluate(adapterEvent);
    }

    @Override
    public abstract void trigger();
}