package de.hpi.oryxengine.process.instantiation;

import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.util.PatternAppendable;

/**
 * This pattern is designed to be the first instantiationPattern. So it should be able to create an initial
 * {@link AbstractProcessInstance processInstance} so that it can be modified in the next patterns.
 */
public interface StartInstantiationPattern extends PatternAppendable<InstantiationPattern> {

    /**
     * Creates a {@link AbstractProcessInstance processInstance} that can be modified in the following patterns.
     * 
     * @param patternContext
     *            - the {@link InstantiationPatternContext patternContext}
     * @return an {@link AbstractProcessInstance}
     */
    AbstractProcessInstance createProcessInstance(InstantiationPatternContext patternContext);
}
