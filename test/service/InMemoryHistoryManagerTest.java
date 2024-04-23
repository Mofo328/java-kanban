package service;


import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void addTask() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void shouldBeInMemoryHistoryManagerAddTaskToHistory() {
        Task task = new Task("Имя", Status.NEW, "Описание");
        historyManager.addTaskToHistory(task);
        List<Task> history = historyManager.getHistory();
        boolean empty = history.isEmpty();
        assertFalse(empty);
    }

    @Test
    public void shouldBeInMemoryHistoryManagerSizeHaveLimitBy10() {
        List<Task> history = historyManager.getHistory();
        for (int i = 0; i < 13; i++) {
            Task task = new Task("Имя", Status.NEW, "Описание");
            historyManager.addTaskToHistory(task);
        }
        boolean isSizeLimitCorrect = history.size() == 9;
        System.out.println(history.size());
        assertTrue(isSizeLimitCorrect);
    }

    @Test
    public void shouldBeInMemoryHistoryManagerTheFirstElementIsRemovedWhenTheSizeIsFull() {
        List<Task> history = historyManager.getHistory();
        Task taskTestFirst = new Task("Тест",Status.NEW,"Описание");
        historyManager.addTaskToHistory(taskTestFirst);
        for (int i = 0; i < 10; i++) {
            Task task = new Task("Имя", Status.NEW, "Описание");
            historyManager.addTaskToHistory(task);
        }
        boolean isExpected = history.getFirst().equals(taskTestFirst);
        assertFalse(isExpected);
        }
}