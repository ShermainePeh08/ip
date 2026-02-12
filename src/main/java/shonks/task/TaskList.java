package shonks.task;
import java.util.ArrayList;

/**
 * Wraps the task collection and provides operations to add, delete,
 * and access tasks in a single place.
 */
public class TaskList {
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
}
