package de.hpi.oryxengine.rest.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.adapter.EventTypes;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterEvent;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.EventConditionImpl;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.IdentityBuilder;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */

public final class DemoProcessStartEmailForWebservice {

    private static final Logger logger = LoggerFactory.getLogger(DemoProcessStartEmailForWebservice.class);
    private static final String PATH_TO_XML = "/Users/Gery/Entwicklung/BachelorprojektWorkspace/Oryx-Engine-Git/oryx_engine/engine-core-impl/src/test/resources/de/hpi/oryxengine/deployment/bpmn/SimpleUserTask.bpmn.xml";
    private static IdentityBuilder builder;
    private static boolean invoked = false;

    /**
     * Instantiates a new demo data for webservice.
     */
    private DemoProcessStartEmailForWebservice() {

    }

    /**
     * Resets invoked, to be honest mostly for testign purposed after each method.
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

        builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        return builder;
    }

    /**
     * Generate example Participants.
     */
    public static synchronized void generate() {

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
     */
    private static void generateDemoParticipants() {

    }

    /**
     * Generate demo worklist items for our participants.
     * 
     * @throws IllegalStarteventException
     * @throws DefinitionNotFoundException
     */
    private static void generateDemoWorklistItems()
    throws IllegalStarteventException, DefinitionNotFoundException {

        // Building the ProcessDefintion
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        Node startNode, node1, node2, node3, endNode;

        startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

        // Building Node1
        int[] ints = { 1, 1 };
        node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);

        // Building Node2
        node2 = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(builder, "result");

        endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, endNode);

        builder.setDescription("description").setName("Demoprocess with Email start event");

        ProcessDefinition def = builder.buildDefinition();
        UUID exampleProcessUUID = ServiceFactory.getRepositoryService().getDeploymentBuilder()
        .deployProcessDefinition(new RawProcessDefintionImporter(def));

        // Create a mail adapater event here.
        // TODO @TobiP Could create a builder for this later.
        MailAdapterConfiguration config = MailAdapterConfiguration.dalmatinaGoogleConfiguration();
        EventCondition subjectCondition = null;
        try {
            subjectCondition = new EventConditionImpl(MailAdapterEvent.class.getMethod("getMessageTopic"), "Hallo");
            List<EventCondition> conditions = new ArrayList<EventCondition>();
            conditions.add(subjectCondition);

//            StartEvent event = new StartEventImpl( exampleProcessUUID);

            builder.createStartTrigger(EventTypes.Mail, config, conditions, node1);
            
            ServiceFactory.getRepositoryService().activateProcessDefinition(exampleProcessUUID);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (JodaEngineRuntimeException e) {
            logger.error(e.getMessage(), e);
        }
    }

}