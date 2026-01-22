/**
 * Represents an event task that occurs over a specific time period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task with a start and end time.
     *
     * @param description Description of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
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
}
