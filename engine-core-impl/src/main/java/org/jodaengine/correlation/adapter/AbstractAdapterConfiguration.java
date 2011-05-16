package org.jodaengine.correlation.adapter;

import javax.annotation.Nonnull;


/**
 * Abstract adapter configuration.
 * 
 * @author Jan Rehwaldt
 */
public abstract class AbstractAdapterConfiguration
implements AdapterConfiguration {
    

    private final EventType type;
    
    /**
     * Default constructor.
     * 
     * @param type the adapter's type.
     */
    public AbstractAdapterConfiguration(@Nonnull EventType type) {
        this.type = type;
    }
    
    @Override
    public final @Nonnull EventType getEventType() {
        return type;
    }
}