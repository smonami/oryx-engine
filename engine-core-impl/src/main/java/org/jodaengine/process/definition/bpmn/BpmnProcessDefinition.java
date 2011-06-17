package org.jodaengine.process.definition.bpmn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.eventmanagement.processevent.incoming.StartProcessEvent;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.process.activation.ProcessDeActivationPattern;
import org.jodaengine.process.activation.pattern.NullProcessDefinitionActivationPattern;
import org.jodaengine.process.definition.AbstractProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instantiation.StartProcessInstantiationPattern;
import org.jodaengine.process.instantiation.pattern.StartNullInstantiationPattern;
import org.jodaengine.process.structure.Node;

/**
 * The Class ProcessDefinitionImpl. A process definition consists of a list of start nodes that, as we have a tree
 * structure of nodes, reference all the nodes of the definition transitively.
 */
public class BpmnProcessDefinition extends AbstractProcessDefinition {

    @JsonIgnore
    private Map<StartProcessEvent, Node> startTriggers;

    /**
     * Instantiates a new {@link ProcessDefinition}. The name is the ID of the {@link ProcessDefinition}.
     * 
     * @param id
     *            - the internal of the {@link ProcessDefinition}
     * @param name
     *            - the name of the {@link ProcessDefinition}
     * @param description
     *            - the description of the {@link ProcessDefinition}
     * @param startNodes
     *            - the initial nodes that refer to the whole node-tree
     */
    public BpmnProcessDefinition(ProcessDefinitionID id, String name, String description, List<Node> startNodes) {

        this(id, name, description, startNodes, new StartNullInstantiationPattern(),
            new NullProcessDefinitionActivationPattern());
    }

    /**
     * Instantiates a new process definition. A UUID is generated randomly.
     * 
     * @param id
     *            - the internal of the {@link ProcessDefinition}
     * @param name
     *            - the name of the {@link ProcessDefinition}
     * @param description
     *            - the description of the {@link ProcessDefinition}
     * @param startNodes
     *            - the initial nodes that refer to the whole node-tree
     */
    public BpmnProcessDefinition(ProcessDefinitionID id,
                                 String name,
                                 String description,
                                 List<Node> startNodes,
                                 StartProcessInstantiationPattern startInstantiationPattern,
                                 ProcessDeActivationPattern startActivationPattern) {

        super(id, name, description, startNodes, startInstantiationPattern, startActivationPattern);
    }

    /**
     * Hidden constructor for the deserialzation of rest-easy.
     */
    protected BpmnProcessDefinition() {

        super(null, null, null, null, null, null);
    }

    @Override
    public Map<StartProcessEvent, Node> getStartTriggers() {

        return new HashMap<StartProcessEvent, Node>(getLazyStartTriggers());
    }

    /**
     * Getter for the startTrigger. Implemented lazy initialized
     * @return the map of startTrigger; a mapping from a {@link StartProcessEvent} to a {@link Node}
     */
    private Map<StartProcessEvent, Node> getLazyStartTriggers() {

        if (startTriggers == null) {
            this.startTriggers = new HashMap<StartProcessEvent, Node>();
        }
        return startTriggers;
    }

    @Override
    public void addStartTrigger(StartProcessEvent event, Node node)
    throws IllegalStarteventException {

        if (startNodes.contains(node)) {
            getLazyStartTriggers().put(event, node);
        } else {
            throw new IllegalStarteventException();
        }
    }
}
