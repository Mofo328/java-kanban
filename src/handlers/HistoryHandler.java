package handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import network.HttpMethod;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {


    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            try {
                if (exchange.getRequestMethod().equals(HttpMethod.GET)) {
                    sendText(exchange, getGson().toJson(taskManager.getHistory()), 200);
                } else
                    sendText(exchange, "Запрос не может быть обработан", 404);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}