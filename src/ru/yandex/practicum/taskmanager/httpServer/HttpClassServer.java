package ru.yandex.practicum.taskmanager.httpServer;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.taskmanager.manager.*;

import java.net.InetSocketAddress;

public class HttpClassServer {
    private static final int PORT = 8080;
    private static final String hostName = "localhost";
    public final TaskManager manager;
    private HttpServer httpServer;


    public HttpClassServer(TaskManager manager) {
        this.manager = manager;
        try {
            System.out.println("Создание и запуск http сервера");
            httpServer = HttpServer.create(new InetSocketAddress(hostName, PORT), 0);
            httpServer.createContext("/tasks", new TaskHandler(manager));
            httpServer.createContext("/subtasks", new SubtaskHandler(manager));
            httpServer.createContext("/epics", new EpicHandler(manager));
            httpServer.createContext("/history", new HistoryHandler(manager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        } catch (Exception e) {
            System.out.println("Ошибка запуска HTTP-сервера: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        System.out.println("Тест!");

        final HttpClassServer server = new HttpClassServer(Managers.getDefault());
        server.start();
        server.stop();
    }


    public void start() {
        this.httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }


    public void stop() {
        System.out.println("HTTP-сервер остановлен");
        this.httpServer.stop(0);
    }
}

