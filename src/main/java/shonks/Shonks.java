package shonks;

import java.util.ArrayList;

import shonks.command.Command;
import shonks.parser.Parser;
import shonks.storage.Storage;
import shonks.task.Deadline;
import shonks.task.Event;
import shonks.task.Task;
import shonks.task.TaskList;
import shonks.task.Todo;
import shonks.ui.Ui;

/**
 * Entry point of the Shonks chatbot application.
 * Reads user commands, updates the task list, and prints responses.
 */
public class Shonks {

    /**
     * Returns the task at the given 1-based index, or throws a ShonksException if invalid.
     *
     * @param taskList The task list.
     * @param oneBasedIndex 1-based task number.
     * @return The task at that index.
     * @throws ShonksException If the index is out of range.
     */
    private static Task getTaskOrThrow(TaskList taskList, int oneBasedIndex) throws ShonksException {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= taskList.size()) {
            throw new ShonksException("That task number does not exist.");
        }
        return taskList.get(i);
    }

    /**
     * Removes the task at the given 1-based index, or throws a ShonksException if invalid.
     *
     * @param taskList The task list.
     * @param oneBasedIndex 1-based task number.
     * @return The removed task.
     * @throws ShonksException If the index is out of range.
     */
    private static Task removeTaskOrThrow(TaskList taskList, int oneBasedIndex) throws ShonksException {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= taskList.size()) {
            throw new ShonksException("That task number does not exist.");
        }
        return taskList.remove(i);
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("./data/shonks.txt");

        TaskList taskList;
        try {
            ArrayList<Task> loaded = storage.load();
            taskList = new TaskList(loaded);
        } catch (ShonksException e) {
            taskList = new TaskList();
            ui.showError(e.getMessage());
        }

        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand();
                Command command = Parser.parse(input);

                if (command.type == Command.Type.EXIT) {
                    ui.showBye();
                    break;
                }

                if (command.type == Command.Type.LIST) {
                    ui.showListHeader();
                    for (int i = 0; i < taskList.size(); i++) {
                        ui.showLine(taskList.get(i).formatForList(i + 1));
                    }
                    continue;
                }

                if (command.type == Command.Type.MARK) {
                    Task task = getTaskOrThrow(taskList, command.index);
                    task.markDone();
                    storage.save(taskList.getInternalList());
                    ui.showMarked(task);
                    continue;
                }

                if (command.type == Command.Type.UNMARK) {
                    Task task = getTaskOrThrow(taskList, command.index);
                    task.unmarkDone();
                    storage.save(taskList.getInternalList());
                    ui.showUnmarked(task);
                    continue;
                }

                if (command.type == Command.Type.DELETE) {
                    Task removed = removeTaskOrThrow(taskList, command.index);
                    storage.save(taskList.getInternalList());
                    ui.showDeleted(removed, taskList.size());
                    continue;
                }

                if (command.type == Command.Type.TODO) {
                    Task task = new Todo(command.description);
                    taskList.add(task);
                    storage.save(taskList.getInternalList());
                    ui.showAdded(task, taskList.size());
                    continue;
                }

                if (command.type == Command.Type.DEADLINE) {
                    Task task = new Deadline(command.description, command.by);
                    taskList.add(task);
                    storage.save(taskList.getInternalList());
                    ui.showAdded(task, taskList.size());
                    continue;
                }

                if (command.type == Command.Type.EVENT) {
                    Task task = new Event(command.description, command.from, command.to);
                    taskList.add(task);
                    storage.save(taskList.getInternalList());
                    ui.showAdded(task, taskList.size());
                    continue;
                }

                                if (command.type == Command.Type.FIND) {
                    ui.showFindHeader();

                    int shown = 0;
                    for (int i = 0; i < taskList.size(); i++) {
                        Task task = taskList.get(i);
                        if (task.contains(command.keyword)) {
                            shown++;
                            ui.showLine(task.formatForList(shown));
                        }
                    }

                    if (shown == 0) {
                        ui.showNoFindMatches();
                    }
                    continue;
                }

                throw new ShonksException("I don't understand that command.");

            } catch (ShonksException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}