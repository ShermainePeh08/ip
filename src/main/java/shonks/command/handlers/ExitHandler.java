package shonks.command.handlers;

import shonks.command.Command;
import shonks.command.CommandHandler;
import shonks.command.ShonksContext;

/**
 * Handles the exit command.
 */
public class ExitHandler implements CommandHandler {

    @Override
    public void handle(Command command, ShonksContext context) {
        context.ui().showBye();
        context.exitStatus().requestExit();
    }
}
