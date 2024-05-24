package service;


import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File saveFile;

    @BeforeEach
    public void beforeEach() {
        saveFile = new File("resources/tasks.csv");
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
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager();
        Task task1 = new Task("Имя задачи 1", Status.NEW, "Описание задачи 1");
        Epic epic1 = new Epic("Имя эпика 1", "Описание эпика 1");
        SubTask subTask1 = new SubTask("Имя подзадачи 1", Status.NEW, "Описание подзадачи 1", 2);
        fileBackedTaskManager.create(task1);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubTask(subTask1);
        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(saveFile);
        assertEquals(fileBackedTaskManager.getAll(), fileBackedTaskManager2.getAll(), "Списки задач не совпадают");
        assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManager2.getAllEpics(), "Списки эпиков не совпадают");
        assertEquals(fileBackedTaskManager.getAllSubTask(), fileBackedTaskManager2.getAllSubTask(), "Списки подзадач не совпадают");
    }
}

