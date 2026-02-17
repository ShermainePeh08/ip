package shonks.command.handlers;

import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;

/**
 * Handles showing task statistics.
 */
public class StatsHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) {
        context.ui().showLine(StatsUtil.format(context.tasks()));
    }
}
