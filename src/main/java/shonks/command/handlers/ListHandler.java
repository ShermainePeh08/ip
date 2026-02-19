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

        if (context.tasks().size() == 0) {
            context.ui().showEmptyList();
            return;
        }
        for (int i = 0; i < context.tasks().size(); i++) {
            context.ui().showListHeader();
            context.ui().showLine(context.tasks().get(i).formatForList(i + 1));
        }
    }
}
