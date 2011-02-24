package de.hpi.oryxengine.processstructure;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processstructure.Condition;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;
import de.hpi.oryxengine.routingBehaviour.impl.EmptyRoutingBehaviour;

// TODO Auto-generated Javadoc
/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node {

    /** The activity. */
    private Activity activity;

    /** The routing behaviour. */
    private RoutingBehaviour behaviour;

    /** The next node. */
    private ArrayList<Transition> transitions;

    private String id;

    /**
     * Instantiates a new abstract node.
     *
     * @param activity the activity to be executed
     * @param behaviour the behaviour of the node
     */
    public NodeImpl(Activity activity, RoutingBehaviour behaviour) {

        this.activity = activity;
        this.behaviour = behaviour;
        this.transitions = new ArrayList<Transition>();
    }

    public NodeImpl(Activity activity) {

        this(activity, new EmptyRoutingBehaviour());
    }

    /**
     * Gets the activity.
     * 
     * @return the activity to be executed
     */
    public Activity getActivity() {

        return activity;
    }

    /**
     * Sets the activity.
     * 
     * @param activity
     *            the new activity
     */
    public void setActivity(Activity activity) {

        this.activity = activity;
    }

    /**
     * Sets the next node.
     *
     * @param node the next node where the transition points to
     */

    public void transitionTo(Node node) {

        Condition c = new ConditionImpl();
        Transition t = new TransitionImpl(this, node, c);
        this.transitions.add(t);
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }
    
    public RoutingBehaviour getBehaviour() {
        
        return behaviour;
    }

    public void setBehaviour(RoutingBehaviour behaviour) {
    
        this.behaviour = behaviour;
    }

    public void setTransitions(ArrayList<Transition> transitions) {
    
        this.transitions = transitions;
    }

    public ArrayList<Transition> getTransitions() {

        return transitions;
    }

    public void setRoutingBehaviour(RoutingBehaviour behaviour) {

        this.behaviour = behaviour;
    }

    public RoutingBehaviour getRoutingBehaviour() {

        return behaviour;
    }
}
