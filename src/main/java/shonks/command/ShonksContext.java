package shonks.command;

import shonks.storage.Storage;
import shonks.task.TaskList;
import shonks.ui.Ui;

/**
 * Encapsulates dependencies required to execute a command.
 */
public class ShonksContext {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final ExitStatus exitStatus;

    /**
     * Constructs a context object.
     *
     * @param storage The storage used to persist tasks.
     * @param tasks The in-memory task list.
     * @param ui The UI used to output messages.
     * @param exitStatus Tracks whether exit has been requested.
     */
    public ShonksContext(Storage storage, TaskList tasks, Ui ui, ExitStatus exitStatus) {
        this.storage = storage;
        this.tasks = tasks;
        this.ui = ui;
        this.exitStatus = exitStatus;
    }

    /**
     * Returns the storage instance.
     *
     * @return The storage instance.
     */
    public Storage storage() {
        return storage;
    }

    /**
     * Returns the task list.
     *
     * @return The task list.
     */
    public TaskList tasks() {
        return tasks;
    }

    /**
     * Returns the UI instance.
     *
     * @return The UI instance.
     */
    public Ui ui() {
        return ui;
    }

    /**
     * Returns the exit status tracker.
     *
     * @return The exit status tracker.
     */
    public ExitStatus exitStatus() {
        return exitStatus;
    }
}
