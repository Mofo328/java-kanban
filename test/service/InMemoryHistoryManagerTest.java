package service;


import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void BeforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void shouldBeInMemoryHistoryManagerAddTaskToHistory() {
        Task task = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        historyManager.addTaskToHistory(task);
        List<Task> tasksHistory = historyManager.getHistory();
        assertEquals(1, tasksHistory.size(), "History not add");
        assertEquals(task, tasksHistory.getFirst(), "History add,not correct");
    }

    @Test
    public void historyEmpty() {
        assertEquals(Collections.emptyList(), historyManager.getHistory(), "History not empty");
    }

    @Test
    public void shouldBeInMemoryHistoryManagerCanRemoveFirst() {
        Task task1 = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task2 = new Task("Имя2", Status.NEW, "Описание2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task3 = new Task("Имя3", Status.NEW, "Описание3", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        historyManager.remove(task1.getId());
        assertEquals(historyManager.getHistory(), List.of(task2, task3), "First element,not remove");

    }

    @Test
    public void shouldBeInMemoryHistoryManagerCanRemoveMidle() {
        Task task1 = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task2 = new Task("Имя2", Status.NEW, "Описание2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task3 = new Task("Имя3", Status.NEW, "Описание3", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        historyManager.remove(task2.getId());
        assertEquals(historyManager.getHistory(), List.of(task1, task3), "Midle element,not remove");
    }

    @Test
    public void shouldBeInMemoryHistoryManagerCanRemoveLast() {
        Task task1 = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task2 = new Task("Имя2", Status.NEW, "Описание2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task3 = new Task("Имя3", Status.NEW, "Описание3", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        historyManager.remove(task3.getId());
        assertEquals(historyManager.getHistory(), List.of(task1, task2), "Last element,not remove");
    }


    @Test
    public void shouldBeInMemoryHistoryManagerCanHaveDuplicate() {
        Task task1 = new Task("Имя", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task2 = new Task("Имя2", Status.NEW, "Описание2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        task1.setId(1);
        task2.setId(1);
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Duplicate in history");
        assertEquals(task1, history.getFirst(), "Tasks not equals");
    }

    @Test
    public void shouldBeInMemoryHistoryManagertheSubtaskDoesNotStoreTheOldId() {
        Epic epic1 = new Epic("Имя", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Epic epic2 = new Epic("Имя2", "Описание2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        epic1.setId(3);
        epic2.setId(4);
        historyManager.addTaskToHistory(epic1);
        historyManager.addTaskToHistory(epic2);
        SubTask subTask1 = new SubTask("Name", Status.NEW, "abv", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), epic1.getId());
        SubTask subTask2 = new SubTask("Name2", Status.NEW, "abv2", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), epic1.getId());
        subTask1.setId(1);
        subTask2.setId(2);
        historyManager.addTaskToHistory(subTask1);
        historyManager.addTaskToHistory(subTask2);
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        assertNotEquals(subTask1.getId(), history.get(subTask1.getId()), "Duplicate in history");
    }
}