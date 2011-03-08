package de.hpi.oryxengine.worklist.pattern;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.WorklistQueue;

/**
 * Simple Pull Pattern - Only for testing.
 */
public class SimplePullPattern implements Pattern {

    @Override
    public void execute(Task task, Token token, WorklistQueue worklistService) {

        System.out.println("Habe es aus der Liste genommen, Juuuuuunge!");
    }

}
