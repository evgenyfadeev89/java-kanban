package ru.yandex.practicum.taskmanager.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.taskmanager.files.*;
import ru.yandex.practicum.taskmanager.manager.TaskManager;

import java.io.IOException;
import java.util.Objects;

public class EpicHandler extends BaseHttpHandler {
    EpicHandler(TaskManager manager) {
        super(manager);
    }


    protected void epicHandler(HttpExchange exchange) throws IOException {

        switch (method) {
            case "GET":
                getEpicHandler(exchange);
                break;
            case "POST":
                postEpicHandler(exchange);
                break;
            case "DELETE":
                deleteEpicHandler(exchange);
                break;
            default:
                System.out.println("Нераспознанный метод: " + method);
                break;
        }
    }


    private void getEpicHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            int epicId = Integer.parseInt(path.split("/")[2]);

            Epic epic = manager.getEpic(epicId);

            if (!Objects.isNull(epic)) {
                if (path.split("/").length > 3 && path.split("/")[3].equals("subtasks")) {
                    sendText(exchange, manager.getEpicSubtasks(epicId).toString(), 200);
                } else if (path.split("/").length == 3) {
                    sendText(exchange, epic.toString(), 200);
                }
            } else {
                sendText(exchange, "Задача не найдена", 404);
            }
        } else {
            sendText(exchange, manager.getEpics().toString(), 200);
        }
    }


    private void postEpicHandler(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);

        if (requestBody.isEmpty()) {
            sendText(exchange, "Отсутствует текст задачи", 400);
            return;
        }

        Epic epic = gson.fromJson(requestBody, Epic.class);

        if (epic == null) {
            sendText(exchange, "Неверный формат задачи", 400);
            return;
        }

        if (path.split("/").length > 2) {
            sendText(exchange, "Неверный запрос", 400);
        } else {
            try {
                manager.addNewEpic(epic);
                if (epic.getId() > 0) {
                    sendText(exchange, "Задача создана с ID: " + epic.getId(), 201);
                } else {
                    sendText(exchange, "Не удалось создать задачу", 400);
                }
            } catch (Exception e) {
                sendText(exchange, "Неверный запрос", 400);
            }
        }
    }


    private void deleteEpicHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            int epicId = Integer.parseInt(path.split("/")[2]);
            manager.deleteEpic(epicId);
            sendText(exchange, "Задача удалена", 200);
        }
    }
}
