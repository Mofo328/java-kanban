package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class PrioritizedHandler extends  BaseHttpHandler implements HttpHandler {
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().create();
    TaskManager taskManager = Managers.getDefault();

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        try {
            switch (endpoint) {
                case PRIORITIZED:
                    String response = gson.toJson(taskManager.getPrioritizedTasks());
                    writeResponse(exchange, response, 200);
                default:
                    writeResponse(exchange, "Введен неверный запрос", 404);
                    break;
            }
        } catch (IOException e) {
            System.out.println();
        }
    }
}
