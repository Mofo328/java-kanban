package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected final List<Integer> subTasksFromEpic = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, Status.NEW, description);
    }

    public Epic(String name, String description) {
        super(name, Status.NEW, description);
        this.id = getId();
    }

    @Override
    public Integer getEpicId() {
        return super.getEpicId();
    }
    @Override
    public TypeOfTask getType() {
        return TypeOfTask.EPIC;
    }

    public void removeSubTasks(Integer id) {
        subTasksFromEpic.remove(id);
    }

    public List<Integer> getIdSubTasks() {
        return subTasksFromEpic;
    }

    public void addSubTasks(int id) {
        subTasksFromEpic.add(id);
    }

    public void clearSubTasks() {
        subTasksFromEpic.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasksFromEpic +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}