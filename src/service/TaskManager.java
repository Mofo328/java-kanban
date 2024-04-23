package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();
    Task create(Task task);

    void deleteById(int id);

    void update(Task task);

    Task get(int id);

    void clear();

    List<Task> getAll();

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    Epic getEpicById(int id);

    void clearEpic();

    void removeEpicById(int id);

    List<Epic> getAllEpics();

    SubTask createSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    SubTask getSubTaskById(int id);

    void clearSubTasks();

    void removeSubTaskById(int id);

    List<SubTask> getAllSubTask();

    List<SubTask> getAllSubTaskByEpicId(int id);

}