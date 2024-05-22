package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int idCounter = 0;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    @Override
    public Task create(Task task) throws ManagerSaveException {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }


    @Override
    public void deleteById(int id) throws ManagerSaveException {
        tasks.remove(id);
    }


    @Override
    public void update(Task task) throws ManagerSaveException {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }


    @Override
    public Task get(int id) throws ManagerSaveException {
        Task task = tasks.get(id);
        historyManager.addTaskToHistory(task);
        return task;
    }


    @Override
    public void clear() throws ManagerSaveException {
        tasks.clear();
    }


    @Override
    public List<Task> getAll() {
        return new ArrayList<>((tasks.values()));
    }


    @Override
    public Epic createEpic(Epic epic) throws ManagerSaveException {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }


    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        if (epics.containsKey(epic.getId())) {
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }
    }


    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic epic = epics.get(id);
        historyManager.addTaskToHistory(epic);
        return epic;
    }


    @Override
    public void clearEpic() throws ManagerSaveException {
        epics.clear();
        subTasks.clear();
    }


    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        for (int idSubTask : epics.get(id).getIdSubTasks()) {
            subTasks.remove(idSubTask);
        }
        epics.remove(id);
    }


    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>((epics.values()));
    }


    @Override
    public SubTask createSubTask(SubTask subTask) throws ManagerSaveException {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            subTask.setId(generateId());
            subTasks.put(subTask.getId(), subTask);
            epic.addSubTasks(subTask.getId()); // subTask.getId
            epic.setStatus(calculateStatus());
        }
        return subTask;
    }


    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
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


    @Override
    public SubTask getSubTaskById(int id) throws ManagerSaveException {
        SubTask subTask = subTasks.get(id);
        historyManager.addTaskToHistory(subTask);
        return subTask;
    }


    @Override
    public void clearSubTasks() throws ManagerSaveException {
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            epic.setStatus(calculateStatus());
        }
    }


    @Override
    public void removeSubTaskById(int id) throws ManagerSaveException {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTasks(id);
            epic.setStatus(calculateStatus());
        }
    }


    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>((subTasks.values()));
    }


    @Override
    public List<SubTask> getAllSubTaskByEpicId(int id) {
        Epic epic = epics.get(id);
        List<SubTask> subtasksList = new ArrayList<>();
        if (epic != null)
            for (int idSubTask : epic.getIdSubTasks()) {
                SubTask subTask = subTasks.get(idSubTask);
                if (subTask != null) {
                    subtasksList.add(subTask);
                }
            }
        return subtasksList;
    }


    public Status calculateStatus() {
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


    public int generateId() {
        return ++idCounter;
    }
}
