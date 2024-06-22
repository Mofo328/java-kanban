package handlers;


import com.sun.net.httpserver.HttpExchange;
import network.HttpMethod;
import service.TaskManager;


public class HistoryHandler extends BaseHttpHandler {


    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            if (exchange.getRequestMethod().equals(HttpMethod.GET)) {
                sendText(exchange, getGson().toJson(taskManager.getHistory()), 200);
            } else
                sendText(exchange, "Запрос не может быть обработан", 404);
        }
    }
}