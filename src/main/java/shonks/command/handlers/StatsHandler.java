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
        StatsUtil.Summary s = StatsUtil.compute(context.tasks());

        context.ui().showLine(StatsUtil.format(s));
        context.ui().showPieChart("Task Types", s.todo, s.deadline, s.event);
    }
}
