package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int idCounter = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public void deleteById(int id) {
        tasks.remove(id);
    }

    public void update(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public Task get(int id) {
        return tasks.get(id);
    }

    public void clear() {
        tasks.clear();
    }

    public List<Task> getAll() {
        return new ArrayList<>((tasks.values()));
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void clearEpic() {
        epics.clear();
        subTasks.clear();
    }

    public void removeEpicById(int id) {
        for (int idSubTask : epics.get(id).getIdSubTasks()) {
            subTasks.remove(idSubTask);
        }
        epics.remove(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>((epics.values()));
    }

    public SubTask createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            subTask.setId(generateId());
            subTasks.put(subTask.getId(), subTask);
            epic.addSubTasks(subTask.getId()); // subTask.getId
            epic.setStatus(calculateStatus());
        }
        return subTask;
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            Status currentStatus = epic.getStatus();
            Status newStatus = calculateStatus();
            if (currentStatus != newStatus) {
                epic.setStatus(newStatus);
            }
        }
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void clearSubTasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            epic.setStatus(calculateStatus());
        }
    }

    public void removeSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTasks(subTask.getId());
            epic.setStatus(calculateStatus());
        }
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>((subTasks.values()));
    }

    public ArrayList<SubTask> getAllSubTaskByEpicId(int id) {
        Epic epic = epics.get(id);
        ArrayList<SubTask> subrasksList = new ArrayList<>();
        if (epic != null)
            for (int idSubTask : epic.getIdSubTasks()) {
                SubTask subTask = subTasks.get(idSubTask);
                if (subTask != null) {
                    subrasksList.add(subTask);
                }
            }
        return subrasksList;
    }

    private Status calculateStatus() {
        boolean taskDone = true;
        boolean taskNew = true;
        if (subTasks.isEmpty()) {
            return Status.NEW;
        }
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus() != Status.DONE) {
                taskDone = false;
            }
            if (subTask.getStatus() != Status.NEW) {
                taskNew = false;
            }
        }
        if (taskDone) {
            return Status.DONE;
        } else if (taskNew) {
            return Status.NEW;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    private int generateId() {
        return ++idCounter;
    }
}
