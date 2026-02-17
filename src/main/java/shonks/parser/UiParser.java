package shonks.parser;

import shonks.ShonksException;
import shonks.command.Command;

/**
 * Wrapper parser for UI-driven parsing.
 * Delegates parsing to {@link Parser}.
 */
public class UiParser {

    /**
     * Parses raw input into a {@link Command}.
     *
     * @param input The raw user input.
     * @return The parsed command.
     * @throws ShonksException If parsing fails.
     */
    public Command parse(String input) throws ShonksException {
        return Parser.parse(input);
    }
}
