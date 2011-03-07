package de.hpi.oryxengine.process.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.EmptyRoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.join.JoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.join.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.SplitBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.impl.TakeAllSplitBehaviour;

/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl
implements Node {

    /**
     * The activity. This is the behaviour of the node e.g. what gets executed.
     * */
    private Activity activity;

    /** The routing behaviour. E.g. incoming and outgoing transitions. */
    private SplitBehaviour outgoingBehaviour;
    private JoinBehaviour incomingBehaviour;

    /** The next node. */
    private List<Transition> transitions;

    /** The id. */
    private UUID id;

    /**
     * Instantiates a new abstract node.
     * 
     * @param activity the activity to be executed
     * @param behaviour the behaviour of the node
     */
    public NodeImpl(Activity activity,
                    JoinBehaviour incomingBehaviour,
                    SplitBehaviour outgoingBehaviour) {

        this.activity = activity;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.transitions = new ArrayList<Transition>();
    }
    

    public SplitBehaviour getOutgoingBehaviour() {
    
        return outgoingBehaviour;
    }


    public void setOutgoingBehaviour(SplitBehaviour outgoingBehaviour) {
    
        this.outgoingBehaviour = outgoingBehaviour;
    }


    public JoinBehaviour getIncomingBehaviour() {
    
        return incomingBehaviour;
    }


    public void setIncomingBehaviour(JoinBehaviour incomingBehaviour) {
    
        this.incomingBehaviour = incomingBehaviour;
    }


    /**
     * Instantiates a new node impl.
     * 
     * @param activity
     *            the activity
     */
    public NodeImpl(Activity activity) {
        this(activity, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Activity getActivity() {
        return activity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActivity(Activity activity) {

        this.activity = activity;
    }

    /**
     * Transition to the next node.
     *
     * @param node the node
     * {@inheritDoc}
     */
    @Override
    public void transitionTo(Node node) {
        Condition c = new ConditionImpl();
        createTransitionWithCondition(node, c);
    }
    
    /**
     * Creates the transition with condition.
     *
     * @param node the destination
     * @param c the condition
     */
    private void createTransitionWithCondition(Node node, Condition c) {
        Transition t = new TransitionImpl(this, node, c);
        this.transitions.add(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getID() {
        return this.id;
    }

    /**
     * Sets the transitions.
     * 
     * @param transitions the new transitions
     */
    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProcessInstance> execute(ProcessInstance instance) {
        List<ProcessInstance> instances = this.incomingBehaviour.join(instance);
        this.activity.execute(instance);
        return this.outgoingBehaviour.split(instances);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void transitionToWithCondition(Node node, Condition c) {
        createTransitionWithCondition(node, c);
        
    }
}
