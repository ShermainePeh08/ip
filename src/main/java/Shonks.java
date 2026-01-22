import java.util.ArrayList;
import java.util.Scanner;

public class Shonks {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println("Hello! I'm Shonks");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();

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

            Task newTask = new Task(input);
            tasks.add(newTask);
            System.out.println("added: " + input);
        }
    }
}

