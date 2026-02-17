package shonks.command;

import shonks.ShonksException;

/**
 * Executes a command using a {@link ShonksContext}.
 */
@FunctionalInterface
public interface CommandHandler {

    /**
     * Executes the command.
     *
     * @param command The command to execute.
     * @param context The execution context.
     * @throws ShonksException If execution fails.
     */
    void handle(Command command, ShonksContext context) throws ShonksException;
}
