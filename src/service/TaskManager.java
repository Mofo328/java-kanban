package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    Task create(Task task) throws ManagerSaveException;

    void deleteById(int id) throws ManagerSaveException;

    void update(Task task) throws ManagerSaveException;

    Task get(int id) throws ManagerSaveException;

    void clear() throws ManagerSaveException;

    List<Task> getAll();

    Epic createEpic(Epic epic) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    Epic getEpicById(int id) throws ManagerSaveException;

    void clearEpic() throws ManagerSaveException;

    void removeEpicById(int id) throws ManagerSaveException;

    List<Epic> getAllEpics();

    SubTask createSubTask(SubTask subTask) throws ManagerSaveException;

    void updateSubTask(SubTask subTask) throws ManagerSaveException;

    SubTask getSubTaskById(int id) throws ManagerSaveException;

    void clearSubTasks() throws ManagerSaveException;

    void removeSubTaskById(int id) throws ManagerSaveException;

    List<SubTask> getAllSubTask();

    List<SubTask> getAllSubTaskByEpicId(int id);

}