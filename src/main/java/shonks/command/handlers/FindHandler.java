package shonks.command.handlers;

import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;
import shonks.task.Task;

/**
 * Handles finding tasks by keyword.
 */
public class FindHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) {

        int shown = 0;
        for (int i = 0; i < context.tasks().size(); i++) {
            Task task = context.tasks().get(i);
            if (task.contains(command.keyword)) {
                context.ui().showFindHeader();
                shown++;
                context.ui().showLine(task.formatForList(shown));
            }
        }

        if (shown == 0) {
            context.ui().showNoFindMatches();
        }
    }
}
