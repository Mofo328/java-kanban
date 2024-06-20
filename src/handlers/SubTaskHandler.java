package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ValidationException;
import model.SubTask;
import network.HttpMethod;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            try {
                switch (exchange.getRequestMethod()) {
                    case HttpMethod.GET:
                        if (processRequest(exchange) == 0) {
                            sendText(exchange, getGson().toJson(taskManager.getAllSubTask()), 200);
                            break;
                        } else if (processRequest(exchange) == 1) {
                            sendText(exchange, getGson().toJson(taskManager.getSubTaskById(getIdFromPath(exchange))), 200);
                            break;
                        }
                        if (processRequest(exchange) == 1 && getIdFromPath(exchange) == -1) {
                            sendText(exchange, "Такой задачи нет", 404);
                            break;
                        }
                    case HttpMethod.POST:
                        SubTask subTask;
                        try {
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            subTask = getGson().fromJson(body, SubTask.class);
                        } catch (JsonSyntaxException exception) {
                            sendText(exchange, "Получен некорректный JSON", 400);
                            break;
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
                        if (processRequest(exchange) == 0) {
                            taskManager.clearSubTasks();
                            sendText(exchange, "Задачи удалены", 200);
                            break;
                        } else if (processRequest(exchange) == 1) {
                            if (getIdFromPath(exchange) == -1) {
                                sendText(exchange, "Такой задачи нет", 404);
                            } else
                                taskManager.removeSubTaskById(getIdFromPath(exchange));
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