package shonks.command.handlers;

import shonks.ShonksException;
import shonks.command.ShonksContext;
import shonks.task.Task;

/**
 * Utility methods used by command handlers.
 */
public class HandlerUtil {

    /**
     * Saves the current task list to storage.
     *
     * @param context The execution context.
     * @throws ShonksException If saving fails.
     */
    public static void save(ShonksContext context) throws ShonksException {
        context.storage().save(context.tasks().getInternalList());
    }

    /**
     * Adds a task and reports through the UI.
     *
     * @param context The execution context.
     * @param task The task to add.
     * @throws ShonksException If saving fails.
     */
    public static void addTask(ShonksContext context, Task task) throws ShonksException {
        context.tasks().add(task);
        save(context);
        context.ui().showAdded(task, context.tasks().size());
    }

    /**
     * Retrieves a task by 1-based index.
     *
     * @param context The execution context.
     * @param oneBasedIndex The 1-based task index.
     * @return The task.
     * @throws ShonksException If the index is null or invalid.
     */
    public static Task getTaskOrThrow(ShonksContext context, Integer oneBasedIndex) throws ShonksException {
        int index = requireIndex(oneBasedIndex);
        int i = index - 1;

        if (i < 0 || i >= context.tasks().size()) {
            throw new ShonksException("That task number does not exist.");
        }

        return context.tasks().get(i);
    }

    /**
     * Removes a task by 1-based index.
     *
     * @param context The execution context.
     * @param oneBasedIndex The 1-based task index.
     * @return The removed task.
     * @throws ShonksException If the index is null or invalid.
     */
    public static Task removeTaskOrThrow(ShonksContext context, Integer oneBasedIndex) throws ShonksException {
        int index = requireIndex(oneBasedIndex);
        int i = index - 1;

        if (i < 0 || i >= context.tasks().size()) {
            throw new ShonksException("That task number does not exist.");
        }

        return context.tasks().remove(i);
    }

    private static int requireIndex(Integer oneBasedIndex) throws ShonksException {
        if (oneBasedIndex == null) {
            throw new ShonksException("Please provide a task number.");
        }
        return oneBasedIndex;
    }
}
