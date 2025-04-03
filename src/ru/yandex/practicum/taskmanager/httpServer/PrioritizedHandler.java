package ru.yandex.practicum.taskmanager.httpServer;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.taskmanager.manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    PrioritizedHandler(TaskManager manager) {
        super(manager);
    }


    protected void prioritizedHandler(HttpExchange exchange) throws IOException {

        switch (method) {
            case "GET":
                getPrioritizedHandler(exchange);
                break;
            default:
                System.out.println("Нераспознанный метод: " + method);
                break;
        }
    }


    private void getPrioritizedHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            sendText(exchange, "Неверный запрос для вывода истории", 400);
        } else {
            sendText(exchange, manager.getPrioritizedTasks().toString(), 200);
        }
    }
}
