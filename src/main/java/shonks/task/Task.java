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

    /**
    * Returns the formatted representation of this task for list display.
    *
    * @param index The 1-based index shown to the user.
    * @return A user-facing string representing the task.
    */
    public String formatForList(int index) {
        return index + ".[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getDetails();
    }

    /**
    * Returns the formatted representation of this task without list index.
    *
    * @return A user-facing string representing the task.
    */
    public String formatStatusLine() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getDetails();
    }

    /**
     * Checks if this task's description contains the keyword (case-insensitive).
     *
     * @param keyword Keyword to search for.
     * @return True if the description contains the keyword.
     */
    public boolean contains(String keyword) {
        return description.toLowerCase().contains(keyword.toLowerCase());
    }

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

        Task task;
        if (type.equals("T")) {
            task = new Todo(desc);
        } else if (type.equals("D")) {
            if (parts.length < 4) {
                throw new ShonksException("Corrupted deadline line: " + line);
            }
            LocalDate by = LocalDate.parse(parts[3]); // yyyy-MM-dd
            task = new Deadline(desc, by);
        } else if (type.equals("E")) {
            if (parts.length < 5) {
                throw new ShonksException("Corrupted event line: " + line);
            }
            LocalDateTime from = LocalDateTime.parse(parts[3]); // ISO-8601
            LocalDateTime to = LocalDateTime.parse(parts[4]);   // ISO-8601
            task = new Event(desc, from, to);
        } else {
            throw new ShonksException("Unknown Task taskype in save file: " + type);
        }

        if (done) {
            task.markDone();
        }
        return task;
    }
}