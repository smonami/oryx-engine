package de.hpi.oryxengine.rest.demo;

import java.io.File;
import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.node.factory.TransitionFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.FormImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.RolePushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */
public final class DemoDataForWebservice {
    
    private static final String PATH_TO_WEBFORMS = "src/main/resources/forms";
    private static IdentityBuilder identityBuilder;
    private static AbstractRole r;
    public final static int NUMBER_OF_PROCESSINSTANCES = 10;
    private static boolean invoked = false;
    
    /**
     * Hidden constructor.
     */
    private DemoDataForWebservice() { }
    
    /**
     * Resets invoked, to be honest mostly for testing purposed after each method.
     */
    public synchronized static void resetInvoked() {
        invoked = false;
    }

    /**
     * Gets the builder.
     * 
     * @return the builder
     */
    private static IdentityBuilder getBuilder() {

        identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        return identityBuilder;
    }

    /**
     * Generate example Participants.
     * 
     * @throws ResourceNotAvailableException 
     */
    public static synchronized void generate() throws ResourceNotAvailableException {
        
        if (!invoked) {
            invoked = true;
            generateDemoParticipants();
            try {
                generateDemoWorklistItems();
            } catch (DefinitionNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalStarteventException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generate demo participants.
     * @throws ResourceNotAvailableException 
     */
    private static void generateDemoParticipants() throws ResourceNotAvailableException {
        
        r = getBuilder().createRole("BPT");
        AbstractParticipant p1 = getBuilder().createParticipant("Thorben-demo");
        AbstractParticipant p2 = getBuilder().createParticipant("Tobi P.-demo");
        AbstractParticipant p3 = getBuilder().createParticipant("Tobi M.-demo");
        AbstractParticipant p4 = getBuilder().createParticipant("Gerardo-demo");
        AbstractParticipant p5 = getBuilder().createParticipant("Jan-demo");
        AbstractParticipant p6 = getBuilder().createParticipant("Jannik-demo");
        getBuilder().participantBelongsToRole(p1.getID(), r.getID())
                    .participantBelongsToRole(p2.getID(), r.getID())
                    .participantBelongsToRole(p3.getID(), r.getID())
                    .participantBelongsToRole(p4.getID(), r.getID())
                    .participantBelongsToRole(p5.getID(), r.getID())
                    .participantBelongsToRole(p6.getID(), r.getID());
    }

    /**
     * Generate demo worklist items for our participants.
     * 
     * @throws DefinitionNotFoundException the requested definition was not found
     * @throws IllegalStarteventException the registered start event was missing or not legally defined
     */
    private static void generateDemoWorklistItems()
    throws DefinitionNotFoundException, IllegalStarteventException {

        // TODO Use Example Process to create some tasks for the role demo

        ProcessDefinitionBuilder processBuilder = new ProcessDefinitionBuilderImpl();

        Node startNode, node1, node2, node3, endNode;

        startNode = BpmnNodeFactory.createBpmnStartEventNode(processBuilder);

        // Creating the Webform for the task
        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        UUID processArtifactID = deploymentBuilder.deployArtifactAsFile("form1", new File(PATH_TO_WEBFORMS + "/claimPoints.html"));
        Form form = new FormImpl(ServiceFactory.getRepositoryService().getProcessResource(processArtifactID));

        // Create the task
        AllocationStrategies strategies = new AllocationStrategiesImpl(new RolePushPattern(), new SimplePullPattern(),
            null, null);
        Task task = new TaskImpl("do something", "Really do something we got a demo coming up guys!", form, strategies,
            r);

        node1 = BpmnNodeFactory.createBpmnUserTaskNode(processBuilder, task);

        node2 = BpmnNodeFactory.createBpmnUserTaskNode(processBuilder, task);

        // Create the task
        node3 = BpmnNodeFactory.createBpmnUserTaskNode(processBuilder, task);

        endNode = BpmnNodeFactory.createBpmnEndEventNode(processBuilder);

        TransitionFactory.createTransitionFromTo(processBuilder, startNode, node1);
        TransitionFactory.createTransitionFromTo(processBuilder, node1, node2);
        TransitionFactory.createTransitionFromTo(processBuilder, node2, node3);
        TransitionFactory.createTransitionFromTo(processBuilder, node3, endNode);
        
        // Start Process
        ProcessDefinition processDefinition = processBuilder.setName("Demoprocess")
                                       .setDescription("A simple demo process with three human tasks.")
                                       .buildDefinition();
        
        UUID processID = ServiceFactory.getRepositoryService()
                                       .getDeploymentBuilder()
                                       .deployProcessDefinition(new RawProcessDefintionImporter(processDefinition));
        
        // create more tasks
        for (int i = 0; i < NUMBER_OF_PROCESSINSTANCES; i++) {
            ServiceFactory.getNavigatorService().startProcessInstance(processID);
        }

    }
}