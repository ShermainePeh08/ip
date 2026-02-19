package shonks.command;

import java.util.EnumMap;
import java.util.Map;

import shonks.ShonksException;
import shonks.command.handlers.ArchiveHandler;
import shonks.command.handlers.DeleteHandler;
import shonks.command.handlers.DeadlineAddHandler;
import shonks.command.handlers.EventAddHandler;
import shonks.command.handlers.ExitHandler;
import shonks.command.handlers.FindHandler;
import shonks.command.handlers.ListHandler;
import shonks.command.handlers.MarkHandler;
import shonks.command.handlers.StatsHandler;
import shonks.command.handlers.TodoAddHandler;
import shonks.command.handlers.UnmarkHandler;
import shonks.command.handlers.RestoreArchiveHandler;

/**
 * Invokes the appropriate handler for a given command.
 */
public class CommandInvoker {

    private final Map<Command.Type, CommandHandler> handlers;

    /**
     * Constructs a command invoker with registered handlers.
     */
    public CommandInvoker() {
        this.handlers = new EnumMap<>(Command.Type.class);
        registerHandlers();
    }

    /**
     * Executes the given command.
     *
     * @param command The command to execute.
     * @param context The execution context.
     * @throws ShonksException If the command is unknown or execution fails.
     */
    public void execute(Command command, ShonksContext context) throws ShonksException {
        CommandHandler handler = handlers.get(command.type);
        if (handler == null) {
            throw new ShonksException("I don't understand that command.");
        }
        handler.handle(command, context);
    }

    private void registerHandlers() {
        handlers.put(Command.Type.EXIT, new ExitHandler());
        handlers.put(Command.Type.LIST, new ListHandler());
        handlers.put(Command.Type.MARK, new MarkHandler());
        handlers.put(Command.Type.UNMARK, new UnmarkHandler());
        handlers.put(Command.Type.DELETE, new DeleteHandler());
        handlers.put(Command.Type.TODO, new TodoAddHandler());
        handlers.put(Command.Type.DEADLINE, new DeadlineAddHandler());
        handlers.put(Command.Type.EVENT, new EventAddHandler());
        handlers.put(Command.Type.FIND, new FindHandler());
        handlers.put(Command.Type.STATS, new StatsHandler());
        handlers.put(Command.Type.ARCHIVE, new ArchiveHandler());
        handlers.put(Command.Type.RESTORE_ARCHIVE, new RestoreArchiveHandler());
    }
}
