import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Переезд", Status.NEW ,"Собираем вещи");
        taskManager.create(task1);
        System.out.println("Create task 1: " + task1);

        Task task2 = new Task("Выгулить животное", Status.NEW,"Собака");
        taskManager.create(task2);
        System.out.println("Create task 2: " + task2);

        Epic epic1 = new Epic("Пойти за покупками", "Пойти в пятерку");
        taskManager.createEpic(epic1);
        System.out.println("Create epic 1: " + epic1);

        SubTask subTask1 = new SubTask("Купить лук", Status.NEW ,"Проверить на свежесть", epic1);
        taskManager.createSubTasks(subTask1);
        System.out.println("Create subtask1: " + subTask1);
        System.out.println("Create epic 1 - subtask 1: " + epic1);

        SubTask subTask2 = new SubTask("Купить молоко", Status.IN_PROGRESS, "Сверить дату", epic1);
        taskManager.createSubTasks(subTask2);
        System.out.println("Create subtask2: " + subTask2);
        System.out.println("Create epic 1 - subtask 2: " + epic1);

        Epic epic2 = new Epic("Погулять", "Не забыть взять зонт");
        taskManager.createEpic(epic2);
        System.out.println("Create epic 2: " + epic2);


        SubTask subTask3 = new SubTask("Купить билет", Status.DONE ,"В кассе", epic2);
        taskManager.createSubTasks(subTask3);
        System.out.println("Create 04: " + subTask3);
        System.out.println("Create 4.1: " + epic2);

        System.out.println("Status: " + taskManager.getEpic((epic1.getId())));
        taskManager.subTaskRemoveId(subTask3.getId());
        System.out.println("Status: " + taskManager.getEpic((epic1.getId())));
        taskManager.epicRemoveId(epic2.getId());
        System.out.println("Result: " + taskManager.getEpic(epic2.getId()));
        System.out.println("Result: " + taskManager.getSubTask(subTask3.getId()));





    }
}