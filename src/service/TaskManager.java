package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
   final private HashMap<Integer, Task> tasks;
    final private HashMap<Integer, Epic> epics;

   final  private HashMap<Integer, SubTask> subTasks;
    private int idCounter = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    public Task create (Task task) {
        task.setId(generateId());
        tasks.put(task.getId(),task);
        return task;
    }
    public void update(Task task) {
        tasks.put(task.getId(),task);
        Task saved = tasks.get(task.getId());
        saved.setName(task.getName());
        saved.setStatus(task.getStatus());
        saved.setDescription(task.getDescription());
    }
    public Task get(int id){
        return tasks.get(id);
    }

    public Task clear(Task task) {
        tasks.clear();
        return task;
    }
    public Task removeId(Task task){
        tasks.remove(task.getId());
        return task;
    }
    public Task  getAll(Task task){
        System.out.println(tasks);
        return task;
    }



    public Epic createEpic (Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic){
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            saved.setName(epic.getName());
            saved.setDescription(epic.getDescription());
        }
    }
public Epic getEpic (int id) {
       return epics.get(id);
}
    public void epicsClear(Epic epic) {
        epics.clear();
        subTasks.clear();

    }
    public  void epicRemoveId (int id) {
        for (int idSubTask : epics.get(id).getIdSubTasks()) {
            subTasks.remove(idSubTask);
        }
        epics.remove(id);
    }


        public Task  getAllEpics (Epic epic){
        System.out.println(epics);
        return epic;
    }

    public SubTask createSubTasks(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpic());
        if (epic != null) {
            subTasks.put(generateId(), subTask);
            subTask.setId(generateId());
            epic.addSubTasks(generateId());
            calculateStatus();
        }
        return subTask;
    }

    public void updateSubTask(SubTask subTask){

         if (subTasks.containsKey(subTask.getId())) {
                subTasks.put(subTask.getId(), subTask);
                final Epic epic = epics.get(subTask.getEpic());
                calculateStatus();
            }
        }
    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void clearSubTasks() {
        for (Epic epic: epics.values()) {
            epic.clearSubTasks();
        }
        subTasks.clear();
        calculateStatus();
    }

        public void subTaskRemoveId(int id) {
        SubTask subTask = subTasks.remove(id);
        Epic epic = epics.get(subTask.getEpic());
        epic.removeSubTasks(id);
        calculateStatus();
    }
    public Task  getAllSubTasks (SubTask subTask){
        System.out.println(subTasks);
        return subTask;
    }




    private Status calculateStatus(){
        boolean taskDone = true;
        boolean taskNew = true;
        if (subTasks.isEmpty()){
            return Status.NEW;
        }
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus() != Status.DONE){
                taskDone = false;
            }
            if (subTask.getStatus() != Status.NEW) {
                taskNew = false;
            }
        }
        if(taskDone) {
            return Status.DONE;
        } else if (taskNew) {
            return Status.NEW;
        }else{
            return  Status.IN_PROGRESS;
        }
    }
    private int generateId(){
        return ++idCounter;
    }
}
