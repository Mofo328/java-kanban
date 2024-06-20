package network;

import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static handlers.BaseHttpHandler.getGson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {


    TaskManager manager = Managers.getDefault();

    HttpTaskServer taskServer = new HttpTaskServer(manager);


    @BeforeEach
    public void setUp() {
        manager.clear();
        manager.clearEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void canTaskCreated() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        String taskJson = getGson().toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getAll();
        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя 1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.create(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.get(1).toString(), getGson().fromJson(response.body(), Task.class).toString(),
                "Задачи не совпадают");
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.create(task);
        manager.create(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getAll();
        List<Task> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.create(task);
        manager.create(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getAll();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.create(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getAll();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    public void canEpicCreated() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        String taskJson = getGson().toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> tasksFromManager = manager.getAllEpics();
        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя 1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpicById(1).toString(), getGson().fromJson(response.body(), Epic.class).toString(),
                "Задачи не совпадают");
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Epic epic2 = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        manager.createEpic(epic2);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0), 2);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> expectedTasks = manager.getAllEpics();
        List<Task> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void getSubtaskByEpicId() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<SubTask> expectedTasks = manager.getAllSubTaskByEpicId(1);
        List<SubTask> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Epic epic2 = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        manager.createEpic(epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> expectedTasks = manager.getAllEpics();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> expectedTasks = manager.getAllEpics();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    public void canSubTaskCreated() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        String taskJson = getGson().toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> tasksFromManager = manager.getAllSubTask();
        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void getSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubTaskById(2).toString(), getGson().fromJson(response.body(), SubTask.class).toString(),
                "Задачи не совпадают");
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<SubTask> expectedTasks = manager.getAllSubTask();
        List<Task> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void deleteSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<SubTask> expectedTasks = manager.getAllSubTask();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void deleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Имя", Status.NEW, "Описание", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<SubTask> expectedTasks = manager.getAllSubTask();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.create(task);
        manager.create(task1);
        manager.get(1);
        manager.get(2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getHistory();
        assertEquals(expectedTasks.size(), 2);
    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", Status.NEW, "Описание", Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.create(task);
        manager.create(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getPrioritizedTasks();
        assertEquals(expectedTasks.size(), 2);
    }
}

