package ru.yandex.practicum.taskmanager.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.taskmanager.manager.*;
import ru.yandex.practicum.taskmanager.parseAdapter.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;

    BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    protected String method;
    protected String path;
    protected String type;
    protected String response;
    protected int id;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    public void handle(HttpExchange exchange) {
        // Получаем информацию о запросе
        try {
            System.out.println("Обработка запроса");
            path = exchange.getRequestURI().getPath();
            method = exchange.getRequestMethod();
            type = path.split("/")[1];

            switch (type) {
                case "tasks" -> taskHandler(exchange);
                case "subtasks" -> subtaskHandler(exchange);
                case "epics" -> epicHandler(exchange);
                case "history" -> historyHandler(exchange);
                case "prioritized" -> prioritizedHandler(exchange);
                default -> System.out.println("Нераспознанный тип задачи: " + type);
            }
        } catch (Exception e) {
            System.out.println("Возникла ошибка:" + e.getMessage());
        } finally {
            exchange.close();
        }
    }


    protected void taskHandler(HttpExchange exchange) throws IOException {
    }


    protected void subtaskHandler(HttpExchange exchange) throws IOException {
    }


    protected void epicHandler(HttpExchange exchange) throws IOException {
    }


    protected void historyHandler(HttpExchange exchange) throws IOException {
    }


    protected void prioritizedHandler(HttpExchange exchange) throws IOException {
    }


    //для отправки общего ответа в случае успеха пользователю
    protected void sendText(HttpExchange exchange, String response, int code) throws IOException {

        String serializedTask = gson.toJson(response);
        byte[] resp = serializedTask.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");

        // Устанавливаем заголовки ответа
        exchange.sendResponseHeaders(code, resp.length);

        // Отправляем тело ответа
        exchange.getResponseBody().write(resp);
        exchange.close();
    }


    //читает данные, переданные в сервер от пользователя
    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
    }
}
