package de.hpi.oryxengine.node.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.node.activity.NullActivity;
import de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.OutgoingBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The test of the routing behavior.
 */
public class RoutingBehaviourTest {

    /** The process token. */
    private Token token = null;

    /**
     * Set up. An instance is build.
     */
    @BeforeClass
    public void setUp() {

        token = simpleToken();

    }

    /**
     * Test class. A routing from the current node to the next node is done. The instance's current node should now be
     * this next node.
     */
    @Test
    public void testClass() {

        Node node = token.getCurrentNode();
        Node nextNode = node.getOutgoingTransitions().get(0).getDestination();

        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();

        List<Token> joinedTokens = incomingBehaviour.join(token);

        try {
            outgoingBehaviour.split(joinedTokens);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(token.getCurrentNode(), nextNode);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

    }

    /**
     * Simple token. An activity is set up, it gets a behavior and a transition to a second node.
     * 
     * @return the process token that was created within the method
     */
    private TokenImpl simpleToken() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        Node node = builder.getNodeBuilder().setActivityBlueprintFor(NullActivity.class)
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        Node node2 = builder.getNodeBuilder().setActivityBlueprintFor(NullActivity.class)
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        builder.getTransitionBuilder().transitionGoesFromTo(node, node2).buildTransition();

        return new TokenImpl(node);
    }
}