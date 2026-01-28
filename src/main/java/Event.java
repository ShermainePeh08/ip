import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that occurs over a specific time period.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event task with a start and end time.
     *
     * @param description Description of the event.
     * @param from Start datetime (LocalDateTime).
     * @param to End datetime (LocalDateTime).
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected String getTypeIcon() {
        return "E";
    }

    @Override
    protected String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
        return " (from: " + from.format(formatter) + " to: " + to.format(formatter) + ")";
    }

    @Override
    protected String getStorageType() {
        return "E";
    }

    @Override
    protected String getStorageExtra() {
        return " | " + from.toString() + " | " + to.toString(); // ISO-8601
    }
}