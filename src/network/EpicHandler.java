package network;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import service.Managers;
import service.TaskManager;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().create();
    TaskManager taskManager = Managers.getDefault();

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        String response;
        int epicId;
        try {
            switch (endpoint) {
                case GET_EPICS:
                    response = gson.toJson(taskManager.getAllEpics());
                    writeResponse(exchange, response, 200);
                case GET_EPIC:
                    epicId = getIdFromPath(exchange);
                    if (epicId == -1) {
                        writeResponse(exchange, "Такой задачи нет", 404);
                    }
                    response = gson.toJson(taskManager.getEpicById(epicId));
                    writeResponse(exchange, response, 200);
                    break;
                case GET_EPIC_SUBTASKS:
                    epicId = getIdFromPath(exchange);
                    if (epicId == -1) {
                        writeResponse(exchange, "Такой задачи нет", 404);
                    }
                    response = gson.toJson(taskManager.getSubTaskById(epicId));
                    writeResponse(exchange, response, 200);
                case POST_EPIC:
                    Epic epic;
                    try {
                        epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class);
                    } catch (JsonSyntaxException exception) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                        return;
                    }
                    try {
                        if (checkTaskOverlap(epic)) {
                            taskManager.updateEpic(epic);
                            writeResponse(exchange, "Задача обновлена", 200);
                        }
                        writeResponse(exchange, "Задача пересекается с существующей", 406);
                    } catch (NullPointerException exception) {
                        taskManager.createEpic(epic);
                        writeResponse(exchange, "Задача добавлена", 200);
                    }
                    break;
                case DELETE_EPIC:
                    epicId = getIdFromPath(exchange);
                    if (epicId == -1) {
                        writeResponse(exchange, "Такой задачи нет", 404);
                    }
                    taskManager.removeEpicById(epicId);
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
