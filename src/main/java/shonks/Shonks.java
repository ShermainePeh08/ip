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

    private final Storage storage;
    private final TaskList taskList;

    public Shonks(String filePath) {
        this.storage = new Storage(filePath);

        TaskList loadedList;
        try {
            ArrayList<Task> loaded = storage.load();
            loadedList = new TaskList(loaded);
        } catch (ShonksException e) {
            loadedList = new TaskList();
        }
        this.taskList = loadedList;
    }

    private void save() throws ShonksException {
        storage.save(taskList.getInternalList());
    }

    private static Task getTaskOrThrow(TaskList taskList, int oneBasedIndex) throws ShonksException {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= taskList.size()) {
            throw new ShonksException("That task number does not exist.");
        }
        return taskList.get(i);
    }

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

    public String getResponse(String input) {
        try {
            return respondTo(Parser.parse(input));
        } catch (ShonksException e) {
            return e.getMessage();
        }
    }

    private String respondTo(Command command) throws ShonksException {
        switch (command.type) {
        case EXIT:
            return "bye. hope to see you again soon!";
        case LIST:
            return formatListResponse();
        case MARK:
            return markAndFormat(command.index);
        case UNMARK:
            return unmarkAndFormat(command.index);
        case DELETE:
            return deleteAndFormat(command.index);
        case TODO:
            return addAndFormat(new Todo(command.description));
        case DEADLINE:
            return addAndFormat(new Deadline(command.description, command.by));
        case EVENT:
            return addAndFormat(new Event(command.description, command.from, command.to));
        case FIND:
            return formatFindResponse(command.keyword);
        default:
            return "i don't understand that command.";
        }
    }

    private String formatListResponse() {
        if (taskList.size() == 0) {
            return "your list is empty.";
        }
        StringBuilder sb = new StringBuilder("here are the tasks in your list:\n");
        for (int i = 0; i < taskList.size(); i++) {
            sb.append(taskList.get(i).formatForList(i + 1)).append("\n");
        }
        return sb.toString().trim();
    }

    private String markAndFormat(int oneBasedIndex) throws ShonksException {
        Task task = getTaskOrThrow(taskList, oneBasedIndex);
        task.markDone();
        save();
        return "nice! i've marked this task as done:\n  " + task;
    }

    private String unmarkAndFormat(int oneBasedIndex) throws ShonksException {
        Task task = getTaskOrThrow(taskList, oneBasedIndex);
        task.unmarkDone();
        save();
        return "ok! i've marked this task as not done yet:\n  " + task;
    }

    private String deleteAndFormat(int oneBasedIndex) throws ShonksException {
        Task removed = removeTaskOrThrow(taskList, oneBasedIndex);
        save();
        return "noted. i've removed this task:\n  " + removed
                + "\nnow you have " + taskList.size() + " tasks in the list.";
    }

    private String addAndFormat(Task task) throws ShonksException {
        taskList.add(task);
        save();
        return "got it. i've added this task:\n  " + task
                + "\nnow you have " + taskList.size() + " tasks in the list.";
    }

    private String formatFindResponse(String keyword) {
        StringBuilder sb = new StringBuilder("matching tasks:\n");
        int shown = 0;
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.contains(keyword)) {
                shown++;
                sb.append(task.formatForList(shown)).append("\n");
            }
        }

        if (shown == 0) {
            return "no matching tasks found.";
        }
        return sb.toString().trim();
    }
}
