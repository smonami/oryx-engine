package org.jodaengine.correlation.adapter;

import javax.annotation.Nonnegative;

import org.quartz.Job;

import org.jodaengine.correlation.EventConfiguration;

/**
 * The Interface TimedAdapterConfiguration. This should be used for adapters or events
 * which have to be called in specific time intervals.
 * 
 * @author Jannik Streek
 */
public interface TimedConfiguration extends EventConfiguration {

    /**
     * Returns the adapter's time interval in ms.
     * 
     * @return the pull interval in ms
     */
    @Nonnegative
    long getTimeInterval();

    /**
     * Gets the scheduled class which is used for the QUARTZ Scheduler.
     * 
     * @return the scheduled class
     */
    Class<? extends Job> getScheduledClass();
}