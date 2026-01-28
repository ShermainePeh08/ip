package shonks.parser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import shonks.ShonksException;
import shonks.command.Command;

public class Parser {

    public static Command parse(String input) throws ShonksException {
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
            int index = Integer.parseInt(input.substring(5).trim());
            return Command.mark(index);
        }

        if (input.startsWith("unmark")) {
            if (input.equals("unmark")) {
                throw new ShonksException("Please specify which task number to unmark.");
            }
            int index = Integer.parseInt(input.substring(7).trim());
            return Command.unmark(index);
        }

        if (input.startsWith("delete")) {
            if (input.equals("delete")) {
                throw new ShonksException("Please specify which task number to delete.");
            }
            int index = Integer.parseInt(input.substring(7).trim());
            return Command.delete(index);
        }

        if (input.startsWith("todo")) {
            if (input.equals("todo")) {
                throw new ShonksException("The description of a todo cannot be empty.");
            }
            String desc = input.substring(5).trim();
            return Command.todo(desc);
        }

        if (input.startsWith("deadline")) {
            if (!input.contains("/by")) {
                throw new ShonksException("Deadline must have /by <date>.");
            }
            String rest = input.substring(9).trim();
            String[] parts = rest.split(" /by ");
            if (parts[0].isEmpty()) {
                throw new ShonksException("The description of a deadline cannot be empty.");
            }

            LocalDate by;
            try {
                by = LocalDate.parse(parts[1].trim()); // yyyy-MM-dd
            } catch (DateTimeParseException e) {
                throw new ShonksException("Please use date format yyyy-MM-dd (e.g., 2019-10-15).");
            }

            return Command.deadline(parts[0], by);
        }

        if (input.startsWith("event")) {
            if (!input.contains("/from") || !input.contains("/to")) {
                throw new ShonksException("Event must have /from <start> /to <end>.");
            }
            String rest = input.substring(6).trim();
            String[] parts = rest.split(" /from | /to ");
            if (parts[0].isEmpty()) {
                throw new ShonksException("The description of an event cannot be empty.");
            }

            LocalDateTime from;
            LocalDateTime to;
            try {
                from = LocalDateTime.parse(parts[1].trim()); // yyyy-MM-ddTHH:mm
                to = LocalDateTime.parse(parts[2].trim());   // yyyy-MM-ddTHH:mm
            } catch (DateTimeParseException e) {
                throw new ShonksException("Please use datetime format yyyy-MM-ddTHH:mm "
                        + "(e.g., 2019-10-15T14:00).");
            }

            return Command.event(parts[0], from, to);
        }

        throw new ShonksException("I don't understand that command.");
    }
}