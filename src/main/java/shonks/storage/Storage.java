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

/**
 * Handles loading tasks from disk and saving tasks back to disk.
 */
public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws ShonksException {
        ArrayList<Task> tasks = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            return tasks; // first run: no file yet
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
        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new ShonksException("Could not create data folder.");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Task t : tasks) {
                bw.write(t.toStorageString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ShonksException("Error saving data.");
        }
    }
}