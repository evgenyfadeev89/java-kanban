import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpClassServerTestSubtask extends HttpClassServerTest {
    @Test
    void getSubtaskByIdStatusOk() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);

        String subtaskText = gson.toJson(subtask1);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int subtask1Id = manager.getSubtasks().get(0).getId();
            subtask1.setId(subtask1Id);

            URI uriTasks = URI.create(HOST + ":" + PORT + "/subtasks/" + subtask1Id);
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
    void getSubtaskByIdStatusNotOk() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);

        String subtaskText = gson.toJson(subtask1);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int subtask1Id = manager.getSubtasks().get(0).getId();
            subtask1.setId(subtask1Id);

            URI uriTasks = URI.create(HOST + ":" + PORT + "/subtasks/" + (subtask1Id + 1));
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
    void getAllSubtasksStatus() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 12:00",
                epicId);

        String subtaskText1 = gson.toJson(subtask1);
        String subtaskText2 = gson.toJson(subtask2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText2))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            URI uriTasks = URI.create(HOST + ":" + PORT + "/subtasks");
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
    void getAllSubtasksList() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 12:00",
                epicId);

        String subtaskText1 = gson.toJson(subtask1);
        String subtaskText2 = gson.toJson(subtask2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText2))
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
                    .replace("]\"", "]");

            assertEquals(manager.getSubtasks().toString(), respBody, "Список задач не совпадает");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void createSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);

        String subtaskText1 = gson.toJson(subtask1);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
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
    void createCrossSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:05",
                epicId);

        String subtaskText1 = gson.toJson(subtask1);
        String subtaskText2 = gson.toJson(subtask2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText2))
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
    void updateSubtaskCheckStatus() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 12:00",
                epicId);

        String subtaskText1 = gson.toJson(subtask1);
        String subtaskText2 = gson.toJson(subtask2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            int subtaskId = manager.getSubtasks().get(0).getId();
            subtask2.setId(subtaskId);

            URI uriUpd = URI.create(HOST + ":" + PORT + "/subtasks" + "/" + subtaskId);
            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText2))
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
    void updateSubtaskEquals() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:20",
                epicId);

        String subtaskText1 = gson.toJson(subtask1);
        String subtaskText2 = gson.toJson(subtask2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText2))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            int subtaskId = manager.getSubtasks().get(0).getId();
            subtask1.setStartTime("2025-02-22 10:25");
            String subtaskText1Upd = gson.toJson(subtask1);

            URI uriUpd = URI.create(HOST + ":" + PORT + "/subtasks" + "/" + subtaskId);
            HttpRequest requestUpd = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1Upd))
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
    void deleteSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);

        String subtaskText1 = gson.toJson(subtask1);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int subtaskId = manager.getSubtasks().get(0).getId();
            subtask1.setId(subtaskId);

            URI uriUpd = URI.create(HOST + ":" + PORT + "/subtasks" + "/" + subtaskId);
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
