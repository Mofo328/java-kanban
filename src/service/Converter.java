package service;

import model.*;

public class Converter {


    protected String toString(Task task) {
        String taskToString;
        taskToString = String.format("%s,%s,%s,%s,%s,",
                task.getId(), task.getName(), task.getStatus(), task.getDescription(), task.getType());
        if (task.getType() == TypeOfTask.SUBTASK) {
            taskToString = taskToString + String.format("%s", ((SubTask) task).getEpicId());
        }
        return taskToString;
    }

    protected static Task fromString(String value) {
        String[] values = value.split(",");
        String name = values[0];
        Status status = Status.valueOf(values[1]);
        String description = values[2];
        TypeOfTask type = TypeOfTask.valueOf(values[3]);
        int getEpicId = Integer.parseInt(values[4]);
        return switch (type) {
            case TASK -> new Task(name, status, description);
            case EPIC -> new Epic(name, description);
            case SUBTASK -> new SubTask(name, status, description, getEpicId);
        };
    }
}
