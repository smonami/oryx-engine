package org.jodaengine.example;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.ext.logger.NavigatorListenerLogger;
import org.jodaengine.factory.node.HumanTaskNodeFactory;
import org.jodaengine.factory.node.PrintingNodeFactory;
import org.jodaengine.factory.node.RoutingBehaviourTestFactory;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNTokenImpl;



/**
 * The Class that holds the example process that needs as review process for the engine.
 */
public final class SimpleHumanTaskProcess {
    
    /** The Constant SLEEP_TIME. */
    private static final int SLEEP_TIME = 5000;

    /** Hidden constructor. */
    private SimpleHumanTaskProcess() {
        
    }

    /**
     * The main method of our example process for review.
     * 
     * @param args
     *            the arguments
     * @throws InterruptedException
     *             the exception if an interrupt occurs
     */
    public static void main(String[] args)
    throws InterruptedException {
        
        // the main
        NavigatorImpl navigator = new NavigatorImpl();
        navigator.registerListener(NavigatorListenerLogger.getInstance());
        navigator.start(new JodaEngine());
        
        BPMNTokenImpl token = processTokenForReview();
        navigator.startArbitraryInstance(token);
        
        Thread.sleep(SLEEP_TIME);
        
        navigator.stop();
    }
    
    /**
     * Creates the process token for the reviewProcess.
     * 
     * @return the process token impl
     */
    private static BPMNTokenImpl processTokenForReview() {


        Node startNode = new RoutingBehaviourTestFactory().createWithAndSplitAndLogger();
        
        Node secondNode = new HumanTaskNodeFactory().createWithLogger();

        Node thirdNode = new PrintingNodeFactory().createWithLogger();
        
        // TODO @Gerardo: hier gilt dasselbe wie im ExampleProcessForReview.
//        Node endNode = new NodeImpl(BpmnEndActivity.class);

        // Setting the transitions
        startNode.transitionTo(secondNode);
        secondNode.transitionTo(thirdNode);
//        thirdNode.transitionTo(endNode);

        BPMNTokenImpl sampleToken = new BPMNTokenImpl(startNode, new ProcessInstanceImpl(null), null);
        return sampleToken;
    }

}
