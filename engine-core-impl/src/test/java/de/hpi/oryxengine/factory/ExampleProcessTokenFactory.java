package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

/**
 * A factory for creating ExampleProcessToken objects.
 * These objects just have 2 add Number activities.
 */
public class ExampleProcessTokenFactory {
    private Node node1;
    private Node node2;
    private Token token;
    
    /**
     * Creates an Example process token which uses just two AddNumbersAndStore Nodes.. a bit of profiling
     *
     * @return the process token
     */
    public Token create() {
        this.initializeNodes();
        this.initializeProcessToken();
        return this.token;
    }
    
    /**
     * Initializes the nodes.
     */
    public void initializeNodes() {
        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        node1 = factory.create();
        node2 = factory.create();
        node1.transitionTo(node2);
    }
    
    /**
     * Initializes the process token.
     */
    public void initializeProcessToken() {
        SimpleProcessTokenFactory factory = new SimpleProcessTokenFactory();
        token = factory.create(node1);        
    }

}