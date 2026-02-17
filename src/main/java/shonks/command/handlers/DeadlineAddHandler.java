package shonks.command.handlers;

import shonks.ShonksException;
import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Deadline;

/**
 * Handles adding a deadline task.
 */
public class DeadlineAddHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) throws ShonksException {
        HandlerUtil.addTask(context, new Deadline(command.description, command.by));
    }
}
