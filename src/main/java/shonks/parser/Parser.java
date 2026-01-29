package shonks.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import shonks.ShonksException;
import shonks.command.Command;

/**
 * Parses raw user input into structured commands.
 */
public class Parser {

    private static int parseTaskNumber(String raw) throws ShonksException {
        try {
            int num = Integer.parseInt(raw.trim());
            if (num <= 0) {
                throw new ShonksException("Task number must be a positive integer.");
            }
            return num;
        } catch (NumberFormatException e) {
            throw new ShonksException("Please enter a valid task number (e.g., mark 2).");
        }
    }

    public static Command parse(String input) throws ShonksException {
        input = input.trim();

        if (input.equals("bye")) {
            return Command.exit();
        }

        if (input.equals("list")) {
            return Command.list();
        }

        if (input.startsWith("mark")) {
            if (input.equals("mark")) {
                throw new ShonksException("Please specify which task number to mark.");
            }
            int index = parseTaskNumber(input.substring(5));
            return Command.mark(index);
        }

        if (input.startsWith("unmark")) {
            if (input.equals("unmark")) {
                throw new ShonksException("Please specify which task number to unmark.");
            }
            int index = parseTaskNumber(input.substring(7));
            return Command.unmark(index);
        }

        if (input.startsWith("delete")) {
            if (input.equals("delete")) {
                throw new ShonksException("Please specify which task number to delete.");
            }
            int index = parseTaskNumber(input.substring(7));
            return Command.delete(index);
        }

        if (input.startsWith("todo")) {
            if (input.equals("todo")) {
                throw new ShonksException("The description of a todo cannot be empty.");
            }
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new ShonksException("The description of a todo cannot be empty.");
            }
            return Command.todo(desc);
        }

        if (input.startsWith("deadline")) {
            if (!input.contains("/by")) {
                throw new ShonksException("Deadline must have /by <date>.");
            }

            String rest = input.substring(9).trim();
            String[] parts = rest.split(" /by ", 2);

            if (parts.length < 2) {
                throw new ShonksException("Deadline must have /by <date>.");
            }
            if (parts[0].trim().isEmpty()) {
                throw new ShonksException("The description of a deadline cannot be empty.");
            }

            LocalDate by;
            try {
                by = LocalDate.parse(parts[1].trim());
            } catch (DateTimeParseException e) {
                throw new ShonksException("Please use date format yyyy-MM-dd (e.g., 2019-10-15).");
            }

            return Command.deadline(parts[0].trim(), by);
        }

        if (input.startsWith("event")) {
            if (!input.contains("/from") || !input.contains("/to")) {
                throw new ShonksException("Event must have /from <start> /to <end>.");
            }

            String rest = input.substring(6).trim();
            String[] parts = rest.split(" /from | /to ");

            if (parts.length < 3) {
                throw new ShonksException("Event must have /from <start> /to <end>.");
            }
            if (parts[0].trim().isEmpty()) {
                throw new ShonksException("The description of an event cannot be empty.");
            }

            LocalDateTime from;
            LocalDateTime to;
            try {
                from = LocalDateTime.parse(parts[1].trim());
                to = LocalDateTime.parse(parts[2].trim());
            } catch (DateTimeParseException e) {
                throw new ShonksException("Please use datetime format yyyy-MM-ddTHH:mm (e.g., 2019-10-15T14:00).");
            }

            return Command.event(parts[0].trim(), from, to);
        }

        if (input.startsWith("find")) {
            if (input.equals("find")) {
                throw new ShonksException("Please provide a keyword to find (e.g., find book).");
            }
            String keyword = input.substring(5).trim();
            if (keyword.isEmpty()) {
                throw new ShonksException("Please provide a keyword to find (e.g., find book).");
            }
            return Command.find(keyword);
        }

        throw new ShonksException("I don't understand that command.");
    }
}