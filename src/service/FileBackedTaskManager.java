package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    private Converter converter;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task create(Task task) {
        Task taskFromFile = super.create(task);
        save();
        return taskFromFile;
    }

    @Override
    public void deleteById(int id) {
        super.deleteById(id);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epicForFile = super.createEpic(epic);
        save();
        return epicForFile;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask subTaskForFile = super.createSubTask(subTask);
        save();
        return subTaskForFile;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void clear() {
        super.clear();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    public void save() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getAll());
        allTasks.addAll(getAllEpics());
        allTasks.addAll(getAllSubTask());
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,name,status,type,description" + "\n");
            for (Task task : allTasks) {
                fileWriter.write(converter.toString(task) + "\n");
            }


        } catch (IOException e) {
            throw new ManagerSaveException("Сохранение файла прошло не удачно");
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            Task task = null;
            while (bufferedReader.ready()) {
                String fileText = bufferedReader.readLine();
                if (!fileText.isEmpty()) {
                    task = Converter.fromString(fileText);
                }
                switch (task.getType()) {
                    case TASK -> fileBackedTaskManager.tasks.put(task.getId(), task);
                    case EPIC -> fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                    case SUBTASK -> fileBackedTaskManager.subTasks.put(task.getId(), (SubTask) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи файла");
        }
        return fileBackedTaskManager;
    }
}
