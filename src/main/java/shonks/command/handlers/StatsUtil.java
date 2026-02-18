package shonks.command.handlers;

import shonks.task.Deadline;
import shonks.task.Event;
import shonks.task.Task;
import shonks.task.TaskList;
import shonks.task.Todo;

/**
 * Formats statistics about the current task list.
 */
public class StatsUtil {

    /**
     * Simple immutable stats container.
     */
    public static class Summary {
        public final int total;
        public final int done;
        public final int pending;
        public final int todo;
        public final int deadline;
        public final int event;

        public Summary(int total, int done, int pending, int todo, int deadline, int event) {
            this.total = total;
            this.done = done;
            this.pending = pending;
            this.todo = todo;
            this.deadline = deadline;
            this.event = event;
        }
    }

    /**
     * Computes summary statistics for the given task list.
     *
     * @param taskList The task list.
     * @return Summary stats.
     */
    public static Summary compute(TaskList taskList) {
        int total = taskList.size();
        int done = 0;
        int todo = 0;
        int deadline = 0;
        int event = 0;

        for (Task t : taskList.getInternalList()) {
            if (t.isDone()) {
                done++;
            }
            if (t instanceof Todo) {
                todo++;
            } else if (t instanceof Deadline) {
                deadline++;
            } else if (t instanceof Event) {
                event++;
            }
        }

        int pending = total - done;
        return new Summary(total, done, pending, todo, deadline, event);
    }

    /**
     * Formats summary statistics for the given task list.
     *
     * @param taskList The task list.
     * @return A formatted statistics string.
     */
    public static String format(TaskList taskList) {
        return format(compute(taskList));
    }

    /**
     * Formats summary statistics from a precomputed Summary.
     *
     * @param s Summary stats.
     * @return formatted string.
     */
    public static String format(Summary s) {
        return "📊 stats\n"
                + "total: " + s.total + "\n"
                + "done: " + s.done + "\n"
                + "pending: " + s.pending + "\n"
                + "types: todo=" + s.todo + ", deadline=" + s.deadline + ", event=" + s.event;
    }
}
