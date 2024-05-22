package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {


    static File file;

    public FileBackedTaskManager(File file) {
        FileBackedTaskManager.file = file;
    }

    @Override
    public Task create(Task task) throws ManagerSaveException {
        Task taskFromFile = super.create(task);
        save();
        return taskFromFile;
    }

    @Override
    public void deleteById(int id) throws ManagerSaveException {
        super.deleteById(id);
        save();
    }

    @Override
    public void update(Task task) throws ManagerSaveException {
        super.update(task);
        save();
    }

    @Override
    public Task get(int id) throws ManagerSaveException {
        Task taskByIdFromFile = super.get(id);
        save();
        return taskByIdFromFile;
    }


    @Override
    public Epic createEpic(Epic epic) throws ManagerSaveException {
        Epic epicForFile = super.createEpic(epic);
        save();
        return epicForFile;
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic epicForFile = super.getEpicById(id);
        save();
        return epicForFile;
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        super.removeEpicById(id);
        save();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) throws ManagerSaveException {
        SubTask subTaskForFile = super.createSubTask(subTask);
        save();
        return subTaskForFile;
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public SubTask getSubTaskById(int id) throws ManagerSaveException {
        SubTask subTaskForFile = super.getSubTaskById(id);
        save();
        return subTaskForFile;
    }

    @Override
    public void removeSubTaskById(int id) throws ManagerSaveException {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void clear() throws ManagerSaveException {
        super.clear();
        save();
    }

    @Override
    public void clearEpic() throws ManagerSaveException {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubTasks() throws ManagerSaveException {
        super.clearSubTasks();
        save();
    }


    public void save() throws ManagerSaveException {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getAll());
        allTasks.addAll(getAllEpics());
        allTasks.addAll(getAllSubTask());
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,name,status,type,description" + "\n");
            for (Task task : allTasks) {
                fileWriter.write(toString(task) + "\n");
            }


        } catch (IOException e) {
            throw new ManagerSaveException("Сохранение файла прошло не удачно");
        }

    }

    protected String toString(Task task) {
        String taskToString;
        taskToString = String.format("%s,%s,%s,%s,%s,",
                task.getId(), task.getName(), task.getStatus(), task.getDescription(), task.getType());
        if (task.getType() == TypeOfTask.SUBTASK) {
            taskToString = taskToString + String.format("%s", ((SubTask) task).getEpicId());
        }
        return taskToString;
    }

    private static Task fromString(String value) {
        String[] values = value.split(",");
        String name = values[0];
        Status status = Status.valueOf(values[1]);
        String description = values[2];
        TypeOfTask type = TypeOfTask.valueOf(values[3]);
        int getEpicId = Integer.parseInt(values[4]);

        if (type == TypeOfTask.TASK) {
            return new Task(name, status, description);
        } else if (type == TypeOfTask.EPIC) {
            return new Epic(name, description);
        } else if (type == TypeOfTask.SUBTASK) {
            return new SubTask(name, status, description, getEpicId);
        }
        return null;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            Task task = null;
            while (bufferedReader.ready()) {
                String fileText = bufferedReader.readLine();
                if (!fileText.isEmpty()) {
                    task = fromString(fileText);
                }
                if (task.getType() == TypeOfTask.TASK) {
                    fileBackedTaskManager.create(task);
                } else if (task.getType() == TypeOfTask.EPIC) {
                    fileBackedTaskManager.createEpic((Epic) task);
                } else if (task.getType() == TypeOfTask.SUBTASK) {
                    fileBackedTaskManager.createSubTask((SubTask) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи файла");
        }
        return fileBackedTaskManager;
    }
}
