package de.hpi.oryxengine.node.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractJodaEngineTest;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.node.activity.bpmn.BpmnHumanTaskActivity;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.pattern.AllocateSinglePattern;
import de.hpi.oryxengine.resource.allocation.pattern.ConcreteResourcePattern;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

/**
 * The test for the {@link BpmnHumanTaskActivity}.
 */

public class HumanTaskActivityTest extends AbstractJodaEngineTest {

    private CreationPattern pattern = null;
    private AbstractResource<?> resource = null;

    private BpmnHumanTaskActivity humanTask = null;

    private Token token;

    /**
     * Set up.
     * 
     * @throws Exception
     *             the exception
     */
    @BeforeMethod
    public void setUp()
    throws Exception {

        // Prepare the organisation structure

        IdentityBuilder identityBuilder = new IdentityServiceImpl().getIdentityBuilder();
        AbstractParticipant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");

        resource = participant;

        // Define the task
        String subject = "Jannik, get Gerardo a cup of coffee!";
        String description = "You know what I mean.";

//        Pattern pushPattern = new DirectDistributionPattern();
//        Pattern pullPattern = new SimplePullPattern();
//
//        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        pattern = new ConcreteResourcePattern(subject, description, null, participant);
//        task = new TaskImpl(subject, description, allocationStrategies, participant);

        humanTask = new BpmnHumanTaskActivity(pattern, new AllocateSinglePattern());
        
        Node node = new NodeImpl(humanTask, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        token = new TokenImpl(node, new ProcessInstanceImpl(null), new NavigatorImplMock());
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {

        // Reseting the Worklist Manager after the test case
//        ServiceFactoryForTesting.clearWorklistManager();
    }

    /**
     * Test activity initialization. The activity should not be null if it was instantiated correctly.
     */
    @Test
    public void testActivityInitialization() {

        assertNotNull(humanTask, "It should not be null since it should be instantiated correctly");
    }

    /**
     * Test that the humanTask pushes a task item into the Jannik's worklist.
     */
    @Test
    public void testJannikHasWorklistItem() {

        humanTask.execute(token);

        int worklistSize = ServiceFactory.getWorklistService().getWorklistItems(resource).size();
        String failureMessage = "Jannik should now have 1 item in his worklist, but there are " + worklistSize
            + " item(s) in the worklist.";
        assertTrue(worklistSize == 1, failureMessage);

        AbstractWorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(resource).get(0);
        assertEquals(worklistItem.getSubject(), pattern.getItemSubject());
        assertEquals(worklistItem.getDescription(), pattern.getItemDescription());
    }
}
