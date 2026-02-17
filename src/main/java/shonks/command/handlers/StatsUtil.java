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
     * Formats summary statistics for the given task list.
     *
     * @param taskList The task list.
     * @return A formatted statistics string.
     */
    public static String format(TaskList taskList) {
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

        return "📊 stats\n"
                + "total: " + total + "\n"
                + "done: " + done + "\n"
                + "pending: " + pending + "\n"
                + "types: todo=" + todo + ", deadline=" + deadline + ", event=" + event;
    }
}
