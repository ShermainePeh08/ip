package shonks.command.handlers;

import shonks.ShonksException;
import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Todo;

/**
 * Handles adding a todo task.
 */
public class TodoAddHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) throws ShonksException {
        HandlerUtil.addTask(context, new Todo(command.description));
    }
}
