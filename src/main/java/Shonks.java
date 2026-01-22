import java.util.ArrayList;
import java.util.Scanner;

public class Shonks {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> items = new ArrayList<>();

        System.out.println("Hello! I'm Shonks");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (input.equals("list")) {
                for (int i = 0; i < items.size(); i++) {
                    System.out.println((i + 1) + ". " + items.get(i));
                }
                continue;
            }

            items.add(input);
            System.out.println("added: " + input);

            System.out.println(input);
        }
    }
}

