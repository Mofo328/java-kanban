import exceptions.ManagerSaveException;
import model.*;
import service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Переезд", Status.NEW, "Собираем вещи", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task1);
        System.out.println("Create task 1: " + task1);

        Task task2 = new Task("Выгулить животное", Status.NEW, "Собака", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.create(task2);
        System.out.println("Create task 2: " + task2);

        Epic epic1 = new Epic("Пойти за покупками", "Пойти в пятерку", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic1);
        System.out.println("Create epic 1: " + taskManager.getEpicById(epic1.getId()));

        SubTask subTask1 = new SubTask("Купить лук", Status.NEW, "Проверить на свежесть", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), epic1.getId());
        taskManager.createSubTask(subTask1);
        System.out.println("Create subtask1: " + subTask1);
        System.out.println("Create epic 1 - subtask 1: " + epic1);

        SubTask subTask2 = new SubTask("Купить молоко", Status.IN_PROGRESS, "Сверить дату", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0), epic1.getId());
        taskManager.createSubTask(subTask2);
        System.out.println("Create subtask2: " + subTask2);
        System.out.println("Create epic 1 - subtask 2: " + epic1);
//
        Epic epic2 = new Epic("Погулять", "Не забыть взять зонт", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        taskManager.createEpic(epic2);
        System.out.println("Create epic 2: " + epic2);
        taskManager.updateEpic(epic2);
        System.out.println("Update epic 2: " + taskManager.getEpicById(epic2.getId()));


        SubTask subTask3 = new SubTask("Купить билет", Status.DONE, "В кассе", Duration.ofMinutes(4), LocalDateTime.of(2024, 6, 1, 2, 0), epic1.getId());
        taskManager.createSubTask(subTask3);
        System.out.println(taskManager.getAllSubTaskByEpicId(epic1.getId()));
        System.out.println("Status: " + taskManager.getEpicById((epic1.getId())));
        taskManager.removeSubTaskById(subTask3.getId());
        System.out.println("Status: " + taskManager.getEpicById((epic1.getId())));
        taskManager.removeEpicById(epic2.getId());
        System.out.println("Result: " + taskManager.getEpicById(epic2.getId()));
        System.out.println("Result: " + taskManager.getSubTaskById(subTask3.getId()));
        subTask3 = new SubTask("Купить хомяка", Status.NEW, "Толстого", Duration.ofMinutes(5), LocalDateTime.of(2024, 10, 1, 2, 0), epic2.getId());
        taskManager.updateSubTask(subTask3);
        System.out.println("Update subTask" + subTask3);
        taskManager.clearSubTasks();
        System.out.println(taskManager.getAllSubTask());
        taskManager.clearEpic();
        System.out.println(taskManager.getAllEpics());

    }
}