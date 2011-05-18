package org.jodaengine.eventmanagement.timing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.PullAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.TimedConfiguration;
import org.jodaengine.eventmanagement.adapter.TimerConfigurationImpl;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.mail.InboundMailAdapterConfiguration;
import org.jodaengine.eventmanagement.timing.TimingManager;
import org.jodaengine.eventmanagement.timing.TimingManagerImpl;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenImpl;

import org.quartz.SchedulerException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test class for {@link TimingManagerImpl}.
 */
public class TimingManagerTest {
    
    private static final int PULL_TIMEOUT = 5;
    private static final short VERIFY_FACTOR = 4;
    private static final long TIMER = 100;
    
    private TimingManager timer = null;
    
    /**
     * Tests the registering of a pull adapter and its invocation.
     * 
     * @throws JodaEngineException test fails if either pulling or registering fails
     */
    @Test
    public void testRegisteringAPullAdapter() throws JodaEngineException {
        InboundPullAdapter adapter = mock(InboundPullAdapter.class);
        // Unfortunately mocking doesn't seem to work with classes as return value,
        // therefore the PullAdapterConfiguration is instantiated manually
        PullAdapterConfiguration configuration = new InboundMailAdapterConfiguration(null, null, null,
            null, 0, false);
        when(adapter.getConfiguration()).thenReturn(configuration);
        this.timer.registerPullAdapter(adapter);
        
        verify(adapter, timeout(PULL_TIMEOUT * VERIFY_FACTOR).atLeastOnce()).pull();
    }
    
    /**
     * Test registering a non recurring event, in this case a simple timer.
     * The test waits some time, until the timer should be done and then an assertion is used to test
     * if resume was called on the token.
     *
     * @throws JodaEngineException the JodaEngine exception
     * @throws InterruptedException the interrupted exception for thread sleeping
     */
    @Test
    public void testRegisteringANonRecurringEvent() throws JodaEngineException, InterruptedException {
        Token token = mock(TokenImpl.class);
        TimedConfiguration configuration = new TimerConfigurationImpl(TIMER);
        this.timer.registerNonRecurringJob(configuration, token);
        Thread.sleep(TIMER + TIMER);
        verify(token).resume();
    }
    
    /**
     * Test registering a non recurring event, in this case a simple timer. Because waiting time is not long enough,
     * the token should not be resumed.
     *
     * @throws JodaEngineException the JodaEngine exception
     * @throws InterruptedException the interrupted exception for thread sleeping
     */
    @Test
    public void testFailingRegisteringANonRecurringEvent() throws JodaEngineException, InterruptedException {
        Token token = mock(TokenImpl.class);
        TimedConfiguration configuration = new TimerConfigurationImpl(TIMER);
        this.timer.registerNonRecurringJob(configuration, token);
        verify(token, never()).resume();
        // Wait until job is deleted, otherwise conflicts can occur
        Thread.sleep(TIMER + TIMER);
    }

    /**
     * Setup.
     * @throws SchedulerException test fails
     */
    @BeforeMethod
    public void beforeMethod()
    throws SchedulerException {
        ErrorAdapter errorAdapter = mock(ErrorAdapter.class);
//        CorrelationManagerImpl correlation = mock(CorrelationManagerImpl.class);
        this.timer = new TimingManagerImpl(errorAdapter);
    }

    /**
     * Cleanup.
     */
    @AfterMethod
    public void afterMethod() {
        this.timer = null;
    }

}