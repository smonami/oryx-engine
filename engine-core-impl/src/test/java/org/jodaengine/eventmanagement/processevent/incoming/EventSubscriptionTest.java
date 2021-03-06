package org.jodaengine.eventmanagement.processevent.incoming;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.start.BaseIncomingStartProcessEvent;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the start event subscriptions.
 */
public class EventSubscriptionTest extends AbstractJodaEngineTest {

    private EventManager eventManager;
    private AbstractCorrelatingEventAdapter<?> correlationAdapter;
    private AdapterConfiguration adapterConfiguration;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        
        this.eventManager = new EventManager();
        eventManager.start(this.jodaEngineServices);

        correlationAdapter = Mockito.mock(AbstractCorrelatingEventAdapter.class);
        adapterConfiguration = Mockito.mock(AdapterConfiguration.class);
        Mockito.when(adapterConfiguration.registerAdapter(Mockito.any(AdapterManagement.class))).thenReturn(
            correlationAdapter);
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {

        eventManager.stop();
    }

    /**
     * Test process start event is in list of adapter.
     */
    @Test
    public void testForwardingProcessStartEventSubscription() {

        IncomingStartProcessEvent startEvent = Mockito.mock(BaseIncomingStartProcessEvent.class);
        Mockito.when(startEvent.getAdapterConfiguration()).thenReturn(adapterConfiguration);

        eventManager.subscribeToStartEvent(startEvent);

        ArgumentCaptor<BaseIncomingStartProcessEvent> event = ArgumentCaptor.forClass(BaseIncomingStartProcessEvent.class);
        Mockito.verify(correlationAdapter).subscribeToStartEvent(event.capture());
    }

    /**
     * Tests the forwarding process of calls when an intermediate event is subscribed.
     */
    @Test
    public void testForwardingProcessIntermediateEventSubscription() {

        IncomingIntermediateProcessEvent intermediateEvent = Mockito.mock(IncomingIntermediateProcessEvent.class);
        Mockito.when(intermediateEvent.getAdapterConfiguration()).thenReturn(adapterConfiguration);

        eventManager.subscribeToIncomingIntermediateEvent(intermediateEvent);

        ArgumentCaptor<IncomingIntermediateProcessEvent> event = ArgumentCaptor.forClass(IncomingIntermediateProcessEvent.class);
        Mockito.verify(correlationAdapter).subscribeToIncomingIntermediateEvent(event.capture());
    }
}
