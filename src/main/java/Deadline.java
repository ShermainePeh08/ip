/**
 * Represents a task that must be completed before a specified deadline.
 */
public class Deadline extends Task {
    private final String by;

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

    @Override
    protected String getStorageType() {
        return "D";
    }

    @Override
    protected String getStorageExtra() {
        return " | " + by;
    }
}