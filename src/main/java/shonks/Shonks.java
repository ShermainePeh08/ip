package shonks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import shonks.command.Command;
import shonks.parser.Parser;
import shonks.storage.Storage;
import shonks.task.Deadline;
import shonks.task.Event;
import shonks.task.Task;
import shonks.task.TaskList;
import shonks.task.Todo;
import shonks.ui.Ui;

public class Shonks {
    /**
     * The main application class for Shonks.
     * <p>
     * This class coordinates user input parsing, command execution, task list
     * management, and storage operations. It serves as the central controller
     * for the application and integrates extension features such as task
     * archiving and statistics reporting.
     */
    private static final String ARCHIVE_PATH = "./data/shonks-archive.txt";

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

                if (command.type == Command.Type.STATS) {
                    ui.showLine(formatStats(taskList));
                    continue;
                }

                if (command.type == Command.Type.ARCHIVE) {
                    int archivedCount = taskList.size();
                    storage.archiveTo(ARCHIVE_PATH, taskList.getInternalList());
                    taskList.clear();
                    storage.save(taskList.getInternalList());
                    ui.showLine("🗄 Archived " + archivedCount + " task(s) to " + ARCHIVE_PATH
                            + ". Your list is now empty.");
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
        case STATS:
            return formatStats(taskList);
        case ARCHIVE:
            return archiveAndFormat(command.index);
        default:
            return "i don't understand that command.";
        }
    }

    /**
     * Archives all tasks if {@code oneBasedIndex} is null, otherwise archives only the specified task.
     *
     * @param oneBasedIndex 1-based task number to archive, or null to archive all tasks.
     * @return User-facing message describing the archive result.
     * @throws ShonksException If an invalid index is given or I/O fails.
     */
    private String archiveAndFormat(Integer oneBasedIndex) throws ShonksException {
        if (oneBasedIndex == null) {
            int archivedCount = taskList.size();
            storage.archiveTo(ARCHIVE_PATH, taskList.getInternalList());
            taskList.clear();
            storage.save(taskList.getInternalList());
            return "🗄 archived " + archivedCount + " task(s) to " + ARCHIVE_PATH
                    + ". your list is now empty.";
        }

        Task task = getTaskOrThrow(taskList, oneBasedIndex);

        java.util.ArrayList<Task> one = new java.util.ArrayList<>();
        one.add(task);

        storage.archiveTo(ARCHIVE_PATH, one);
        removeTaskOrThrow(taskList, oneBasedIndex);
        storage.save(taskList.getInternalList());

        return "🗄 archived task:\n  " + task + "\nnow you have " + taskList.size() + " tasks in the list.";
    }

    /**
     * Computes and formats summary statistics about the current in-memory task list.
     * This includes total tasks, completed vs pending counts, and a breakdown by task type.
     *
     * @param taskList The current task list.
     * @return A user-facing multi-line string containing statistics.
     */
    private static String formatStats(TaskList taskList) {
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
        List<Task> matches = taskList.getInternalList().stream()
            .filter(task -> task.contains(keyword))
            .collect(Collectors.toList());

        if (matches.isEmpty()) {
            return "no matching tasks found.";
        }

        StringBuilder sb = new StringBuilder("matching tasks:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(matches.get(i).formatForList(i + 1)).append("\n");
        }
        return sb.toString().trim();
    }
}
