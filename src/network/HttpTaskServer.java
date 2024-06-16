package network;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    public final int PORT = 8088;

     final HttpServer server;

    public HttpTaskServer(){
        try {
            this.server = HttpServer.create(new InetSocketAddress(PORT),0);
            this.server.createContext("/tasks", new TaskHandler());
            this.server.createContext("/epics", new EpicHandler());
            this.server.createContext("/subtasks", new SubTaskHandler());
            this.server.createContext("/history", new HistoryHandler());
            this.server.createContext("/prioritized", new PrioritizedHandler());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("http://localhost:");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

}