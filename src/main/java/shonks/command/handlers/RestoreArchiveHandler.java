package shonks.command.handlers;

import java.io.File;
import java.util.ArrayList;

import shonks.ShonksException;
import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Task;

/**
 * Handles restoring tasks from the archive back into the main task list.
 *
 * Supported:
 * - restore            -> restores all archived tasks
 * - restore <n>        -> restores the n-th archived task (1-based)
 * - unarchive ...      -> alias of restore
 */
public class RestoreArchiveHandler implements CommandHandler {

    private static final String ARCHIVE_PATH = getAppPath("shonks-archive.txt");

    private static String getAppPath(String filename) {
        String home = System.getProperty("user.home");
        return home + File.separator + ".shonks" + File.separator + filename;
    }

    @Override
    public void handle(Command command, ShonksContext context) throws ShonksException {
        ArrayList<Task> archived = context.storage().loadFromPath(ARCHIVE_PATH);

        if (archived.isEmpty()) {
            context.ui().showLine("Nothing in the archive to restore. Go archive something first.");
            return;
        }

        if (command.index == null) {
            restoreAll(context, archived);
            return;
        }

        restoreOne(context, archived, command.index);
    }

    private void restoreAll(ShonksContext context, ArrayList<Task> archived) throws ShonksException {
        for (Task t : archived) {
            context.tasks().add(t);
        }

        context.storage().overwriteTo(ARCHIVE_PATH, new ArrayList<>());
        HandlerUtil.save(context);

        context.ui().showLine("🗃 Restored " + archived.size()
                + " task(s) from archive. They are back. Regrettably.");
    }

    private void restoreOne(ShonksContext context,
                            ArrayList<Task> archived,
                            int oneBasedArchivedIndex) throws ShonksException {
        int i = oneBasedArchivedIndex - 1;
        if (i < 0 || i >= archived.size()) {
            throw new ShonksException("That archived task number does not exist.");
        }

        Task restored = archived.remove(i);
        context.tasks().add(restored);

        context.storage().overwriteTo(ARCHIVE_PATH, archived);
        HandlerUtil.save(context);

        context.ui().showLine("🗃 Restored archived task:\n  "
                + restored.formatStatusLine()
                + "\nNow you have " + context.tasks().size() + " tasks in the list.");
    }
}
