package de.hpi.oryxengine.routingBehaviour.impl;

import de.hpi.oryxengine.routingBehaviour.AbstractRoutingBehaviour;

public class TakeAllBehaviour extends AbstractRoutingBehaviour {

    public TakeAllBehaviour() {

        this.joinBehaviour = new SimpleJoinBehaviour();
        this.splitBehaviour = new TakeAllSplitBehaviour();
    }
}
