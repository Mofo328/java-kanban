package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, Status.NEW, description);
        this.type = TypeOfTask.EPIC;
    }

    public void removeSubTasks(Integer id) {
        subTasks.remove(id);
    }

    public List<Integer> getIdSubTasks() {
        return subTasks;
    }

    public void addSubTasks(int id) {
        subTasks.add(id);
    }

    public void clearSubTasks() {
        subTasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}