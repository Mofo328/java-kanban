package service;

import model.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {

        public Node prev;
        public Node next;
        public Task task;


        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.next = next;
            this.task = task;
        }
    }

    private Node first;
    private Node last;
    private final Map<Integer, Node> nodes = new HashMap<>();


    private void linkLast(Task task) {
        Node newNode = new Node(last, task, null);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
    }

    private List<Task> getTasks() {
        List<Task> tasksHistory = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasksHistory.add(current.task);
            current = current.next;
        }
        return tasksHistory;
    }

    private void removeNode(int id) {
        Node nodeRemove = nodes.remove(id);
        if (nodeRemove == null) {
            return;
        }
        if (nodeRemove.prev == null) {
            first = first.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        } else if (nodeRemove.next == null) {
            last = last.prev;
            last.next = null;
        } else {
            nodeRemove.prev.next = nodeRemove.next;
            nodeRemove.next.prev = nodeRemove.prev;
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public void addTaskToHistory(Task task) {
        if (task == null) {
            return;
        }
        removeNode(task.getId());
        linkLast(task);
        nodes.put(task.getId(), last);

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
