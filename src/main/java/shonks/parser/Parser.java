package shonks.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import shonks.ShonksException;
import shonks.command.Command;

/**
 * Parses raw user input strings into {@link Command} objects.
 * Follows Single Level of Abstraction per method (SLAP).
 */
public class Parser {

    private static final String DATE_FORMAT_MSG =
            "ThAt dAtE fOrMaT iS wRoNg. Use yyyy-MM-dd (e.g., 2019-10-15).";

    private static final String DATETIME_FORMAT_MSG =
            "Wow. That datetime is wrong. Use yyyy-MM-ddTHH:mm (e.g., 2019-10-15T14:00). It is not complicated.";

    /**
     * Parses user input into a {@link Command}.
     */
    public static Command parse(String input) throws ShonksException {
        assert input != null : "User input should not be null";
        Tokenized tokenized = parseInput(input);
        return dispatch(tokenized);
    }

    /**
     * Cleans and tokenizes user input into keyword and arguments.
     */
    private static Tokenized parseInput(String input) throws ShonksException {
        String cleaned = requireNonBlank(input,
                "Oh nice. A blank command. Very productive. Truly inspiring.").trim();

        String[] parts = cleaned.split("\\s+", 2);
        String keyword = parts[0].trim().toLowerCase();
        String args = (parts.length == 2) ? parts[1].trim() : "";

        return new Tokenized(keyword, args);
    }

    /**
     * Dispatches the tokenized input to the appropriate parser.
     */
    private static Command dispatch(Tokenized t) throws ShonksException {
        switch (t.keyword) {
        case "bye":
            return Command.exit();
        case "list":
            return Command.list();
        case "mark":
            return parseMark(t.args);
        case "unmark":
            return parseUnmark(t.args);
        case "delete":
            return parseDelete(t.args);
        case "todo":
            return parseTodo(t.args);
        case "deadline":
            return parseDeadline(t.args);
        case "event":
            return parseEvent(t.args);
        case "find":
            return parseFind(t.args);
        case "archive":
            return parseArchive(t.args);
        case "stats":
            return Command.stats();
        case "restore":
        case "unarchive":
            return parseRestoreArchive(t.args);
        default:
            throw new ShonksException("?? I do not understand that command. Try again but like correctly.");
        }
    }

    /**
     * Parses a mark command.
     */
    private static Command parseMark(String args) throws ShonksException {
        return Command.mark(parseTaskNumber(args,
                "mArK wHaT exactly? I do not read minds."));
    }

    /**
     * Parses an unmark command.
     */
    private static Command parseUnmark(String args) throws ShonksException {
        return Command.unmark(parseTaskNumber(args,
                "Unmark wHiCh task?? Use a number."));
    }

    /**
     * Parses a delete command.
     */
    private static Command parseDelete(String args) throws ShonksException {
        return Command.delete(parseTaskNumber(args,
                "Delete wHaT? Give a task number. Not rubbish."));
    }

    /**
     * Parses a todo command.
     */
    private static Command parseTodo(String args) throws ShonksException {
        return Command.todo(requireNonBlank(args,
                "A todo with no description? Bold strategy."));
    }

    /**
     * Parses a deadline command.
     */
    private static Command parseDeadline(String args) throws ShonksException {
        Parts p = splitOnMarker(args, "/by",
                "Deadline needs /by <date>. I am not guessing.");

        String description = requireNonBlank(p.before,
                "Deadline description is empty. That is impressive.");

        LocalDate by = parseDate(requireNonBlank(p.after,
                "You forgot the /by date. Truly astonishing."));

        return Command.deadline(description, by);
    }

    /**
     * Parses an event command.
     */
    private static Command parseEvent(String args) throws ShonksException {
        EventParts p = splitEventMarkers(args,
                "Event needs /from <start> /to <end>. Follow instructions.");

        String description = requireNonBlank(p.description,
                "Event description is empty. Are we improvising now?");

        LocalDateTime from = parseDateTime(requireNonBlank(p.from,
                "Missing /from time. Yes, that part matters."));

        LocalDateTime to = parseDateTime(requireNonBlank(p.to,
                "Missing /to time. This is not optional."));

        return Command.event(description, from, to);
    }

    /**
     * Parses a find command.
     */
    private static Command parseFind(String args) throws ShonksException {
        return Command.find(requireNonBlank(args,
                "Find wHaT?? A keyword would help."));
    }

    /**
     * Parses an archive command.
     */
    private static Command parseArchive(String args) throws ShonksException {
        if (args.isEmpty()) {
            return Command.archive(null);
        }

        int taskNum = parseTaskNumber(args,
                "Archive which task? Use a number. Please.");

        return Command.archive(taskNum);
    }

    private static Command parseRestoreArchive(String args) throws ShonksException {
    if (args.isEmpty()) {
        return Command.restoreArchive(null);
    }

    int archivedTaskNum = parseTaskNumber(args,
            "Restore which archived task? Use a number. Or just 'restore' for all.");

    return Command.restoreArchive(archivedTaskNum);
}

    /**
     * Splits a string using a single marker.
     */
    private static Parts splitOnMarker(String args,
                                       String marker,
                                       String errorMessage)
            throws ShonksException {

        String cleaned = requireNonBlank(args, errorMessage);
        int idx = cleaned.indexOf(marker);

        if (idx < 0) {
            throw new ShonksException(errorMessage);
        }

        String before = cleaned.substring(0, idx).trim();
        String after = cleaned.substring(idx + marker.length()).trim();

        return new Parts(before, after);
    }

    /**
     * Splits an event string using /from and /to markers.
     */
    private static EventParts splitEventMarkers(String args,
                                                String errorMessage)
            throws ShonksException {

        String cleaned = requireNonBlank(args, errorMessage);

        int fromIdx = cleaned.indexOf("/from");
        int toIdx = cleaned.indexOf("/to");

        if (fromIdx < 0 || toIdx < 0 || toIdx < fromIdx) {
            throw new ShonksException(errorMessage);
        }

        String description = cleaned.substring(0, fromIdx).trim();
        String from = cleaned.substring(fromIdx + "/from".length(), toIdx).trim();
        String to = cleaned.substring(toIdx + "/to".length()).trim();

        return new EventParts(description, from, to);
    }

    /**
     * Parses and validates a task number.
     */
    private static int parseTaskNumber(String raw,
                                       String emptyMessage)
            throws ShonksException {

        String value = requireNonBlank(raw, emptyMessage);

        try {
            int num = Integer.parseInt(value);

            if (num <= 0) {
                throw new ShonksException("Task number must be positive. Obviously.");
            }

            return num;

        } catch (NumberFormatException e) {
            throw new ShonksException("That is not even a number. Example: mark 2.");
        }
    }

    /**
     * Parses a LocalDate from a string.
     */
    private static LocalDate parseDate(String raw)
            throws ShonksException {

        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            throw new ShonksException(DATE_FORMAT_MSG);
        }
    }

    /**
     * Parses a LocalDateTime from a string.
     */
    private static LocalDateTime parseDateTime(String raw)
            throws ShonksException {

        try {
            return LocalDateTime.parse(raw);
        } catch (DateTimeParseException e) {
            throw new ShonksException(DATETIME_FORMAT_MSG);
        }
    }

    /**
     * Ensures a string is not null or blank.
     */
    private static String requireNonBlank(String value,
                                          String message)
            throws ShonksException {

        if (value == null || value.trim().isEmpty()) {
            throw new ShonksException(message);
        }

        return value.trim();
    }

    /**
     * Represents tokenized user input.
     */
    private static class Tokenized {
        private final String keyword;
        private final String args;

        private Tokenized(String keyword, String args) {
            this.keyword = keyword;
            this.args = args;
        }
    }

    /**
     * Represents a two-part split result.
     */
    private static class Parts {
        private final String before;
        private final String after;

        private Parts(String before, String after) {
            this.before = before;
            this.after = after;
        }
    }

    /**
     * Represents parsed event components.
     */
    private static class EventParts {
        private final String description;
        private final String from;
        private final String to;

        private EventParts(String description,
                           String from,
                           String to) {
            this.description = description;
            this.from = from;
            this.to = to;
        }
    }
}
