package shonks.ui;
import java.util.Scanner;

import shonks.task.Task;

/**
 * Handles all user-facing text output and input prompting.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        System.out.println("Hello! I'm Shonks");
        System.out.println("What can I do for you?");
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        System.out.println("Oops! " + message);
    }

    public void showListHeader() {
        System.out.println("Here are the tasks in your list:");
    }

    public void showLine(String line) {
        System.out.println(line);
    }

    public void showAdded(Task task, int size) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task.formatStatusLine());
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void showDeleted(Task task, int size) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task.formatStatusLine());
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void showMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task.formatStatusLine());
    }

    public void showUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task.formatStatusLine());
    }

    public void showFindHeader() {
        System.out.println("Here are the matching tasks in your list:");
    }

    public void showNoFindMatches() {
        System.out.println("No matching tasks found.");
    }
}