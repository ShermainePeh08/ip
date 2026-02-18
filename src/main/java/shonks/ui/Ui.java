package shonks.ui;

import java.util.Scanner;

import shonks.task.Task;

/**
 * Handles all user-facing text output and input prompting.
 */
public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        renderLine("Oh. You are back.");
        renderLine("What do you want this time?");
    }

    public void showBye() {
        renderLine("Finally. Goodbye.");
        renderLine("Do not break anything while I am gone.");
    }

    public String readCommand() {
        assert scanner != null : "Scanner should be initialised";
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        assert message != null : "Error message should not be null";
        renderLine("Congratulations. You caused this:");
        renderLine(message);
    }

    public void showListHeader() {
        renderLine("Here is your list. Try not to panic.");
    }

    public void showLine(String line) {
        assert line != null : "Line to print should not be null";
        renderLine(line);
    }

    public void showAdded(Task task, int size) {
        assert task != null : "Task should not be null";
        assert size >= 0 : "Task list size should be non-negative";

        renderLine("Fine. I added it.");
        renderLine("  " + task.formatStatusLine());
        renderLine("You now have " + size + " tasks. Manage them responsibly.");
    }

    public void showDeleted(Task task, int size) {
        assert task != null : "Task should not be null";
        assert size >= 0 : "Task list size should be non-negative";

        renderLine("It is gone. Happy now?");
        renderLine("  " + task.formatStatusLine());
        renderLine("You now have " + size + " tasks left. Try not to delete everything.");
    }

    public void showMarked(Task task) {
        assert task != null : "Task should not be null";

        renderLine("Wow. Productivity.");
        renderLine("  " + task.formatStatusLine());
    }

    public void showUnmarked(Task task) {
        assert task != null : "Task should not be null";

        renderLine("Ah yes. Back to unfinished tasks.");
        renderLine("  " + task.formatStatusLine());
    }

    public void showFindHeader() {
        renderLine("Here are the matching tasks. Assuming you typed correctly.");
    }

    public void showNoFindMatches() {
        renderLine("No matches found. Perhaps spell properly next time.");
    }

    public void showPieChart(String title, int todo, int deadline, int event) {
    }

    /**
     * Renders a single line of UI output.
     * Subclasses may override this to redirect output (e.g., GUI buffers).
     *
     * @param line Line to render.
     */
    protected void renderLine(String line) {
        System.out.println(line);
    }
}
