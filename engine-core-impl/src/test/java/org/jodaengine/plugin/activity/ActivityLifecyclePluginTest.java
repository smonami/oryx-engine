package org.jodaengine.plugin.activity;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.TokenImpl;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * Test class for various activity plugin tests.
 */
public class ActivityLifecyclePluginTest {

    private TokenImpl token = null;
    private ArgumentCaptor<ActivityLifecycleChangeEvent> eventCapturer = null;

    /**
     * Setup method.
     */
    @BeforeTest
    public void setUp() {

        String dummyString = "s.out";

        this.token = new TokenImpl(new NodeImpl(new AutomatedDummyActivity(dummyString), new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour()));
        this.eventCapturer = ArgumentCaptor.forClass(ActivityLifecycleChangeEvent.class);
    }

    /**
     * Tests that the plugin is called twice during activity lifecycle (active, completed).
     * It's final state will be completed.
     * 
     * @throws JodaEngineException
     */
    @Test
    public void testStartedTrigger()
    throws JodaEngineException {

        AbstractTokenPlugin mock = mock(AbstractTokenPlugin.class);
        this.token.registerPlugin(mock);
        token.executeStep();

        verify(mock, times(2)).update(eq(this.token), this.eventCapturer.capture());
        Assert.assertEquals(ActivityState.COMPLETED, this.eventCapturer.getValue().getNewState());
    }
}
