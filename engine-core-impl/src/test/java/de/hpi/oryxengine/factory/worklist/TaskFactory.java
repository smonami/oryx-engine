package de.hpi.oryxengine.factory.worklist;

import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskImpl;
import de.hpi.oryxengine.allocation.pattern.SimplePullPattern;
import de.hpi.oryxengine.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.Participant;

/**
 * Little factory for creating Resources. A short cut for the implementation.
 */
public final class TaskFactory {

    public static final String SIMPLE_TASK_SUBJECT = "Get Gerardo a cup of coffee!";
    public static final String SIMPLE_TASK_DESCRIPTION = "You know what I mean.";

    /**
     * Private Constructor because the CheckStyle want me to do that. Gerardo do what told. Gerardo intelligent. Gerardo
     * checkstyle also want you to comment methods. Gerardo better do that.
     */
    private TaskFactory() { }

    /**
     * Creates a new Task object where Jannik shall get coffee for Gerardo. Note that the Participant who shall complete
     * this task (Jannik) is also created in the course of this method(side effect).
     * 
     * @return the task
     */
    public static Task createJannikServesGerardoTask() {
        
        Pattern pushPattern = new DirectPushPattern();
        Pattern pullPattern = new SimplePullPattern();

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);

        // creates the participant Jannik
        AbstractResource<?> resource = ParticipantFactory.createJannik();

        Task task = new TaskImpl(SIMPLE_TASK_SUBJECT,
                                 SIMPLE_TASK_DESCRIPTION,
                                 allocationStrategies,
                                 resource);

        return task;
    }
    
    /**
     * Creates a new Task object, however it does NOT create a participant as a sideffect.
     *
     * @param resourceToAssign the resource to assign
     * @return the task
     */
    public static Task createSimpleTask(AbstractResource<?> resourceToAssign) {

        Task task = new TaskImpl(SIMPLE_TASK_SUBJECT, SIMPLE_TASK_DESCRIPTION, null, resourceToAssign);

        return task;
    }
    
    /**
     * Creates a new Task object for a given Participant.
     *
     * @param p the participant
     * @return the task
     */
    public static Task createParticipantTask(AbstractParticipant p) {
        
        Pattern pushPattern = new DirectPushPattern();
        Pattern pullPattern = new SimplePullPattern();

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);

        AbstractResource<?> resource = p;

        Task task = new TaskImpl(SIMPLE_TASK_SUBJECT,
                                 SIMPLE_TASK_DESCRIPTION,
                                 allocationStrategies,
                                 resource);

        return task;
    }

}
