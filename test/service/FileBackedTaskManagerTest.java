package service;

import exceptions.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {


    FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    public void BeforeEach() throws IOException {
        File tempFile = File.createTempFile("temp", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(tempFile);
    }

    @Test
    public void shoudBeFileBackedCanSaveTasksToFile() throws ManagerSaveException {
        Task task = new Task("Имя", Status.NEW, "Описание");
        Epic epic = new Epic("Имя", "Описание");
        epic.setId(2);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        fileBackedTaskManager.create(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subTask);
        Task loadedTask = fileBackedTaskManager.get(task.getId());
        assertEquals(task, loadedTask, "Загруженная задача не соответствует сохраненной задаче");

        Epic loadedEpic = fileBackedTaskManager.getEpicById(epic.getId());
        assertEquals(epic, loadedEpic, "Загруженный эпик не соответствует сохраненному эпику");

        SubTask loadedSubTask = fileBackedTaskManager.getSubTaskById(subTask.getId());
        assertEquals(subTask, loadedSubTask, "Загруженная подзадача не соответствует сохраненной подзадаче");
    }

    @Test
    public void shouldBeFileBackedCanGetTasksToString() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        Epic epic = new Epic("Имя", "Описание");
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        fileBackedTaskManager.toString(task);
        fileBackedTaskManager.toString(epic);
        fileBackedTaskManager.toString(subTask);
        assertEquals("0,Имя,NEW,Описание,TASK,", fileBackedTaskManager.toString(task), "Incorrect toString output for Task");
        assertEquals("0,Имя,NEW,Описание,EPIC,", fileBackedTaskManager.toString(epic), "Incorrect toString output for Task");
        assertEquals("0,Имя,NEW,Описание,SUBTASK,2", fileBackedTaskManager.toString(subTask), "Incorrect toString output for Task");
    }
}

