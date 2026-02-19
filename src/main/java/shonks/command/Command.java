package shonks.command;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Command {
    /**
     * Represents a parsed user command in Shonks.
     * <p>
     * Each {@code Command} instance encapsulates a command type and any associated
     * parameters required to execute that command. This includes both core task
     * management commands and extension commands such as {@code archive} and
     * {@code stats}.
     */
    public enum Type {
        EXIT, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND,
        ARCHIVE, RESTORE_ARCHIVE, STATS
    }

    public final Type type;
    public final Integer index;         // for mark/unmark/delete (1-based)
    public final String description;    // for todo/deadline/event
    public final LocalDate by;          // for deadline
    public final LocalDateTime from;    // for event
    public final LocalDateTime to;      // for event
    public final String keyword;        // for find

    private Command(Type type, Integer index, String description,
                    LocalDate by, LocalDateTime from, LocalDateTime to, String keyword) {
        this.type = type;
        this.index = index;
        this.description = description;
        this.by = by;
        this.from = from;
        this.to = to;
        this.keyword = keyword;
    }

    public static Command exit() { return new Command(Type.EXIT, null, null, null, null, null, null); }
    public static Command list() { return new Command(Type.LIST, null, null, null, null, null, null); }
    public static Command mark(int index) { return new Command(Type.MARK, index, null, null, null, null, null); }
    public static Command unmark(int index) { return new Command(Type.UNMARK, index, null, null, null, null, null); }
    public static Command delete(int index) { return new Command(Type.DELETE, index, null, null, null, null, null); }
    public static Command todo(String desc) { return new Command(Type.TODO, null, desc, null, null, null, null); }
    public static Command deadline(String desc, LocalDate by) { return new Command(Type.DEADLINE, null, desc, by, null, null, null); }
    public static Command event(String desc, LocalDateTime from, LocalDateTime to) {
        return new Command(Type.EVENT, null, desc, null, from, to, null);
    }
    public static Command find(String keyword) {
        return new Command(Type.FIND, null, null, null, null, null, keyword);
    }

    /**
     * Creates an ARCHIVE command that archives either all tasks (if {@code index} is null)
     * or a specific task number (if {@code index} is non-null).
     *
     * @param index 1-based task number to archive, or null to archive all tasks.
     * @return A {@code Command} representing an archive request.
     */
    public static Command archive(Integer index) {
        return new Command(Type.ARCHIVE, index, null, null, null, null, null);
    }

    public static Command restoreArchive(Integer index) {
        return new Command(Type.RESTORE_ARCHIVE, index, null, null, null, null, null);
    }

    /**
     * Creates a STATS command.
     * The stats command produces a summary of tasks currently stored in memory.
     *
     * @return A {@code Command} representing a stats request.
     */
    public static Command stats() {
        return new Command(Type.STATS, null, null, null, null, null, null);
    }
}
