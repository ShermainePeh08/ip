package shonks.storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import shonks.ShonksException;
import shonks.task.Task;

public class Storage {
    /**
     * Handles loading, saving, and archiving of tasks in Shonks.
     * <p>
     * This class manages all file I/O operations, including persisting the current
     * task list to disk and appending archived tasks to a separate archive file.
     * Tasks are stored in a single-line text format defined by
     * {@link shonks.task.Task#toStorageString()}.
     */
    private final String filePath;

    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path should be non-empty";
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws ShonksException {
        assert filePath != null && !filePath.isEmpty() : "File path should be non-empty";
        ArrayList<Task> tasks = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                tasks.add(Task.fromStorageString(line));
            }
        } catch (IOException e) {
            throw new ShonksException("Error loading saved data.");
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws ShonksException {
        assert tasks != null : "Tasks to save should not be null";
        assert filePath != null && !filePath.isEmpty() : "File path should be non-empty";

        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new ShonksException("Could not create data folder.");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                assert task != null : "Task in list should not be null";
                bw.write(task.toStorageString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ShonksException("Error saving data.");
        }
    }

    /**
     * Appends the given tasks to an archive file.
     * <p>
     * This does not modify the in-memory task list. Callers should clear the current list
     * separately if they want a "clean slate" effect after archiving.
     * <p>
     * Each archived task is written using {@link shonks.task.Task#toStorageString()}.
     *
     * @param archivePath Path to the archive file to append to.
     * @param tasks Tasks to archive.
     * @throws shonks.ShonksException If archiving fails due to I/O errors or invalid paths.
     */
    public void archiveTo(String archivePath, ArrayList<Task> tasks) throws ShonksException {
        assert archivePath != null && !archivePath.isEmpty() : "Archive path should be non-empty";
        assert tasks != null : "Tasks to archive should not be null";

        File file = new File(archivePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new ShonksException("Could not create archive folder.");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) { // append = true
            for (Task task : tasks) {
                bw.write(task.toStorageString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ShonksException("Error archiving data.");
        }
    }

    public ArrayList<Task> loadFromPath(String path) throws ShonksException {
        assert path != null && !path.isEmpty() : "Path should be non-empty";
        return loadFromFile(new File(path), new ArrayList<>(), "Error loading data.");
    }

    public void overwriteTo(String path, ArrayList<Task> tasks) throws ShonksException {
        assert path != null && !path.isEmpty() : "Path should be non-empty";
        assert tasks != null : "Tasks to write should not be null";
        writeToPath(path, tasks, false, "Could not create folder.", "Error writing data.");
    }

    private static ArrayList<Task> loadFromFile(File file,
                                           ArrayList<Task> tasks,
                                           String ioErrorMessage) throws ShonksException {
        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                tasks.add(Task.fromStorageString(line));
            }
        } catch (IOException e) {
            throw new ShonksException(ioErrorMessage);
        }

        return tasks;
    }

    private static void writeToPath(String path,
                                    ArrayList<Task> tasks,
                                    boolean append,
                                    String mkdirErrorMessage,
                                    String ioErrorMessage) throws ShonksException {
        File file = new File(path);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new ShonksException(mkdirErrorMessage);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
            for (Task task : tasks) {
                assert task != null : "Task in list should not be null";
                bw.write(task.toStorageString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ShonksException(ioErrorMessage);
        }
    }
}
