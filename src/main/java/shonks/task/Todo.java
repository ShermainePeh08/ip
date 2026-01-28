package shonks.task;
/**
 * Represents a todo task with only a description.
 */
public class Todo extends Task {

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