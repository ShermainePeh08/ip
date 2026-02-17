package shonks.ui;

import shonks.command.ExitStatus;
import shonks.task.Task;

/**
 * UI implementation that captures output into a {@link StringBuilder}.
 * Intended for GUI integration via {@code getResponse}.
 */
public class StringUi extends Ui {

    private final StringBuilder output;
    private final ExitStatus exitStatus;

    /**
     * Constructs a StringUi that writes UI outputs into a buffer.
     *
     * @param output The output buffer.
     * @param exitStatus Exit status tracker.
     */
    public StringUi(StringBuilder output, ExitStatus exitStatus) {
        this.output = output;
        this.exitStatus = exitStatus;
    }

    @Override
    public void showWelcome() {
        output.append("Hello! I'm Shonks\n");
        output.append("What can I do for you?\n");
    }

    @Override
    public void showBye() {
        output.append("Bye. Hope to see you again soon!\n");
        exitStatus.requestExit();
    }

    @Override
    public void showError(String message) {
        output.append("Oops! ").append(message).append("\n");
    }

    @Override
    public void showListHeader() {
        output.append("Here are the tasks in your list:\n");
    }

    @Override
    public void showLine(String line) {
        output.append(line).append("\n");
    }

    @Override
    public void showAdded(Task task, int size) {
        output.append("Got it. I've added this task:\n");
        output.append("  ").append(task.formatStatusLine()).append("\n");
        output.append("Now you have ").append(size).append(" tasks in the list.\n");
    }

    @Override
    public void showDeleted(Task task, int size) {
        output.append("Noted. I've removed this task:\n");
        output.append("  ").append(task.formatStatusLine()).append("\n");
        output.append("Now you have ").append(size).append(" tasks in the list.\n");
    }

    @Override
    public void showMarked(Task task) {
        output.append("Nice! I've marked this task as done:\n");
        output.append("  ").append(task.formatStatusLine()).append("\n");
    }

    @Override
    public void showUnmarked(Task task) {
        output.append("OK, I've marked this task as not done yet:\n");
        output.append("  ").append(task.formatStatusLine()).append("\n");
    }

    @Override
    public void showFindHeader() {
        output.append("Here are the matching tasks in your list:\n");
    }

    @Override
    public void showNoFindMatches() {
        output.append("No matching tasks found.\n");
    }
}
