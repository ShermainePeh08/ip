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
     * Runs the chatbot loop until the user exits.
     *
     * @param args Command-line arguments (unused).
     */
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
                    Task t = taskList.get(command.index - 1);
                    t.markDone();
                    storage.save(taskList.getInternalList());
                    ui.showMarked(t);
                    continue;
                }

                if (command.type == Command.Type.UNMARK) {
                    Task t = taskList.get(command.index - 1);
                    t.unmarkDone();
                    storage.save(taskList.getInternalList());
                    ui.showUnmarked(t);
                    continue;
                }

                if (command.type == Command.Type.DELETE) {
                    Task removed = taskList.remove(command.index - 1);
                    storage.save(taskList.getInternalList());
                    ui.showDeleted(removed, taskList.size());
                    continue;
                }

                if (command.type == Command.Type.TODO) {
                    Task t = new Todo(command.description);
                    taskList.add(t);
                    storage.save(taskList.getInternalList());
                    ui.showAdded(t, taskList.size());
                    continue;
                }

                if (command.type == Command.Type.DEADLINE) {
                    Task t = new Deadline(command.description, command.by);
                    taskList.add(t);
                    storage.save(taskList.getInternalList());
                    ui.showAdded(t, taskList.size());
                    continue;
                }

                if (command.type == Command.Type.EVENT) {
                    Task t = new Event(command.description, command.from, command.to);
                    taskList.add(t);
                    storage.save(taskList.getInternalList());
                    ui.showAdded(t, taskList.size());
                }

            } catch (ShonksException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showGenericError();
            }
        }
    }
}