package service;


import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<TaskManager> {

    private File saveFile;

    @BeforeEach
    public void beforeEach() throws IOException {
        saveFile = File.createTempFile("tasks", ".csv", new File("C:\\Users\\Win\\IdeaProjects\\java-kanban\\resources"));
        super.taskManager = Managers.getDefaultFileBackedTaskManager();
    }

    @AfterEach
    public void afterEach() {
        if (saveFile != null && saveFile.exists()) {
            boolean delete = saveFile.delete();
            System.out.println(delete + " Файл успешно удален");
        }
    }

    @Test
    public void shouldBeFileBackedManagerRestoreStateFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(saveFile);
        Task task1 = new Task("Имя задачи 1", Status.NEW, "Описание задачи 1",
                Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        Epic epic1 = new Epic("Имя эпика 1", "Описание эпика 1", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        SubTask subTask1 = new SubTask("Имя подзадачи 1", Status.NEW, "Описание подзадачи 1",
                Duration.ofMinutes(5), LocalDateTime.of(2026, 1, 1, 2, 0), 2);
        fileBackedTaskManager.create(task1);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubTask(subTask1);
        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(saveFile);
        assertEquals(fileBackedTaskManager.getAll(), fileBackedTaskManager2.getAll(), "Списки задач не совпадают");
        assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManager2.getAllEpics(), "Списки эпиков не совпадают");
        assertEquals(fileBackedTaskManager.getAllSubTask(), fileBackedTaskManager2.getAllSubTask(), "Списки подзадач не совпадают");
    }
}

