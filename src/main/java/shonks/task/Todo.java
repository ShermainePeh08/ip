package shonks.task;
/**
 * Represents a todo task with only a description.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the given description.
     *
     * @param description The task description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }

    @Override
    protected String getStorageType() {
        return "T";
    }
}