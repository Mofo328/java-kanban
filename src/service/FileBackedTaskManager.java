package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

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
            fileWriter.write(("id,name,status,description,type,duration,startTime,endTime,epic" + "\n"));
            for (Task task : allTasks) {
                fileWriter.write(Converter.toString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Сохранение файла прошло не удачно");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager();
        try (var bufferedReader = new BufferedReader(new FileReader(file))) {
            int maxId = 0;
            Task task;
            String fileText;
            List<String> linesInFile = new ArrayList<>();
            while ((fileText = bufferedReader.readLine()) != null) {
                if (!fileText.isEmpty()) {
                    linesInFile.add(fileText);
                    for (int i = 1; i < linesInFile.size(); i++) {
                        task = Converter.fromString(linesInFile.get(i));
                        if (task != null) {
                            if (task.getId() > maxId) {
                                maxId = task.getId();
                            }
                            switch (task.getType()) {
                                case TASK -> {
                                    fileBackedTaskManager.tasks.put(task.getId(), task);
                                    fileBackedTaskManager.prioritizedTasks.add(task);
                                }
                                case EPIC -> fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                                case SUBTASK -> {
                                    fileBackedTaskManager.subTasks.put(task.getId(), (SubTask) task);
                                    Epic epic = fileBackedTaskManager.epics.get(task.getEpicId());
                                    epic.addSubTasks(task.getId());
                                    fileBackedTaskManager.prioritizedTasks.add(task);
                                }
                            }
                        }
                    }
                }
            }
            fileBackedTaskManager.idCounter = maxId + 1;
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при чтении файла");
        }

        return fileBackedTaskManager;
    }
}
