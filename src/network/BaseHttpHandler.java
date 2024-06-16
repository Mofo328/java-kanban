package network;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class BaseHttpHandler {


    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

   private  final TaskManager taskManager = Managers.getDefault();


    protected  Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length < 2 || pathParts[1].isEmpty()) {
            return Endpoint.UNKNOWN;
        }

        String endpointType = pathParts[1];

        switch (endpointType) {
            case "tasks":
                if (pathParts.length == 2) {
                    return Endpoint.GET_TASKS;
                } else if (pathParts.length == 3) {
                    return switch (requestMethod) {
                        case "GET" -> Endpoint.GET_TASK;
                        case "POST" -> Endpoint.POST_TASK;
                        case "DELETE" -> Endpoint.DELETE_TASK;
                        default -> Endpoint.UNKNOWN;
                    };
                }
                break;
            case "epics":
                if (pathParts.length == 2) {
                    return Endpoint.GET_EPICS;
                } else if (pathParts.length == 3) {
                    return switch (requestMethod) {
                        case "GET" -> Endpoint.GET_EPIC;
                        case "POST" -> Endpoint.POST_EPIC;
                        case "DELETE" -> Endpoint.DELETE_EPIC;
                        default -> Endpoint.UNKNOWN;
                    };
                }else if (pathParts.length == 4){
                        return Endpoint.GET_EPIC_SUBTASKS;
                }
                break;
            case "subtasks":
                if (pathParts.length == 2) {
                    return Endpoint.GET_SUBTASKS;
                } else if (pathParts.length == 3) {
                    return switch (requestMethod) {
                        case "GET" -> Endpoint.GET_SUBTASK;
                        case "POST" -> Endpoint.POST_SUBTASK;
                        case "DELETE" -> Endpoint.DELETE_SUBTASK;
                        default -> Endpoint.UNKNOWN;
                    };
                }
                break;
            case "history":
                return Endpoint.HISTORY;
            case "prioritized":
                return Endpoint.PRIORITIZED;
            default:
                return Endpoint.UNKNOWN;
        }

        return Endpoint.UNKNOWN;
    }

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, responseString.length());
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected  Optional<Integer> getId(HttpExchange exchange) {

        String[] parts = exchange.getRequestURI().getPath().split("/");
        if (parts.length >= 2) {
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

    protected  int getIdFromPath(HttpExchange exchange) throws IOException {
        Optional<Integer> idFromPath = getId(exchange);
        return idFromPath.orElse(-1);
    }

    protected  boolean checkTaskOverlap(Task newTask) {
        for (Task existingTask : taskManager.getAll()) {
            if (existingTask.getId() == newTask.getId()) {
                return true;
            }
        }
        return false;
    }
    }

