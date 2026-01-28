/**
 * Represents an event task that occurs over a specific time period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
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
        return " (from: " + from + " to: " + to + ")";
    }

    @Override
    protected String getStorageType() {
        return "E";
    }

    @Override
    protected String getStorageExtra() {
        return " | " + from + " | " + to;
    }
}