package handlers;

import com.sun.net.httpserver.HttpExchange;
import network.HttpMethod;
import service.TaskManager;

public class PrioritizedHandler extends BaseHttpHandler {


    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            if (exchange.getRequestMethod().equals(HttpMethod.GET)) {
                sendText(exchange, getGson().toJson(taskManager.getPrioritizedTasks()), 200);
            } else
                sendText(exchange, "Запрос не может быть обработан", 404);
        }
    }
}