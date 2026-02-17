package shonks.command.handlers;

import shonks.ShonksException;
import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Event;

/**
 * Handles adding an event task.
 */
public class EventAddHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) throws ShonksException {
        HandlerUtil.addTask(context, new Event(command.description, command.from, command.to));
    }
}
