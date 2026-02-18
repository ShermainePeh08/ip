package shonks.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import shonks.ShonksException;
import shonks.command.handlers.ArchiveHandler;
import shonks.command.handlers.StatsHandler;
import shonks.storage.Storage;
import shonks.task.Deadline;
import shonks.task.Event;
import shonks.task.TaskList;
import shonks.task.Todo;
import shonks.ui.StringUi;
import shonks.ui.Ui;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ArchiveAndStatsTest {

    private static final String DATA_PATH = "./data/test-shonks.txt";
    private static final String ARCHIVE_PATH = getAppPath("shonks-archive.txt");

    @AfterEach
    public void cleanupFiles() {
        new File(DATA_PATH).delete();
        new File(ARCHIVE_PATH).delete();
    }

    @Test
    public void stats_printsSummary() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));
        tasks.add(new Deadline("b", LocalDate.of(2026, 3, 15)));
        tasks.add(new Event("c",
                LocalDateTime.of(2026, 3, 12, 14, 0),
                LocalDateTime.of(2026, 3, 12, 16, 0)));

        StringBuilder out = new StringBuilder();
        ExitStatus exitStatus = new ExitStatus();
        Ui ui = new StringUi(out, exitStatus);

        ShonksContext context = new ShonksContext(new Storage(DATA_PATH), tasks, ui, exitStatus);

        new StatsHandler().handle(Command.stats(), context);

        String s = out.toString();
        assertTrue(s.contains("stats"));
        assertTrue(s.contains("total: 3"));
        assertTrue(s.contains("types: todo=1, deadline=1, event=1"));
    }

    @Test
    public void archive_oneTask_removesFromList() throws ShonksException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));
        tasks.add(new Deadline("b", LocalDate.of(2026, 3, 15)));

        StringBuilder out = new StringBuilder();
        ExitStatus exitStatus = new ExitStatus();
        Ui ui = new StringUi(out, exitStatus);

        ShonksContext context = new ShonksContext(new Storage(DATA_PATH), tasks, ui, exitStatus);

        Command archiveSecond = Command.archive(2);
        new ArchiveHandler().handle(archiveSecond, context);

        assertEquals(1, tasks.size());
        assertTrue(out.toString().contains("Archived task"));
    }

    @Test
    public void archive_allTasks_clearsList() throws ShonksException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));
        tasks.add(new Todo("b"));

        StringBuilder out = new StringBuilder();
        ExitStatus exitStatus = new ExitStatus();
        Ui ui = new StringUi(out, exitStatus);

        ShonksContext context = new ShonksContext(new Storage(DATA_PATH), tasks, ui, exitStatus);

        Command archiveAll = Command.archive(null);
        new ArchiveHandler().handle(archiveAll, context);

        assertEquals(0, tasks.size());
        assertTrue(out.toString().contains("Your list is now empty"));
    }
}
