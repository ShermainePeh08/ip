/**
 * Represents a todo task without any date or time.
 */
public class Todo extends Task {
    /**
     * Creates a todo task with the given description.
     *
     * @param description Description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }
}
