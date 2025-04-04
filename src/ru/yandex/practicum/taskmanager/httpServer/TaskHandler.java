package ru.yandex.practicum.taskmanager.httpServer;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.taskmanager.files.*;
import ru.yandex.practicum.taskmanager.manager.TaskManager;
import ru.yandex.practicum.taskmanager.manager.TimeCheckException;

import java.io.IOException;
import java.util.Objects;


class TaskHandler extends BaseHttpHandler {

    TaskHandler(TaskManager manager) {
        super(manager);
    }


    protected void taskHandler(HttpExchange exchange) throws IOException {

        switch (method) {
            case "GET":
                getTaskHandler(exchange);
                break;
            case "POST":
                postTaskHandler(exchange);
                break;
            case "DELETE":
                deleteTaskHandler(exchange);
                break;
            default:
                System.out.println("Нераспознанный метод: " + method);
                break;
        }
    }


    private void getTaskHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            int taskId = Integer.parseInt(path.split("/")[2]);

            Task task = manager.getTask(taskId);
            if (!Objects.isNull(task)) {
                sendText(exchange, task.toString(), 200);
            } else {
                sendText(exchange, "Задача не найдена", 404);
            }
        } else {
            sendText(exchange, manager.getTasks().toString(), 200);
        }
    }


    private void postTaskHandler(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);

        if (requestBody.isEmpty()) {
            sendText(exchange, "Отсутствует текст задачи", 400);
            return;
        }

        Task task = gson.fromJson(requestBody, Task.class);


        if (task == null) {
            sendText(exchange, "Неверный формат задачи", 400);
            return;
        }

        if (path.split("/").length > 2) {
            int taskId = Integer.parseInt(path.split("/")[2]);
            task.setId(taskId);
            try {
                manager.updateTask(task);
                sendText(exchange, "Задача обновлена", 201);
            } catch (TimeCheckException e) {
                sendText(exchange, "Не удалось обновить задачу - пересечение по времени", 406);
            }
        } else {
            try {
                manager.addNewTask(task);
                if (task.getId() > 0) {
                    sendText(exchange, "Задача создана с ID: " + task.getId(), 201);
                } else {
                    sendText(exchange, "Не удалось создать задачу", 400);
                }
            } catch (TimeCheckException e) {
                sendText(exchange, "Не удалось обновить задачу - пересечение по времени", 406);
            }
        }
    }


    private void deleteTaskHandler(HttpExchange exchange) throws IOException {
        if (path.split("/").length > 2) {
            int taskId = Integer.parseInt(path.split("/")[2]);
            manager.deleteTask(taskId);
            sendText(exchange, "Задача удалена", 200);
        }
    }
}
