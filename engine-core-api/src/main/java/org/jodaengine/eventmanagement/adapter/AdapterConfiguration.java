package org.jodaengine.eventmanagement.adapter;

import org.jodaengine.eventmanagement.AdapterRegistrar;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.EventConfiguration;

/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration extends EventConfiguration {

    
    // TODO not everyone is a TimingManager @ throws  
    /**
     * Registers the adapter for this configuration.
     *
     * @param adapterRegistrar - the place where you can register your adapters
     * @param correlationService - the Correlation Service needed to correlate events back
     * @return the schedule {@link CorrelationAdapter}
     */
    CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar, CorrelationManager correlationService);
}
