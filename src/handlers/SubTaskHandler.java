package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ValidationException;
import model.SubTask;
import network.HttpMethod;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class SubTaskHandler extends BaseHttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
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
                            sendText(exchange, getGson().toJson(taskManager.getSubTaskById(id)), 200);
                        } else { // Если id из запроса null
                            sendText(exchange, getGson().toJson(taskManager.getAllSubTask()), 200);
                        }
                        break;
                    } catch (Exception e) {
                        sendText(exchange, "Ошибка при обработке запроса", 500);
                    }
                case HttpMethod.POST:
                    SubTask subTask = null;
                    try {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        subTask = getGson().fromJson(body, SubTask.class);
                    } catch (JsonSyntaxException exception) {
                        sendText(exchange, "Получен некорректный JSON", 400);
                        break;
                    } catch (IOException e) {
                        sendText(exchange, "Internal Server Error", 500);
                    }
                    try {
                        if (checkTaskOverlap(subTask)) {
                            taskManager.updateSubTask(subTask);
                            sendText(exchange, "Задача обновлена", 201);
                            break;
                        } else
                            taskManager.createSubTask(subTask);
                        sendText(exchange, "Задача добавлена", 201);
                        break;
                    } catch (ValidationException e) {
                        sendText(exchange, "Задача пересекается с существующей", 406);
                        break;
                    }
                case HttpMethod.DELETE:
                    int id = getIdFromPath(exchange);
                    if (id != -1) { // Если id из запроса не null
                        taskManager.removeSubTaskById(getIdFromPath(exchange));
                        sendText(exchange, "задача удалена", 200);
                    } else { // Если id из запроса null
                        taskManager.clearSubTasks();
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
