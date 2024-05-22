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
        String[] values = value.split("\n");
        for (int i = 0; i < values.length; i++) {
            String[] tasks = values[i].split(",");
            int id = Integer.parseInt(tasks[0]);
            String name = tasks[1];
            Status status = Status.valueOf(tasks[2]);
            String description = tasks[3];
            TypeOfTask type = TypeOfTask.valueOf(tasks[4]);
            return switch (type) {
                case TASK -> new Task(name, status, description);
                case EPIC -> new Epic(name, description);
                case SUBTASK -> new SubTask(name, status, description, Integer.parseInt(tasks[5]));
            };
        }
        return null;
    }
}
