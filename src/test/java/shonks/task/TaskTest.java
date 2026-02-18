package shonks.task;

import org.junit.jupiter.api.Test;
import shonks.ShonksException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void markDone_taskMarked() {
        Task task = new Todo("read book");
        task.markDone();
        assertTrue(task.formatStatusLine().contains("[X]"));
    }

    @Test
    public void unmarkDone_taskUnmarked() {
        Task task = new Todo("read book");
        task.markDone();
        task.unmarkDone();
        assertTrue(task.formatStatusLine().contains("[ ]"));
        assertFalse(task.formatStatusLine().contains("[X]"));
    }

    @Test
    public void contains_isCaseInsensitive() {
        Task task = new Todo("Borrow Book");
        assertTrue(task.contains("book"));
        assertTrue(task.contains("BoRrOw"));
        assertFalse(task.contains("pencil"));
    }

    @Test
    public void storageRoundTrip_todo() throws Exception {
        Task t = new Todo("alpha");
        String stored = t.toStorageString();
        Task loaded = Task.fromStorageString(stored);
        assertEquals(stored, loaded.toStorageString());
    }

    @Test
    public void storageRoundTrip_deadline() throws Exception {
        Task t = new Deadline("beta", LocalDate.of(2026, 3, 15));
        String stored = t.toStorageString();
        Task loaded = Task.fromStorageString(stored);
        assertEquals(stored, loaded.toStorageString());
    }

    @Test
    public void storageRoundTrip_event() throws Exception {
        Task t = new Event("gamma",
                LocalDateTime.of(2026, 3, 12, 14, 0),
                LocalDateTime.of(2026, 3, 12, 16, 0));
        String stored = t.toStorageString();
        Task loaded = Task.fromStorageString(stored);
        assertEquals(stored, loaded.toStorageString());
    }

    @Test
    public void fromStorageString_invalid_throws() {
        assertThrows(ShonksException.class, () -> Task.fromStorageString("X | 0 | huh"));
    }
}
