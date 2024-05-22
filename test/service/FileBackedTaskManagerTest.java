package service;


import model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    public void shouldBeFileBackedManagerRestoreStateFromFile() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager();
        Task task1 = new Task("Имя задачи 1", Status.NEW, "Описание задачи 1");
        Epic epic1 = new Epic("Имя эпика 1", "Описание эпика 1");
        SubTask subTask1 = new SubTask("Имя подзадачи 1", Status.NEW, "Описание подзадачи 1", 2);
        fileBackedTaskManager.create(task1);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubTask(subTask1);
        FileBackedTaskManager fileBackedTaskManager2 = Managers.getDefaultFileBackedTaskManager();
        File saveFile = new File(String.valueOf(File.createTempFile("test", ".csv")));
        FileBackedTaskManager.loadFromFile(saveFile);
        assertEquals(fileBackedTaskManager.getAll(), fileBackedTaskManager2.getAll(), "Списки задач не совпадают");
        assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManager2.getAllEpics(), "Списки эпиков не совпадают");
        assertEquals(fileBackedTaskManager.getAllSubTask(), fileBackedTaskManager2.getAllSubTask(), "Списки подзадач не совпадают");
    }

    @Test
    public void StringToTask() {
        Task task1 = Converter.fromString("1,Имя задачи 1,NEW,Описание задачи 1,TASK,\n" +
                "2,Имя эпика 1,NEW,Описание эпика 1,EPIC,\n" +
                "3,Имя подзадачи 1,NEW,Описание подзадачи 1,SUBTASK,2"
        );
        Task task2 = new Task("Имя задачи 1", Status.NEW, "Описание задачи 1");
        assertEquals(task1, task2);
    }
}

