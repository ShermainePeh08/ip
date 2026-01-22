/**
 * Represents a task that must be completed before a specified deadline.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task with a description and deadline time.
     *
     * @param description Description of the task.
     * @param by Deadline time.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    protected String getDetails() {
        return " (by: " + by + ")";
    }
}
