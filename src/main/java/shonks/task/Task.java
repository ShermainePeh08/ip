package shonks.task;
import java.time.LocalDate;
import java.time.LocalDateTime;

import shonks.ShonksException;

/**
 * Represents a generic task with a description and completion status.
 * <p>
 * This class serves as the base class for all task types in Shonks.
 */
public class Task {
    protected final String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void unmarkDone() {
        this.isDone = false;
    }

    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    protected String getTypeIcon() {
        return " ";
    }

    protected String getDetails() {
        return "";
    }

    public String formatForList(int index) {
        return index + ".[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getDetails();
    }

    public String formatStatusLine() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getDetails();
    }

    // =======================
    // Level-7/8 Save/Load parts
    // =======================

    /**
     * Converts this task into a single-line storage format.
     *
     * Format:
     * TYPE | 0/1 | description | (extra...)
     */
    public String toStorageString() {
        return getStorageType() + " | " + (isDone ? "1" : "0") + " | " + description + getStorageExtra();
    }

    /**
     * Returns the task type code for saving (T/D/E).
     * Subclasses override.
     */
    protected String getStorageType() {
        return "T"; // default treat as Todo
    }

    /**
     * Returns extra fields for saving (e.g. " | by", " | from | to").
     * Subclasses override.
     */
    protected String getStorageExtra() {
        return "";
    }

    /**
     * Creates a Task object from one line in the save file.
     */
    public static Task fromStorageString(String line) throws ShonksException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new ShonksException("Corrupted save file line: " + line);
        }

        String type = parts[0];
        boolean done = parts[1].equals("1");
        String desc = parts[2];

        Task t;
        if (type.equals("T")) {
            t = new Todo(desc);
        } else if (type.equals("D")) {
            if (parts.length < 4) {
                throw new ShonksException("Corrupted deadline line: " + line);
            }
            LocalDate by = LocalDate.parse(parts[3]); // yyyy-MM-dd
            t = new Deadline(desc, by);
        } else if (type.equals("E")) {
            if (parts.length < 5) {
                throw new ShonksException("Corrupted event line: " + line);
            }
            LocalDateTime from = LocalDateTime.parse(parts[3]); // ISO-8601
            LocalDateTime to = LocalDateTime.parse(parts[4]);   // ISO-8601
            t = new Event(desc, from, to);
        } else {
            throw new ShonksException("Unknown task type in save file: " + type);
        }

        if (done) {
            t.markDone();
        }
        return t;
    }
}