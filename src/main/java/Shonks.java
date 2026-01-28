import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point of the Shonks chatbot.
 * <p>
 * Shonks reads user commands from standard input and manages a list of tasks.
 */
public class Shonks {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Storage storage = new Storage("./data/shonks.txt");
        ArrayList<Task> tasks;

        try {
            tasks = storage.load();
        } catch (ShonksException e) {
            tasks = new ArrayList<>();
            System.out.println("Oops! " + e.getMessage());
        }

        System.out.println("Hello! I'm Shonks");
        System.out.println("What can I do for you?");

        while (true) {
            try {
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

                if (input.startsWith("mark")) {
                    if (input.equals("mark")) {
                        throw new ShonksException("Please specify which task number to mark.");
                    }
                    int index = Integer.parseInt(input.substring(5).trim());
                    Task t = tasks.get(index - 1);
                    t.markDone();
                    storage.save(tasks);

                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(t.formatStatusLine());
                    continue;
                }

                if (input.startsWith("unmark")) {
                    if (input.equals("unmark")) {
                        throw new ShonksException("Please specify which task number to unmark.");
                    }
                    int index = Integer.parseInt(input.substring(7).trim());
                    Task t = tasks.get(index - 1);
                    t.unmarkDone();
                    storage.save(tasks);

                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(t.formatStatusLine());
                    continue;
                }

                if (input.startsWith("delete")) {
                    if (input.equals("delete")) {
                        throw new ShonksException("Please specify which task number to delete.");
                    }

                    int index = Integer.parseInt(input.substring(7).trim());
                    Task removed = tasks.remove(index - 1);
                    storage.save(tasks);

                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removed.formatStatusLine());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                if (input.startsWith("todo")) {
                    if (input.equals("todo")) {
                        throw new ShonksException("The description of a todo cannot be empty.");
                    }
                    Task t = new Todo(input.substring(5).trim());
                    tasks.add(t);
                    storage.save(tasks);

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + t.formatStatusLine());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                if (input.startsWith("deadline")) {
                    if (!input.contains("/by")) {
                        throw new ShonksException("Deadline must have /by <time>.");
                    }
                    String rest = input.substring(9).trim();
                    String[] parts = rest.split(" /by ");
                    if (parts[0].isEmpty()) {
                        throw new ShonksException("The description of a deadline cannot be empty.");
                    }
                    Task t = new Deadline(parts[0], parts[1]);
                    tasks.add(t);
                    storage.save(tasks);

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + t.formatStatusLine());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
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
                    Task t = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(t);
                    storage.save(tasks);

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + t.formatStatusLine());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                throw new ShonksException("I don't understand that command.");

            } catch (ShonksException e) {
                System.out.println("Oops! " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Oops! Something went wrong.");
            }
        }
    }
}