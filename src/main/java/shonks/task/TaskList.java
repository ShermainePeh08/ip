package shonks.task;
import java.util.ArrayList;

public class TaskList {
    /**
     * Represents a mutable list of tasks managed by Shonks.
     * <p>
     * This class provides operations for adding, removing, retrieving, and
     * clearing tasks. It is used as the in-memory representation of the user's
     * task list and supports extension features such as task archiving and
     * statistics generation.
     */
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Initial task list should not be null";
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds";
        return tasks.get(index);
    }

    public void add(Task task) {
        assert task != null : "Task to add should not be null";
        tasks.add(task);
    }

    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds";
        return tasks.remove(index);
    }

    public ArrayList<Task> getInternalList() {
        assert tasks != null : "Internal task list should not be null";
        return tasks;
    }

    /**
     * Removes all tasks from this task list.
     * After calling this, {@code size()} will return 0.
     */
    public void clear() {
        tasks.clear();
    }
}
