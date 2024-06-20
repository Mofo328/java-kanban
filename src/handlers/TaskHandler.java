package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ValidationException;
import model.Task;
import network.HttpMethod;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            try {
                switch (exchange.getRequestMethod()) {
                    case HttpMethod.GET:
                        if (processRequest(exchange) == 0) {
                            sendText(exchange, getGson().toJson(taskManager.getAll()), 200);
                            break;
                        } else if (processRequest(exchange) == 1) {
                            sendText(exchange, getGson().toJson(taskManager.get(getIdFromPath(exchange))), 200);
                            break;
                        }
                        if (processRequest(exchange) == 1 && getIdFromPath(exchange) == -1) {
                            sendText(exchange, "Такой задачи нет", 404);
                            break;
                        }
                    case HttpMethod.POST:
                        Task task;
                        try {
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            task = getGson().fromJson(body, Task.class);
                        } catch (JsonSyntaxException exception) {
                            sendText(exchange, "Получен некорректный JSON", 400);
                            break;
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
                        if (processRequest(exchange) == 0) {
                            taskManager.clear();
                            sendText(exchange, "Задачи удалены", 200);
                            break;
                        } else if (processRequest(exchange) == 1) {
                            if (getIdFromPath(exchange) == -1) {
                                sendText(exchange, "Такой задачи нет", 404);
                            } else
                                taskManager.deleteById(getIdFromPath(exchange));
                            sendText(exchange, "задача удалена", 200);
                            break;
                        }
                    default:
                        sendText(exchange, "Введен неверный запрос", 404);
                        break;
                }
            } catch (IOException e) {
                System.out.println();
            }
        }
    }
}