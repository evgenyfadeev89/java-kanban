package ru.yandex.practicum.taskmanager.httpServer;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.taskmanager.files.*;
import ru.yandex.practicum.taskmanager.manager.TaskManager;
import ru.yandex.practicum.taskmanager.manager.TimeCheckException;

import java.io.IOException;
import java.util.Objects;

public class SubtaskHandler extends BaseHttpHandler {

    SubtaskHandler(TaskManager manager) {
        super(manager);
    }


    protected void subtaskHandler(HttpExchange exchange) throws IOException {

        switch (method) {
            case "GET":
                getSubtaskHandler(exchange);
                break;
            case "POST":
                postSubtaskHandler(exchange);
                break;
            case "DELETE":
                deleteSubtaskHandler(exchange);
                break;
            default:
                System.out.println("Нераспознанный метод: " + method);
                break;
        }
    }


    private void getSubtaskHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            int subtaskId = Integer.parseInt(path.split("/")[2]);

            Subtask subtask = manager.getSubtask(subtaskId);
            if (!Objects.isNull(subtask)) {
                sendText(exchange, subtask.toString(), 200);
            } else {
                sendText(exchange, "Задача не найдена", 404);
            }
        } else {
            sendText(exchange, manager.getSubtasks().toString(), 200);
        }
    }


    private void postSubtaskHandler(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);

        if (requestBody.isEmpty()) {
            sendText(exchange, "Отсутствует текст задачи", 400);
            return;
        }

        Subtask subtask = gson.fromJson(requestBody, Subtask.class);

        if (subtask == null) {
            sendText(exchange, "Неверный формат задачи", 400);
            return;
        }

        if (path.split("/").length > 2) {
            int subtaskId = Integer.parseInt(path.split("/")[2]);
            subtask.setId(subtaskId);
            try {
                manager.updateSubtask(subtask);
                sendText(exchange, "Задача обновлена", 201);
            } catch (TimeCheckException e) {
                sendText(exchange, "Не удалось обновить задачу - пересечение по времени", 406);
            }
        } else {
            try {
                manager.addNewSubtask(subtask);
                if (subtask.getId() > 0) {
                    sendText(exchange, "Задача создана с ID: " + subtask.getId(), 201);
                } else {
                    sendText(exchange, "Не удалось создать задачу", 400);
                }
            } catch (TimeCheckException e) {
                sendText(exchange, "Не удалось обновить задачу - пересечение по времени", 406);
            }
        }
    }


    private void deleteSubtaskHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            int subtaskId = Integer.parseInt(path.split("/")[2]);
            manager.deleteSubtask(subtaskId);
            sendText(exchange, "Задача удалена", 200);
        }
    }
}
