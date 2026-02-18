package shonks.parser;

import org.junit.jupiter.api.Test;
import shonks.ShonksException;
import shonks.command.Command;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parse_list_returnsListType() throws Exception {
        Command c = Parser.parse("list");
        assertEquals(Command.Type.LIST, c.type);
    }

    @Test
    public void parse_todo_parsesDescription() throws Exception {
        Command c = Parser.parse("todo read book");
        assertEquals(Command.Type.TODO, c.type);
        assertEquals("read book", c.description);
    }

    @Test
    public void parse_deadline_parsesByDate() throws Exception {
        Command c = Parser.parse("deadline return book /by 2026-03-15");
        assertEquals(Command.Type.DEADLINE, c.type);
        assertEquals("return book", c.description);
        assertEquals(LocalDate.of(2026, 3, 15), c.by);
    }

    @Test
    public void parse_event_parsesFromTo() throws Exception {
        Command c = Parser.parse("event meeting /from 2026-03-12T14:00 /to 2026-03-12T16:00");
        assertEquals(Command.Type.EVENT, c.type);
        assertEquals("meeting", c.description);
        assertEquals(LocalDateTime.of(2026, 3, 12, 14, 0), c.from);
        assertEquals(LocalDateTime.of(2026, 3, 12, 16, 0), c.to);
    }

    @Test
    public void parse_archive_withoutIndex_archivesAll() throws Exception {
        Command c = Parser.parse("archive");
        assertEquals(Command.Type.ARCHIVE, c.type);
        assertNull(c.index);
    }

    @Test
    public void parse_archive_withIndex_archivesOne() throws Exception {
        Command c = Parser.parse("archive 2");
        assertEquals(Command.Type.ARCHIVE, c.type);
        assertEquals(2, c.index);
    }

    @Test
    public void parse_stats_returnsStatsType() throws Exception {
        Command c = Parser.parse("stats");
        assertEquals(Command.Type.STATS, c.type);
    }

    @Test
    public void parse_unknown_throws() {
        assertThrows(ShonksException.class, () -> Parser.parse("glorp"));
    }

    @Test
    public void parse_deadline_missingBy_throws() {
        assertThrows(ShonksException.class, () -> Parser.parse("deadline return book"));
    }

    @Test
    public void parse_event_missingMarkers_throws() {
        assertThrows(ShonksException.class, () -> Parser.parse("event meeting /from 2026-03-12T14:00"));
    }
}
