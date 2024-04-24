package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void BeforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldInMemoryTaskManagerCreateAllTypeOfTasks() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        taskManager.create(task);
        assertNotNull(taskManager.get(1), "Задача не найдена");
        Epic epic = new Epic("Имя", "Описание");
        taskManager.createEpic(epic);
        assertNotNull(taskManager.getEpicById(2), "Задача не найдена");
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        taskManager.createSubTask(subTask);
        assertNotNull(taskManager.getSubTaskById(3), "Задача не найдена");
    }

    @Test
    public void shouldInMemoryTaskManagerCanGetAllTypeOfTasks() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        taskManager.create(task);
        assertEquals(1, taskManager.getAll().size());
        Epic epic = new Epic("Имя", "Описание");
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size());
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        taskManager.createSubTask(subTask);
        assertEquals(1, taskManager.getAllSubTask().size());
    }

    @Test
    public void shouldInMemoryTaskManagerReallyEqualsTypes() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        taskManager.create(task);
        assertEquals(task, taskManager.get(1));
        Epic epic = new Epic("Имя", "Описание");
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicById(2));
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        taskManager.createSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTaskById(3));
    }

    @Test
    public void shouldInMemoryManagerAllTypesOfTasksCanUpdate() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        taskManager.create(task);
        taskManager.update(task);
        assertEquals(task, taskManager.get(1));
        Epic epic = new Epic("Имя", "Описание");
        taskManager.createEpic(epic);
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.getEpicById(2));
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        taskManager.createSubTask(subTask);
        taskManager.updateSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTaskById(3));
    }

    @Test
    public void shouldInMemoryManagerTaskRemoveById() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        Task task2 = new Task("Имя2", Status.NEW, "Описание2");
        taskManager.create(task);
        taskManager.create(task2);
        taskManager.deleteById(1);
        assertEquals(1, taskManager.getAll().size());

    }

    @Test
    public void shoudInMemoryManagerEpicAndSubTasksRemoveById() {
        Epic epic = new Epic("Имя", "Описание");
        Epic epic2 = new Epic("Имя2", "Описание2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.removeEpicById(1);
        assertEquals(1, taskManager.getAllEpics().size());
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        SubTask subTask2 = new SubTask("Имя1", Status.NEW, "Описание2", 2);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);
        taskManager.removeSubTaskById(3);
        assertEquals(1, taskManager.getAllSubTask().size());
    }

    @Test
    public void shoudInMemoryManagerAllTypesOfTasksCanClear() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        taskManager.create(task);
        taskManager.update(task);
        taskManager.clear();
        boolean isEmpty = taskManager.getAll().isEmpty();
        assertTrue(isEmpty);
        Epic epic = new Epic("Имя", "Описание");
        taskManager.createEpic(epic);
        taskManager.updateEpic(epic);
        taskManager.clearEpic();
        boolean epicIsEmpty = taskManager.getAllEpics().isEmpty();
        assertTrue(epicIsEmpty);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 2);
        taskManager.createSubTask(subTask);
        taskManager.updateSubTask(subTask);
        taskManager.clearSubTasks();
        boolean subTaskIsEmpty = taskManager.getAllSubTask().isEmpty();
        assertTrue(subTaskIsEmpty);
    }

    @Test
    public void shoudInMemoryManagerCanGetAllSubTaskByEpicId() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.createEpic(epic);
        taskManager.updateEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", 1);
        taskManager.createSubTask(subTask);
        taskManager.updateSubTask(subTask);
        boolean subTaskIsEmpty = taskManager.getAllSubTaskByEpicId(1).isEmpty();
        assertFalse(subTaskIsEmpty);
    }
}