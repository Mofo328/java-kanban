package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().create();
    TaskManager taskManager = Managers.getDefault();

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        String response;
        int subTaskId;
        try {
            switch (endpoint) {
                case GET_SUBTASKS:
                    response = gson.toJson(taskManager.getAllSubTask());
                    writeResponse(exchange, response, 200);
                    break;
                case GET_SUBTASK:
                    subTaskId = getIdFromPath(exchange);
                    if (subTaskId == -1) {
                        writeResponse(exchange, "Такой задачи нет", 404);
                    }
                    response = gson.toJson(taskManager.getSubTaskById(subTaskId));
                    writeResponse(exchange, response, 200);
                    break;
                case POST_SUBTASK:
                    SubTask subTask;
                    try {
                        subTask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), SubTask.class);
                    } catch (JsonSyntaxException exception) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                        return;
                    }
                    try {
                        if (checkTaskOverlap(subTask)) {
                            taskManager.updateSubTask(subTask);
                            writeResponse(exchange, "Задача обновлена", 200);
                        }
                        writeResponse(exchange, "Задача пересекается с существующей", 406);
                    } catch (NullPointerException exception) {
                        taskManager.createSubTask(subTask);
                        writeResponse(exchange, "Задача добавлена", 200);
                    }
                    break;
                case DELETE_SUBTASK:
                    subTaskId = getIdFromPath(exchange);
                    if (subTaskId == -1) {
                        writeResponse(exchange, "Такой задачи нет", 404);
                    }
                    taskManager.removeSubTaskById(subTaskId);
                    writeResponse(exchange, "задача удалена", 200);
                    break;
                default:
                    writeResponse(exchange, "Введен неверный запрос", 404);
                    break;
            }
        } catch (IOException e) {
            System.out.println();
        }
    }
}
