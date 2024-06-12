package service;

import exceptions.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected int idCounter = 0;

    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            checkTaskTime(task);
            prioritizedTasks.add(task);
        }
        return task;
    }

    @Override
    public void deleteById(int id) {
        Task task = get(id);
        tasks.remove(id);
        prioritizedTasks.remove(task);
        historyManager.remove(id);
    }

    @Override
    public void update(Task task) {
        Task existingTask = tasks.get(task.getId());
        if (existingTask != null) {
            checkTaskTime(task);
            prioritizedTasks.remove(existingTask);
            prioritizedTasks.add(task);
            tasks.put(task.getId(), task);
        }
    }

    public Task get(int id) {
        return Optional.ofNullable(tasks.get(id))
                .map(task -> {
                    historyManager.addTaskToHistory(task);
                    return task;
                })
                .orElseThrow(() -> new ValidationException("Задача с ID " + id + " не найдена"));
    }

    @Override
    public void clear() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>((tasks.values()));
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic existingEpic = epics.get(epic.getId());
        if (existingEpic != null) {
            existingEpic.setName(epic.getName());
            existingEpic.setDescription(epic.getDescription());
            updateEpicInfo(epic);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public Epic getEpicById(int id) {
        return Optional.ofNullable(epics.get(id))
                .map(epic -> {
                    historyManager.addTaskToHistory(epic);
                    return epic;
                })
                .orElseThrow(() -> new ValidationException("Задача с ID " + id + " не найдена"));
    }

    @Override
    public void clearEpic() {
        epics.keySet().forEach(historyManager::remove);
        subTasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subTasks.clear();
        prioritizedTasks.remove(subTasks.values());
    }

    @Override
    public void removeEpicById(int id) {
        epics.get(id).getIdSubTasks().forEach(idSubTask -> {
            subTasks.remove(idSubTask);
            historyManager.remove(idSubTask);
            prioritizedTasks.remove(subTasks.get(idSubTask));
        });
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>((epics.values()));
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            subTask.setId(generateId());
            epic.addSubTasks(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
            updateEpicInfo(epic);
            if (subTask.getStartTime() != null) {
                checkTaskTime(subTask);
                prioritizedTasks.add(subTask);
            }
        }
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask existingSubTask = subTasks.get(subTask.getId());
        if (existingSubTask != null) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = getEpicById(subTask.getEpicId());
            updateEpicInfo(epic);
            checkTaskTime(subTask);
            prioritizedTasks.remove(existingSubTask);
            prioritizedTasks.add(subTask);
            subTasks.put(subTask.getId(), subTask);
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return Optional.ofNullable(subTasks.get(id))
                .map(subTask -> {
                    historyManager.addTaskToHistory(subTask);
                    return subTask;
                })
                .orElseThrow(() -> new ValidationException("Задача с ID " + id + " не найдена"));
    }

    @Override
    public void clearSubTasks() {
        subTasks.keySet().forEach(historyManager::remove);
        epics.values().forEach(epic -> {
            epic.clearSubTasks();
            updateEpicInfo(epic);
        });
        subTasks.values().forEach(prioritizedTasks::remove);
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTasks(id);
            updateEpicInfo(epic);
        }
        historyManager.remove(id);
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void updateEpicInfo(Epic epic) {
        boolean taskDone = true;
        boolean taskNew = true;
        LocalDateTime minTime = LocalDateTime.MAX;
        LocalDateTime maxTime = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(0);


        if (epic.getIdSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (int subTask : epic.getIdSubTasks()) {
            SubTask currentSubTask = getSubTaskById(subTask);
            if (currentSubTask == null) {
                continue;
            }

            if (currentSubTask.getStatus() != Status.DONE) {
                taskDone = false;
            }

            if (currentSubTask.getStatus() != Status.NEW) {
                taskNew = false;
            }

            if (currentSubTask.getStartTime().isBefore(minTime)) {
                minTime = currentSubTask.getStartTime();
            }

            if (currentSubTask.getEndTime().isAfter(maxTime)) {
                maxTime = currentSubTask.getEndTime();
            }
            duration = duration.plus(currentSubTask.getDuration());

            if (taskDone) {
                epic.setStatus(Status.DONE);
            } else if (taskNew) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }

            epic.setStartTime(minTime);
            epic.setEndTime(maxTime);
            epic.setDuration(duration);
        }
    }

    private boolean checkTasksOverlap(Task task, Task existTask) {
        return !(task.getStartTime().isAfter(existTask.getEndTime()) || task.getEndTime().isBefore(existTask.getStartTime()));
    }

    private void checkTaskTime(Task task) {
        List<Task> prioritizedTasks = getPrioritizedTasks();
        prioritizedTasks.stream()
                .filter(existTask -> existTask.getId() != task.getId())
                .filter(existTask -> checkTasksOverlap(task, existTask))
                .findFirst()
                .ifPresent(existTask -> {
                    throw new ValidationException("Задача " + task.getName() +
                            " пересекается с задачей " + existTask.getName());
                });
    }

    public int generateId() {
        return ++idCounter;
    }
}
