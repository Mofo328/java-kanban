package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ValidationException;
import model.Epic;
import network.HttpMethod;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            switch (exchange.getRequestMethod()) {
                case HttpMethod.GET:
                    try {
                        String path = exchange.getRequestURI().getPath();
                        int id = getIdFromPath(exchange);
                        if (id != -1) { // Если id из запроса не null
                            if (path.endsWith("/subtasks")) { // Если в пути есть subtasks
                                sendText(exchange, getGson().toJson(taskManager.getAllSubTaskByEpicId(id)), 200);
                            } else {
                                sendText(exchange, getGson().toJson(taskManager.getEpicById(id)), 200);
                            }
                        } else { // Если id из запроса null
                            sendText(exchange, getGson().toJson(taskManager.getAllEpics()), 200);
                        }
                        break;
                    } catch (Exception e) {
                        sendText(exchange, "Ошибка при обработке запроса", 500);
                    }
                case HttpMethod.POST:
                    Epic epic;
                    try {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        epic = getGson().fromJson(body, Epic.class);
                    } catch (JsonSyntaxException exception) {
                        sendText(exchange, "Получен некорректный JSON", 400);
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        if (checkTaskOverlap(epic)) {
                            taskManager.updateEpic(epic);
                            sendText(exchange, "Задача обновлена", 201);
                            break;
                        } else
                            taskManager.createEpic(epic);
                        sendText(exchange, "Задача добавлена", 201);
                        break;
                    } catch (ValidationException e) {
                        sendText(exchange, "Задача пересекается с существующей", 406);
                        break;
                    }
                case HttpMethod.DELETE:
                    int id = getIdFromPath(exchange);
                    if (id != -1) { // Если id из запроса не null
                        taskManager.removeEpicById(getIdFromPath(exchange));
                        sendText(exchange, "задача удалена", 200);
                    } else { // Если id из запроса null
                        taskManager.clearEpic();
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
