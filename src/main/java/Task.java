/**
 * Represents a generic task with a description and completion status.
 * <p>
 * This class serves as the base class for all task types in Shonks.
 */
public class Task {
    protected final String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmarkDone() {
        this.isDone = false;
    }

    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    protected String getTypeIcon() {
        // override by subclasses
        return " "; 
    }

    protected String getDetails() {
        // override by subclasses
        return ""; 
    }

    /**
     * Returns the formatted representation of this task for list display.
     *
     * @param index Index of the task in the list (1-based).
     * @return Formatted task string.
     */
    public String formatForList(int index) {
        return index + ".[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getDetails();
    }

    /**
     * Returns the formatted representation of this task without index.
     *
     * @return Formatted task string.
     */
    public String formatStatusLine() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getDetails();
    }
}
