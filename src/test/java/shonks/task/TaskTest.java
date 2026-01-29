package shonks.task;

import org.junit.jupiter.api.Test;
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
}