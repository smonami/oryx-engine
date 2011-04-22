package de.hpi.oryxengine.process.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The Class ProcessInstanceContextImpl.
 */
public class ProcessInstanceContextImpl implements ProcessInstanceContext {
    
    @JsonIgnore
    private Map<Node, List<Transition>> waitingTransitions;

    private Map<String, Object> contextVariables;

    /**
     * Instantiates a new process instance context impl.
     */
    public ProcessInstanceContextImpl() {

        waitingTransitions = new HashMap<Node, List<Transition>>();
    }

    @Override
    public void setWaitingExecution(Transition t) {

        List<Transition> tmpList;
        if (waitingTransitions.get(t.getDestination()) != null) {
            tmpList = waitingTransitions.get(t.getDestination());

        } else {
            tmpList = new ArrayList<Transition>();
            waitingTransitions.put(t.getDestination(), tmpList);
        }
        tmpList.add(t);

    }

    @Override
    public List<Transition> getWaitingExecutions(Node n) {

        return waitingTransitions.get(n);
    }

    @Override
    public boolean allIncomingTransitionsSignaled(Node n) {

        List<Transition> signaledTransitions = waitingTransitions.get(n);

        // If there are no waiting transitions yet, the list might not be initialized yet
        if (signaledTransitions == null) {
            return false;
        }
        List<Transition> incomingTransitions = n.getIncomingTransitions();
        return signaledTransitions.containsAll(incomingTransitions);
    }

    @Override
    public void removeIncomingTransitions(Node n) {

        List<Transition> signaledTransitions = waitingTransitions.get(n);
        List<Transition> incomingTransitions = n.getIncomingTransitions();
        removeSubset(signaledTransitions, incomingTransitions);

    }

    /**
     * Removes the subset.
     * 
     * @param superList
     *            the super list
     * @param subList
     *            the sub list
     */
    private void removeSubset(List<Transition> superList, List<Transition> subList) {

        for (Transition t : subList) {
            superList.remove(t);
        }
    }

    @Override
    public Map<String, Object> getVariableMap() {
    
        return getInstanceVariables();
    }

    @Override
    public void setVariable(String name, Object value) {

        getInstanceVariables().put(name, value);
    }

    @Override
    public Object getVariable(String name) {

        return getInstanceVariables().get(name);
    }

    /**
     * Gets the instance variables.
     * 
     * @return the instance variables
     */
    @JsonProperty
    private Map<String, Object> getInstanceVariables() {
        
        if (contextVariables == null) {
            contextVariables = Collections.synchronizedMap(new HashMap<String, Object>());
        }
        return contextVariables;
    }
    
    
    @Override
    public boolean equals(@Nullable Object object) {
        
        if (object instanceof ProcessInstanceContextImpl) {
            ProcessInstanceContextImpl context = (ProcessInstanceContextImpl) object;
            
            if (getInstanceVariables().equals(context.getInstanceVariables())) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
// CHECKSTYLE:OFF
        int hash = 7;
// CHECKSTYLE:ON
        
        Object o;
        for (String key: getInstanceVariables().keySet()) {
            o = getInstanceVariables().get(key);
// CHECKSTYLE:OFF
            hash = 31 * hash + (null == o ? 0 : o.hashCode());
// CHECKSTYLE:ON
        }
        
        return hash;
    }
}
