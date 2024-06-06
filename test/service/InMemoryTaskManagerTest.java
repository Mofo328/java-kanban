package service;

import exceptions.ManagerSaveException;
import exceptions.ValidationException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void BeforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldInMemoryTaskManagerCreateAllTypeOfTasks() throws ManagerSaveException {
        Task task = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task);
        assertNotNull(taskManager.get(1), "Задача не найдена");
        Epic epic = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic);
        assertNotNull(taskManager.getEpicById(2), "Задача не найдена");
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        taskManager.createSubTask(subTask);
        assertNotNull(taskManager.getSubTaskById(3), "Задача не найдена");
    }

    @Test
    public void shouldInMemoryTaskManagerCanGetAllTypeOfTasks() throws ManagerSaveException {
        Task task = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task);
        assertEquals(1, taskManager.getAll().size());
        Epic epic = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size());
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        taskManager.createSubTask(subTask);
        assertEquals(1, taskManager.getAllSubTask().size());
    }

    @Test
    public void shouldInMemoryTaskManagerReallyEqualsTypes() throws ManagerSaveException {
        Task task = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task);
        assertEquals(task, taskManager.get(1));
        Epic epic = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicById(2));
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        taskManager.createSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTaskById(3));
    }

    @Test
    public void shouldInMemoryManagerAllTypesOfTasksCanUpdate() throws ManagerSaveException {
        Task task = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task);
        taskManager.update(task);
        assertEquals(task, taskManager.get(1));
        Epic epic = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic);
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.getEpicById(2));
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        taskManager.createSubTask(subTask);
        taskManager.updateSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTaskById(3));
    }

    @Test
    public void shouldInMemoryManagerTaskRemoveById() throws ManagerSaveException {
        Task task = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task2 = new Task("Имя2", Status.NEW, "Описание2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task);
        taskManager.create(task2);
        taskManager.deleteById(1);
        assertEquals(1, taskManager.getAll().size());

    }

    @Test
    public void shouldInMemoryManagerEpicAndSubTasksRemoveById() throws ManagerSaveException {
        Epic epic = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Epic epic2 = new Epic("Имя2", "Описание2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.removeEpicById(1);
        assertEquals(1, taskManager.getAllEpics().size());
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        SubTask subTask2 = new SubTask("Имя1", Status.NEW, "Описание2", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);
        taskManager.removeSubTaskById(3);
        assertEquals(1, taskManager.getAllSubTask().size());
    }

    @Test
    public void shouldInMemoryManagerAllTypesOfTasksCanClear() throws ManagerSaveException {
        Task task = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task);
        taskManager.update(task);
        taskManager.clear();
        boolean isEmpty = taskManager.getAll().isEmpty();
        assertTrue(isEmpty);
        Epic epic = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic);
        taskManager.updateEpic(epic);
        taskManager.clearEpic();
        boolean epicIsEmpty = taskManager.getAllEpics().isEmpty();
        assertTrue(epicIsEmpty);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        taskManager.createSubTask(subTask);
        taskManager.updateSubTask(subTask);
        taskManager.clearSubTasks();
        boolean subTaskIsEmpty = taskManager.getAllSubTask().isEmpty();
        assertTrue(subTaskIsEmpty);
    }

    @Test
    public void shouldInMemoryManagerCanGetAllSubTaskByEpicId() throws ManagerSaveException {
        Epic epic = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic);
        taskManager.updateEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        taskManager.createSubTask(subTask);
        taskManager.updateSubTask(subTask);
        boolean subTaskIsEmpty = taskManager.getAllSubTaskByEpicId(1).isEmpty();
        assertFalse(subTaskIsEmpty);
    }

    @Test
    public void shouldInMemoryTaskManagerEpicHasCorrectTimi() throws ValidationException {
        Task task = new Task("Имя1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        taskManager.create(task);
        Epic epic = new Epic("Имя2", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2020, 4, 5, 2, 0));
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Имя3", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2022, 8, 12, 1, 0), 2);
        taskManager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Имя4", Status.DONE, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2022, 10, 25, 2, 0), 2);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Имя5", Status.IN_PROGRESS, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2023, 12, 30, 3, 0), 2);
        taskManager.createSubTask(subTask2);
        taskManager.update(task);
        taskManager.updateEpic(epic);
        taskManager.updateSubTask(subTask);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        assertEquals(epic.getStartTime(), subTask.getStartTime(), "Time is not correct");
        assertEquals(epic.getEndTime(), subTask2.getEndTime(), "Time is not correct");
    }

    @Test
    public void shouldInMemoryTaskManagerTasksDoNotOverlap() throws ManagerSaveException {
        Task task = new Task("Имя1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        taskManager.create(task);
        Task task1 = new Task("Имя1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        taskManager.create(task);
        taskManager.create(task);
        taskManager.update(task);
        taskManager.update(task1);
        assertTrue(task.getStartTime().isBefore(task1.getStartTime()));
    }

    @Test
    public void shouldInMemoryTaskManagerTasksOverlap() throws ManagerSaveException {
        Task task = new Task("Имя1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2021, 1, 3, 2, 0));
        taskManager.create(task);
        Task task1 = new Task("Имя1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        taskManager.create(task);
        taskManager.create(task);
        taskManager.update(task);
        taskManager.update(task1);
        assertFalse(task.getStartTime().isBefore(task1.getStartTime()));
    }
}
