package network;

import com.sun.net.httpserver.HttpServer;
import handlers.*;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private final static int PORT = 8080;
    private final TaskManager taskManager;

    private HttpServer server;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.start();
            System.out.println("Сервер запущен " + PORT);
            server.createContext("/tasks", new TaskHandler(taskManager));
            server.createContext("/epics", new EpicHandler(taskManager));
            server.createContext("/subtasks", new SubTaskHandler(taskManager));
            server.createContext("/history", new HistoryHandler(taskManager));
            server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен " + PORT);
    }
}