package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected final List<Integer> subTasksFromEpic = new ArrayList<>();

    protected Duration duration;
    protected LocalDateTime startTime;

    protected  LocalDateTime endTime;

    public Epic(int id, String name, String description, Duration duration, LocalDateTime startTime) {
        super(id, name, Status.NEW, description,duration,startTime);
        this.duration = duration;
        this.startTime = startTime;
    }

    public Epic(String name, String description,Duration duration,LocalDateTime localDateTime) {
        super(name, Status.NEW, description,duration,localDateTime);
        this.id = getId();
        this.duration = Duration.ofMinutes(15);
        this.startTime = LocalDateTime.now();
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
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    @Override
    public LocalDateTime getEndTime() {
       return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}