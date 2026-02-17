package shonks.command.handlers;

import shonks.ShonksException;
import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Task;

/**
 * Handles marking a task as done.
 */
public class MarkHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) throws ShonksException {
        Task task = HandlerUtil.getTaskOrThrow(context, command.index);
        task.markDone();
        HandlerUtil.save(context);
        context.ui().showMarked(task);
    }
}
