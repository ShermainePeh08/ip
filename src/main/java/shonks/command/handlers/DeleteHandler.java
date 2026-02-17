package shonks.command.handlers;

import shonks.ShonksException;
import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Task;

/**
 * Handles deleting a task.
 */
public class DeleteHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) throws ShonksException {
        Task removed = HandlerUtil.removeTaskOrThrow(context, command.index);
        HandlerUtil.save(context);
        context.ui().showDeleted(removed, context.tasks().size());
    }
}
