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
        // override by subclasses
        return " "; 
    }

    protected String getDetails() {
        // override by subclasses
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
}
