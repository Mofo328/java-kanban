package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasksHistory = new ArrayList<>();
    private final static int HISTORY_SIZE_LIMIT = 10;

    @Override
    public void addTaskToHistory(Task task) {
        if (task != null) {
            if (tasksHistory.size() == HISTORY_SIZE_LIMIT) {
                tasksHistory.removeFirst();
            }
        }
        tasksHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(tasksHistory);
    }
}
