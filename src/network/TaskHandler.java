package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().create();
    TaskManager taskManager = Managers.getDefault();
    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        int taskId;
        String response;
        try {

            switch (endpoint) {
                case GET_TASKS:
                    response = gson.toJson(taskManager.getAll());
                    writeResponse(exchange, response, 200);
                    break;
                case GET_TASK:
                    taskId = getIdFromPath(exchange);
                    if (taskId == -1) {
                        writeResponse(exchange, "Такой задачи нет", 404);
                    }
                    response = gson.toJson(taskManager.get(taskId));
                    writeResponse(exchange, response, 200);
                    break;
                case POST_TASK:
                    Task task;
                    try {
                        String body = new String(exchange.getRequestBody().readAllBytes(),StandardCharsets.UTF_8);
                        task = gson.fromJson(body,Task.class);
                    } catch (JsonSyntaxException exception) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                        return;
                    }
                        if (!checkTaskOverlap(task)) {
                            taskManager.create(task);
                            writeResponse(exchange, "Задача добавлена", 200);
                            taskManager.update(task);
                            writeResponse(exchange, "Задача обновлена", 200);
                            break;
                        }
                        writeResponse(exchange, "Задача пересекается с существующей", 406);
                    break;
                case DELETE_TASK:
                    taskId = getIdFromPath(exchange);
                    if (taskId == -1) {
                        writeResponse(exchange, "Такой задачи нет", 404);
                    }
                    taskManager.deleteById(taskId);
                    writeResponse(exchange, "задача удалена", 200);
                    break;
                default:
                    writeResponse(exchange,"Введен неверный запрос", 404);
                    break;
            }
        } catch (IOException e) {
            System.out.println();
        }
    }
}
