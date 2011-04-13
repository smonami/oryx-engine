package de.hpi.oryxengine.activity;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The test for the automated dummy activity.
 */
public class AutomatedDummyActivityTest {

    /** A temporary print stream. */
    private PrintStream tmp = null;

    /** The byte array output stream. */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /** A dummy string. */
    private String s = "I'm dumb";

    private AutomatedDummyActivity a = null;

    private Token token;
    
    private Navigator nav;

    /**
     * Set up.
     * @throws Exception
     *             the exception
     */
    @BeforeTest
    public void setUp()
    throws Exception {

        tmp = System.out;
        System.setOut(new PrintStream(out));
        
        nav = mock(Navigator.class);
        Node node = new NodeImpl(AutomatedDummyActivity.class);
        ProcessInstance instance = new ProcessInstanceImpl(null);
        Class<?>[] constructorSig = {String.class};
        instance.getContext().setActivityConstructorClasses(node.getID(), constructorSig);
        Object[] params = {s};
        instance.getContext().setActivityParameters(node.getID(), params);
        token = new TokenImpl(node, instance, nav);
    }

//    /**
//     * Test activity initialization.
//     * The activity should not be null if it was instantiated correctly.
//     */
//    @Test
//    public void testActivityInitialization() {
//
//        assertNotNull(a, "It should not be null since it should be instantiated correctly");
//    }

    // @Test
    // public void testStateAfterActivityInitalization(){
    // assertEquals("It should have the state Initialized", State.INIT,
    // a.getState());
    // }

    /**
     * Test execute output.
     * If the activity is executed it should print out the given String.
     * @throws DalmatinaException 
     */
    @Test
    public void testExecuteOutput() throws DalmatinaException {

        token.executeStep();
        assertTrue(out.toString().indexOf(s) != -1, "It should print out the given string when executed");
    }

    /**
     * Test state after execution.
     * After execution the activity should be in state TERMINTAED
     * @throws DalmatinaException 
     */
    @Test
    public void testStateAfterExecution() throws DalmatinaException {

        //a.execute(token);
        token.executeStep();
        assertEquals(token.getCurrentActivityState(), ActivityState.COMPLETED, "It should have the state Completed");
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

        System.setOut(tmp);
    }
}
