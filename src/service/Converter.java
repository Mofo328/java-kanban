package service;

import model.*;

public class Converter {

    protected static String toString(Task task) {
        return task.getId() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + task.getType() + "," + task.getEpicId();
    }

    protected static Task fromString(String value) {
        String[] values = value.split("\n");
        for (String s : values) {
            String[] tasks = s.split(",");
            int id = Integer.parseInt(tasks[0]);
            String name = tasks[1];
            Status status = Status.valueOf(tasks[2]);
            String description = tasks[3];
            TypeOfTask type = TypeOfTask.valueOf(tasks[4]);
            return switch (type) {
                case TASK -> new Task(id,name, status, description) ;
                case EPIC -> new Epic(id,name, description);
                case SUBTASK -> new SubTask(id,name, status, description, Integer.parseInt(tasks[5]));
            };
        }
        return null;
    }
}
