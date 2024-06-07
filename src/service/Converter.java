package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Converter {

    protected static String toString(Task task) {
        return task.getId() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getType() + "," + task.getDuration() + "," + task.getStartTime() + "," + task.getEndTime() + "," + task.getEpicId();
    }

    protected static Task fromString(String value) {
        String[] tasks = value.split(",");
        int id = Integer.parseInt(tasks[0]);
        String name = tasks[1];
        Status status = Status.valueOf(tasks[2]);
        String description = tasks[3];
        TypeOfTask type = TypeOfTask.valueOf(tasks[4]);
        Duration duration = Duration.parse(tasks[5]);
        LocalDateTime startTime = LocalDateTime.parse(tasks[6]);
        return switch (type) {
            case TASK -> new Task(id, name, status, description, duration, startTime);
            case EPIC -> new Epic(id, name, description, duration, startTime);
            case SUBTASK -> new SubTask(id, name, status, description, duration, startTime, Integer.parseInt(tasks[8]));
        };
    }
}



