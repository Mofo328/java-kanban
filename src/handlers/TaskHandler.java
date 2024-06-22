package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ValidationException;
import model.Task;
import network.HttpMethod;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            switch (exchange.getRequestMethod()) {
                case HttpMethod.GET:
                    try {
                        int id = getIdFromPath(exchange);
                        if (id != -1) { // Если id из запроса не null
                            sendText(exchange, getGson().toJson(taskManager.get(id)), 200);
                        } else { // Если id из запроса null
                            sendText(exchange, getGson().toJson(taskManager.getAll()), 200);
                        }
                        break;
                    } catch (Exception e) {
                        sendText(exchange, "Ошибка при обработке запроса", 500);
                    }
                case HttpMethod.POST:
                    Task task;
                    try {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        task = getGson().fromJson(body, Task.class);
                    } catch (JsonSyntaxException exception) {
                        sendText(exchange, "Получен некорректный JSON", 400);
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        if (checkTaskOverlap(task)) {
                            taskManager.update(task);
                            sendText(exchange, "Задача обновлена", 201);
                            break;
                        } else
                            taskManager.create(task);
                        sendText(exchange, "Задача добавлена", 201);
                        break;
                    } catch (ValidationException e) {
                        sendText(exchange, "Задача пересекается с существующей", 406);
                        break;
                    }
                case HttpMethod.DELETE:
                    int id = getIdFromPath(exchange);
                    if (id != -1) { // Если id из запроса не null
                        taskManager.deleteById(getIdFromPath(exchange));
                        sendText(exchange, "задача удалена", 200);
                    } else { // Если id из запроса null
                        taskManager.clear();
                        sendText(exchange, "Задачи удалены", 200);
                    }
                    break;
                default:
                    sendText(exchange, "Введен неверный запрос", 404);
                    break;
            }
        }
    }
}
