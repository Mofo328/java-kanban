package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


public abstract class BaseHttpHandler implements HttpHandler {


    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange exchange, String responseString, int rCode) {
        try (OutputStream os = exchange.getResponseBody()) {
            var responseBytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(rCode, responseBytes.length);
            os.write(responseBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        exchange.close();
    }

    protected Optional<Integer> getId(HttpExchange exchange) {

        String[] parts = exchange.getRequestURI().getPath().split("/");
        if (parts.length >= 3) {
            String postIdStr = parts[2];
            try {
                int postId = Integer.parseInt(postIdStr);
                return Optional.of(postId);
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    protected int getIdFromPath(HttpExchange exchange) {
        Optional<Integer> idFromPath = getId(exchange);
        return idFromPath.orElse(-1);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting();
        return gsonBuilder.create();
    }
}