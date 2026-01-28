package shonks.parser;

import org.junit.jupiter.api.Test;
import shonks.command.Command;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parse_listCommand_returnsListType() throws Exception {
        Command c = Parser.parse("list");
        assertEquals(Command.Type.LIST, c.type);
    }

    @Test
    public void parse_markCommand_returnsIndex() throws Exception {
        Command c = Parser.parse("mark 2");
        assertEquals(Command.Type.MARK, c.type);
        assertEquals(2, c.index);
    }
}