import java.time.LocalDate;
import java.time.LocalDateTime;

public class Command {
    public enum Type {
        EXIT, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT
    }

    public final Type type;
    public final Integer index;         // for mark/unmark/delete (1-based)
    public final String description;    // for todo/deadline/event
    public final LocalDate by;          // for deadline
    public final LocalDateTime from;    // for event
    public final LocalDateTime to;      // for event

    private Command(Type type, Integer index, String description,
                    LocalDate by, LocalDateTime from, LocalDateTime to) {
        this.type = type;
        this.index = index;
        this.description = description;
        this.by = by;
        this.from = from;
        this.to = to;
    }

    public static Command exit() { return new Command(Type.EXIT, null, null, null, null, null); }
    public static Command list() { return new Command(Type.LIST, null, null, null, null, null); }
    public static Command mark(int index) { return new Command(Type.MARK, index, null, null, null, null); }
    public static Command unmark(int index) { return new Command(Type.UNMARK, index, null, null, null, null); }
    public static Command delete(int index) { return new Command(Type.DELETE, index, null, null, null, null); }
    public static Command todo(String desc) { return new Command(Type.TODO, null, desc, null, null, null); }
    public static Command deadline(String desc, LocalDate by) { return new Command(Type.DEADLINE, null, desc, by, null, null); }
    public static Command event(String desc, LocalDateTime from, LocalDateTime to) {
        return new Command(Type.EVENT, null, desc, null, from, to);
    }
}