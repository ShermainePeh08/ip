package shonks;

import java.util.ArrayList;

import shonks.command.Command;
import shonks.command.CommandInvoker;
import shonks.command.ExitStatus;
import shonks.command.ShonksContext;
import shonks.parser.UiParser;
import shonks.storage.Storage;
import shonks.task.Task;
import shonks.task.TaskList;
import shonks.ui.StringUi;
import shonks.ui.Ui;

/**
 * Main class for the Shonks application.
 * Handles initialization and the main application loop.
 * Initializes the parser, storage, task list, and command invoker.
 */
public class Shonks {

    private static final String DATA_FILE_PATH = getAppPath("shonks.txt");

    private final Ui ui;
    private final TaskList tasks;
    private final UiParser parser;
    private final Storage storage;
    private final CommandInvoker commandInvoker;

    private static String getAppPath(String filename) {
        String home = System.getProperty("user.home");
        return home + "/.shonks/" + filename;
    }


    /**
     * Constructs a Shonks application instance.
     * Initializes the parser, storage, task list, and command invoker.
     */
    public Shonks() {
        this.ui = new Ui();
        this.storage = new Storage(DATA_FILE_PATH);
        this.tasks = loadTasks(storage);
        this.parser = new UiParser();
        this.commandInvoker = new CommandInvoker();
    }

    /**
     * Gets responses for GUI integration.
     *
     * @param input The user input command.
     * @return The response from executing the command.
     */
    public String getResponse(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        try {
            Command cmd = parser.parse(input);
            StringBuilder output = new StringBuilder();

            ExitStatus exitStatus = new ExitStatus();
            Ui tempUi = new StringUi(output, exitStatus);

            ShonksContext context = new ShonksContext(storage, tasks, tempUi, exitStatus);
            commandInvoker.execute(cmd, context);

            return output.toString().trim();
        } catch (ShonksException e) {
            return e.getMessage();
        }
    }

    /**
     * Runs the main application loop.
     * Reads user input, parses commands, and executes them until exit.
     */
    public void run() {
        ui.showWelcome();

        boolean isRunning = true;
        while (isRunning) {
            try {
                Command cmd = parser.parse(ui.readCommand());
                ExitStatus exitStatus = new ExitStatus();
                ShonksContext context = new ShonksContext(storage, tasks, ui, exitStatus);

                commandInvoker.execute(cmd, context);

                isRunning = !exitStatus.isExitRequested();
            } catch (ShonksException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static TaskList loadTasks(Storage storage) {
        try {
            ArrayList<Task> loaded = storage.load();
            return new TaskList(loaded);
        } catch (ShonksException e) {
            return new TaskList();
        }
    }

    /**
     * Runs the Shonks application. This is the entry point of the CLI program.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Shonks().run();
    }
}
