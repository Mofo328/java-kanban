package service;

import java.io.File;

public class Managers {
    static File file = new File("C:\\Users\\Win\\IdeaProjects\\java-kanban\\resources\\tasks.csv");

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static File getFile() {
        return file;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBackedTaskManager() {
        return new FileBackedTaskManager(file);
    }
}
