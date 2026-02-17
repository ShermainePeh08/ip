package shonks.command.handlers;

import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;

/**
 * Handles listing all tasks.
 */
public class ListHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) {
        context.ui().showListHeader();
        for (int i = 0; i < context.tasks().size(); i++) {
            context.ui().showLine(context.tasks().get(i).formatForList(i + 1));
        }
    }
}
