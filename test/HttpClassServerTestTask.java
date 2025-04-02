import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.Task;
import ru.yandex.practicum.taskmanager.files.TaskStatus;
import ru.yandex.practicum.taskmanager.files.TaskType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


public class HttpClassServerTestTask extends HttpClassServerTest {
    @Test
    void getTaskByIdStatusOk() {
        Task task = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");

        String taskText = gson.toJson(task);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int taskId = manager.getTasks().get(0).getId();
            task.setId(taskId);

            URI uriTasks = URI.create(HOST + ":" + PORT + "/tasks/" + taskId);
            HttpRequest requestGET = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriTasks)
                    .header("Content-type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(requestGET, HttpResponse.BodyHandlers.ofString());
            int respCode = response.statusCode();
            System.out.println("Ожидается статус 200. Полученный статус ответа: " + respCode);

            assertEquals(200, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void getTaskByIdStatusNotOk() {
        Task task = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");

        String taskText = gson.toJson(task);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int taskId = manager.getTasks().get(0).getId();
            task.setId(taskId);

            URI uriTasks = URI.create(HOST + ":" + PORT + "/tasks/" + (taskId + 1));
            HttpRequest requestGET = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriTasks)
                    .header("Content-type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(requestGET, HttpResponse.BodyHandlers.ofString());
            int respCode = response.statusCode();
            System.out.println("Ожидается статус 404. Полученный статус ответа: " + respCode);

            assertEquals(404, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void getAllTasksStatus() {
        Task task1 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");
        Task task2 = new Task(TaskType.TASK,
                "Test addNewTask2",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 12:00");

        String taskText1 = gson.toJson(task1);
        String taskText2 = gson.toJson(task2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText2))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            URI uriTasks = URI.create(HOST + ":" + PORT + "/tasks");
            HttpRequest requestGET = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriTasks)
                    .header("Content-type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(requestGET, HttpResponse.BodyHandlers.ofString());
            int respCode = response.statusCode();
            System.out.println("Ожидается статус 200. Полученный статус ответа: " + respCode);

            assertEquals(200, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void getAllTasksList() {
        Task task1 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");
        Task task2 = new Task(TaskType.TASK,
                "Test addNewTask2",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 12:00");

        String taskText1 = gson.toJson(task1);
        String taskText2 = gson.toJson(task2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText2))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            HttpRequest requestGET = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(requestGET, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String respBody = response.body()
                    .replaceFirst("\"", "")
                    .replace("\\u003d", "=")
                    .replace("\\u0027", "'")
                    .replace("\\\"", "\"")
                    .replace("\"}]\"", "\"}]");

            assertEquals(manager.getTasks().toString(), respBody, "Список задач не совпадает");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void createTask() {
        Task task = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");

        String taskText = gson.toJson(task);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int respCode = response.statusCode();
            System.out.println("Ожидается статус 201. Полученный статус ответа: " + respCode);

            assertEquals(201, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void createCrossTask() {
        Task task1 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");
        Task task2 = new Task(TaskType.TASK,
                "Test addNewTask2",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:05");

        String taskText1 = gson.toJson(task1);
        String taskText2 = gson.toJson(task2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText2))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            int respCode = response.statusCode();
            System.out.println("Ожидается статус 406. Полученный статус ответа: " + respCode);

            assertEquals(406, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void updateTaskCheckStatus() {
        Task task1 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");
        Task task2 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:30");

        String taskText1 = gson.toJson(task1);
        String taskText2 = gson.toJson(task2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            int taskId = manager.getTasks().get(0).getId();
            task2.setId(taskId);

            URI uriUpd = URI.create(HOST + ":" + PORT + "/tasks" + "/" + taskId);
            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText2))
                    .uri(uriUpd)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> responseUpd = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            int respCode = responseUpd.statusCode();

            System.out.println("Ожидается статус 201. Полученный статус ответа: " + respCode);
            assertEquals(201, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void updateTaskEquals() {
        Task task1 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");
        Task task2 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:20");

        String taskText1 = gson.toJson(task1);
        String taskText2 = gson.toJson(task2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText2))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            int taskId = manager.getTasks().get(0).getId();
            task1.setStartTime("2025-02-22 10:25");
            String taskText1Upd = gson.toJson(task1);

            URI uriUpd = URI.create(HOST + ":" + PORT + "/tasks" + "/" + taskId);
            HttpRequest requestUpd = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText1Upd))
                    .uri(uriUpd)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> responseUpd = httpClient.send(requestUpd, HttpResponse.BodyHandlers.ofString());
            int respCode = responseUpd.statusCode();

            System.out.println("Ожидается статус 406. Полученный статус ответа: " + respCode);
            assertEquals(406, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void deleteTask() {
        Task task = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");

        String taskText = gson.toJson(task);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/tasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(taskText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int taskId = manager.getTasks().get(0).getId();
            task.setId(taskId);

            URI uriUpd = URI.create(HOST + ":" + PORT + "/tasks" + "/" + taskId);
            HttpRequest request2 = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uriUpd)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> responseUpd = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            int respCode = responseUpd.statusCode();

            System.out.println("Ожидается статус 200. Полученный статус ответа: " + respCode);
            assertEquals(200, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }
}
