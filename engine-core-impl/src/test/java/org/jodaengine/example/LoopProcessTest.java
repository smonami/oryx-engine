package org.jodaengine.example;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.definition.ProcessDefinitionImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.condition.HashMapCondition;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class LoopProcessTest.
 */
public class LoopProcessTest {

    private static final String DEFINITION_NAME = "TestLoopProcess";
    private static final String DEFINITION_DESCRIPTION = "This process tests the loop pattern.";

    private Token token;
    private Node start;
    private Node end;
    private Node xorSplit;
    private Node xorJoin;
    private Node node;
    private static final int RUNTIMES = 3;

    /**
     * Test the example loopProcess. It should execute several RUNTIMES and then end.
     * 
     * @throws JodaEngineException the JodaEngine exception
     */
    @Test
    public void testLoop()
    throws JodaEngineException {

        assertEquals(token.getCurrentNode(), start);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getInstance().getContext().getVariable("counter"), 1);
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getInstance().getContext().getVariable("counter"), 2);
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getInstance().getContext().getVariable("counter"), 2 + 1);
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), end);

    }

    /**
     * Sets up an example process.
     */
    @BeforeClass
    public void setUp() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        start = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        // Create the XORJoin
        xorJoin = BpmnNodeFactory.createBpmnXorGatewayNode(builder);

        // Create a following Node
        node = BpmnCustomNodeFactory.createBpmnAddContextNumbersAndStoreNode(builder, "counter", new String[] {
            "increment", "counter" });

        // Create the XORSplit
        xorSplit = BpmnNodeFactory.createBpmnXorGatewayNode(builder);

        // Create the end
        end = BpmnNodeFactory.createBpmnEndEventNode(builder);

        // Setup XOR DATA
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("counter", RUNTIMES);
        Condition condition1 = new HashMapCondition(map, "<=");
        Condition condition2 = new HashMapCondition(map, "==");

        // Create Transitions
        BpmnNodeFactory.createTransitionFromTo(builder, start, xorJoin);
        BpmnNodeFactory.createTransitionFromTo(builder, xorJoin, node);
        BpmnNodeFactory.createTransitionFromTo(builder, node, xorSplit);
        BpmnNodeFactory.createTransitionFromTo(builder, xorSplit, end, condition2);
        BpmnNodeFactory.createTransitionFromTo(builder, xorSplit, xorJoin, condition1);

        // Bootstrap
        Navigator nav = new NavigatorImplMock();
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(start);
        ProcessDefinition definition = new ProcessDefinitionImpl(UUID.randomUUID(), DEFINITION_NAME,
            DEFINITION_DESCRIPTION, startNodes);
        AbstractProcessInstance instance = new ProcessInstanceImpl(definition);
        instance.getContext().setVariable("counter", "0");
        instance.getContext().setVariable("increment", "1");

        // start node for token is set later on
        token = new TokenImpl(start, instance, nav);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

        token = null;
        start = null;
        end = null;
        xorSplit = null;
        xorJoin = null;
        node = null;

    }
}
