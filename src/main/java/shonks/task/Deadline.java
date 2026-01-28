package shonks.task;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed before a specified deadline.
 */
public class Deadline extends Task {
    private final LocalDate by;

    /**
     * Creates a deadline task with a description and deadline date.
     *
     * @param description Description of the task.
     * @param by Deadline date (LocalDate).
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    protected String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return " (by: " + by.format(formatter) + ")";
    }

    @Override
    protected String getStorageType() {
        return "D";
    }

    @Override
    protected String getStorageExtra() {
        return " | " + by.toString(); // yyyy-MM-dd
    }
}