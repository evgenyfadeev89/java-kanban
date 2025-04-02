package ru.yandex.practicum.taskmanager.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.taskmanager.manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    HistoryHandler(TaskManager manager) {
        super(manager);
    }

    protected void historyHandler(HttpExchange exchange) throws IOException {

        switch (method) {
            case "GET":
                getHistoryHandler(exchange);
                break;
            default:
                System.out.println("Нераспознанный метод: " + method);
                break;
        }
    }


    private void getHistoryHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            sendText(exchange, "Неверный запрос для вывода истории", 400);
        } else {
            sendText(exchange, manager.getHistory().toString(), 200);
        }
    }
}
