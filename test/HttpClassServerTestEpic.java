import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpClassServerTestEpic extends HttpClassServerTest {

    @Test
    void getEpicByIdStatusOk() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        String epicText = gson.toJson(epic);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int epicId = manager.getEpics().get(0).getId();
            epic.setId(epicId);

            URI uriEpic = URI.create(HOST + ":" + PORT + "/epics/" + epicId);
            HttpRequest requestGET = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriEpic)
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
    void getEpicByIdStatusNotOk() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        String epicText = gson.toJson(epic);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int epicId = manager.getEpics().get(0).getId();
            epic.setId(epicId);

            URI uriEpic = URI.create(HOST + ":" + PORT + "/epics/" + (epicId + 1));
            HttpRequest requestGET = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriEpic)
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
    void getAllEpicsStatus() {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description");
        Epic epic2 = new Epic("Test addNewEpic2", "Test addNewEpic description");

        String epicText1 = gson.toJson(epic1);
        String epicText2 = gson.toJson(epic2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText2))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            URI uriEpics = URI.create(HOST + ":" + PORT + "/epics");
            HttpRequest requestGET = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriEpics)
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
    void getAllEpicsList() {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description");
        Epic epic2 = new Epic("Test addNewEpic2", "Test addNewEpic description");

        String epicText1 = gson.toJson(epic1);
        String epicText2 = gson.toJson(epic2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText2))
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

            assertEquals(manager.getEpics().toString(), respBody, "Список задач не совпадает");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void createEpic() {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description");

        String epicText1 = gson.toJson(epic1);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
            int respCode = response.statusCode();
            System.out.println("Ожидается статус 201. Полученный статус ответа: " + respCode);

            assertEquals(201, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void createEpicIncorrect() {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description");

        String epicText1 = gson.toJson(epic1);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics" + "/1");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
            int respCode = response.statusCode();
            System.out.println("Ожидается статус 400. Полученный статус ответа: " + respCode);

            assertEquals(400, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void getEpicSubtaskStatus() {
        Epic epic = new Epic(1,
                TaskType.EPIC,
                "Test addNewEpic",
                TaskStatus.NEW,
                "Test addNewEpic description");

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epic.getId());
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 12:00",
                epic.getId());

        String epicText = gson.toJson(epic);
        String subtaskText1 = gson.toJson(subtask1);
        String subtaskText2 = gson.toJson(subtask2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            URI uriSubtasks = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uriSubtasks)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText2))
                    .uri(uriSubtasks)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            URI uriGetSubtasks = URI.create(HOST + ":" + PORT + "/epics/" + epic.getId() + "/subtasks");
            HttpRequest request3 = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriGetSubtasks)
                    .header("Content-type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());

            int respCode = response.statusCode();

            System.out.println("Ожидается статус 200. Полученный статус ответа: " + respCode);
            assertEquals(200, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void getNoEpicSubtaskStatus() {
        Epic epic = new Epic(1,
                TaskType.EPIC,
                "Test addNewEpic",
                TaskStatus.NEW,
                "Test addNewEpic description");

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epic.getId());
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 12:00",
                epic.getId());

        String epicText = gson.toJson(epic);
        String subtaskText1 = gson.toJson(subtask1);
        String subtaskText2 = gson.toJson(subtask2);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            URI uriSubtasks = URI.create(HOST + ":" + PORT + "/subtasks");
            HttpRequest request1 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText1))
                    .uri(uriSubtasks)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

            HttpRequest request2 = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskText2))
                    .uri(uriSubtasks)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            URI uriGetSubtasks = URI.create(HOST + ":" + PORT + "/epics/" + (epic.getId() + 1) + "/subtasks");
            HttpRequest request3 = HttpRequest.newBuilder()
                    .GET()
                    .uri(uriGetSubtasks)
                    .header("Content-type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());

            int respCode = response.statusCode();

            System.out.println("Ожидается статус 404. Полученный статус ответа: " + respCode);
            assertEquals(404, respCode, "Статус ответа не верный");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Test
    void deleteEpic() {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description");

        String epicText1 = gson.toJson(epic1);

        try {
            URI uri = URI.create(HOST + ":" + PORT + "/epics");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(epicText1))
                    .uri(uri)
                    .header("Content-type", "application/json")
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int epicId = manager.getEpics().get(0).getId();
            epic1.setId(epicId);

            URI uriUpd = URI.create(HOST + ":" + PORT + "/epics" + "/" + epicId);
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
