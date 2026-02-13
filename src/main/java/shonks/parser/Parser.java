package shonks.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import shonks.ShonksException;
import shonks.command.Command;

public class Parser {
    /**
     * Parses raw user input strings into {@link shonks.command.Command} objects.
     * <p>
     * The parser is responsible for validating command keywords and arguments,
     * and for constructing the appropriate {@code Command} representation.
     * It supports both built-in task commands and extension commands such as
     * {@code archive} and {@code stats}.
     */
    private static final String DATE_FORMAT_MSG =
            "Please use date format yyyy-MM-dd (e.g., 2019-10-15).";
    private static final String DATETIME_FORMAT_MSG =
            "Please use datetime format yyyy-MM-ddTHH:mm (e.g., 2019-10-15T14:00).";

    /**
     * Parses a user input string into a {@link shonks.command.Command}.
     * The first token is treated as the command keyword, and the remainder as arguments.
     *
     * Supported keywords include built-in task commands (e.g., todo/deadline/event)
     * and extension commands such as {@code archive} and {@code stats}.
     *
     * @param input Raw user input.
     * @return Parsed {@code Command}.
     * @throws shonks.ShonksException If the command keyword is unknown or arguments are invalid.
     */
    public static Command parse(String input) throws ShonksException {
        assert input != null : "User input should not be null";

        String trimmed = requireNonBlank(input, "Please enter a command.");
        String[] parts = splitOnce(trimmed);

        String keyword = parts[0];
        String args = parts[1];

        switch (keyword) {
        case "bye":
            return Command.exit();
        case "list":
            return Command.list();
        case "mark":
            return parseMark(args);
        case "unmark":
            return parseUnmark(args);
        case "delete":
            return parseDelete(args);
        case "todo":
            return parseTodo(args);
        case "deadline":
            return parseDeadline(args);
        case "event":
            return parseEvent(args);
        case "find":
            return parseFind(args);
        case "archive":
        if (args.isEmpty()) {
            return Command.archive(null);
        }
        return Command.archive(parseTaskNumber(args, "Please specify which task to archive."));

        case "stats":
            return Command.stats();

        default:
            throw new ShonksException("I don't understand that command.");
        }
    }

    /* ---------- Command parsers ---------- */

    private static Command parseMark(String args) throws ShonksException {
        return Command.mark(parseTaskNumber(args, "Please specify which task number to mark."));
    }

    private static Command parseUnmark(String args) throws ShonksException {
        return Command.unmark(parseTaskNumber(args, "Please specify which task number to unmark."));
    }

    private static Command parseDelete(String args) throws ShonksException {
        return Command.delete(parseTaskNumber(args, "Please specify which task number to delete."));
    }

    private static Command parseTodo(String args) throws ShonksException {
        String description = requireNonBlank(args, "The description of a todo cannot be empty.");
        return Command.todo(description);
    }

    private static Command parseDeadline(String args) throws ShonksException {
        String[] parts = splitByKeyword(args, "/by",
                "Deadline must have /by <date>.");

        String description = requireNonBlank(parts[0],
                "The description of a deadline cannot be empty.");
        String byRaw = requireNonBlank(parts[1],
                "Deadline must have /by <date>.");

        try {
            return Command.deadline(description, LocalDate.parse(byRaw));
        } catch (DateTimeParseException e) {
            throw new ShonksException(DATE_FORMAT_MSG);
        }
    }

    private static Command parseEvent(String args) throws ShonksException {
        String[] parts = splitByKeywords(args,
                new String[]{"/from", "/to"},
                "Event must have /from <start> /to <end>.");

        String description = requireNonBlank(parts[0],
                "The description of an event cannot be empty.");

        try {
            LocalDateTime from = LocalDateTime.parse(parts[1]);
            LocalDateTime to = LocalDateTime.parse(parts[2]);
            return Command.event(description, from, to);
        } catch (DateTimeParseException e) {
            throw new ShonksException(DATETIME_FORMAT_MSG);
        }
    }

    private static Command parseFind(String args) throws ShonksException {
        String keyword = requireNonBlank(args,
                "Please provide a keyword to find (e.g., find book).");
        return Command.find(keyword);
    }

    /* ---------- Shared helpers ---------- */

    private static String[] splitOnce(String input) {
        String[] parts = input.split("\\s+", 2);
        String keyword = parts[0];
        String args = parts.length == 2 ? parts[1].trim() : "";
        return new String[]{keyword, args};
    }

    private static String requireNonBlank(String value, String message)
            throws ShonksException {
        if (value == null || value.trim().isEmpty()) {
            throw new ShonksException(message);
        }
        return value.trim();
    }

    private static int parseTaskNumber(String raw, String emptyMessage)
            throws ShonksException {
        String value = requireNonBlank(raw, emptyMessage);
        try {
            int num = Integer.parseInt(value);
            if (num <= 0) {
                throw new ShonksException("Task number must be a positive integer.");
            }
            return num;
        } catch (NumberFormatException e) {
            throw new ShonksException("Please enter a valid task number (e.g., mark 2).");
        }
    }

    private static String[] splitByKeyword(String args, String keyword, String errorMessage)
            throws ShonksException {
        String cleaned = requireNonBlank(args, errorMessage);
        String[] parts = cleaned.split("\\s+" + keyword + "\\s+", 2);
        if (parts.length < 2) {
            throw new ShonksException(errorMessage);
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    private static String[] splitByKeywords(String args, String[] keywords, String errorMessage)
            throws ShonksException {
        String cleaned = requireNonBlank(args, errorMessage);

        String regex = "\\s+" + keywords[0] + "\\s+|\\s+" + keywords[1] + "\\s+";
        String[] parts = cleaned.split(regex, 3);

        if (parts.length < 3) {
            throw new ShonksException(errorMessage);
        }

        return new String[]{
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        };
    }
}