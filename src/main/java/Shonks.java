import java.util.ArrayList;
import java.util.Scanner;

public class Shonks {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println("Hello! I'm Shonks");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(tasks.get(i).formatForList(i + 1));
                }
                continue;
            }

            if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim());
                Task t = tasks.get(index - 1);
                t.markDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(t.formatStatusLine());
                continue;
            }

            if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim());
                Task t = tasks.get(index - 1);
                t.unmarkDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(t.formatStatusLine());
                continue;
            }

            if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                Task t = new Todo(desc);
                tasks.add(t);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t.formatStatusLine());
                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                continue;
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring(9).trim();
                int byPos = rest.indexOf(" /by ");
                if (byPos == -1) {
                    System.out.println("Please use: deadline <task> /by <time>");
                    continue;
                }
                String desc = rest.substring(0, byPos).trim();
                String by = rest.substring(byPos + 5).trim(); // after " /by "
                Task t = new Deadline(desc, by);
                tasks.add(t);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t.formatStatusLine());
                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                continue;
            }

            if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");
                if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                    System.out.println("Please use: event <task> /from <start> /to <end>");
                    continue;
                }
                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + 7, toPos).trim(); // after " /from "
                String to = rest.substring(toPos + 5).trim(); // after " /to "
                Task t = new Event(desc, from, to);
                tasks.add(t);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t.formatStatusLine());
                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                continue;
            }

            System.out.println("Unknown command. Try: todo, deadline, event, list, mark, unmark, bye");
        }
    }
}
