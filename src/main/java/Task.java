public class Task {
    private final String description;
    private boolean isDone;

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

    public String formatForList(int index) {
        String status = isDone ? "X" : " ";
        return index + ".[" + status + "] " + description;
    }

    public String formatStatusLine() {
        String status = isDone ? "X" : " ";
        return "[" + status + "] " + description;
    }
}
