package shonks.command.handlers;

import java.io.File;
import java.util.ArrayList;

import shonks.ShonksException;
import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Task;

/**
 * Handles archiving tasks.
 */
public class ArchiveHandler implements CommandHandler {


    private static final String ARCHIVE_PATH = getAppPath("shonks-archive.txt");

    private static String getAppPath(String filename) {
        String home = System.getProperty("user.home");
        return home + File.separator + ".shonks" + File.separator + filename;
    }

    @Override
    public void handle(Command command, ShonksContext context) throws ShonksException {
        if (context.tasks().size() == 0) {
            context.ui().showLine("Archive what?! There is nothing to archive. Zero. None.");
            return;
        }
        if (command.index == null) {
            int archivedCount = context.tasks().size();
            context.storage().archiveTo(ARCHIVE_PATH, context.tasks().getInternalList());
            context.tasks().clear();
            HandlerUtil.save(context);
            context.ui().showLine("🗄 Archived " + archivedCount + " task(s). Find them in the bin next time."
                    + " Your list is now empty.");
            return;
        }

        Task task = HandlerUtil.getTaskOrThrow(context, command.index);
        ArrayList<Task> one = new ArrayList<>();
        one.add(task);

        context.storage().archiveTo(ARCHIVE_PATH, one);
        HandlerUtil.removeTaskOrThrow(context, command.index);
        HandlerUtil.save(context);

        context.ui().showLine("🗄 Archived task:\n  " + task.formatStatusLine()
                + "\nNow you have " + context.tasks().size() + " tasks in the list.");
    }
    
}
