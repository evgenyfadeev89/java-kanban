import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.Task;
import ru.yandex.practicum.taskmanager.manager.FileBackedTaskManager;
import ru.yandex.practicum.taskmanager.manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    @Test
    void FileBackedTaskManager() throws IOException {
        Path testManager = Files.createTempFile("testManager", ".csv");
        File testFile = new File(testManager.toUri());
        final TaskManager taskManager = new FileBackedTaskManager(testFile);
        List<Task> histToControl = new ArrayList<>();

        Task task1 = new Task("Test addNewTask1", "Test addNewTask1 description");
        final int taskId1 = taskManager.addNewTask(task1);
        histToControl.add(0,taskManager.getTask(taskId1));

        Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description");
        final int taskId2 = taskManager.addNewTask(task2);
        histToControl.add(1,taskManager.getTask(taskId2));

        final TaskManager fileBackedTaskManagerTestAfterLoad = FileBackedTaskManager.loadFromFile(testFile);
        List<Task> hist = fileBackedTaskManagerTestAfterLoad.getTasks();

        /*Проверка работы загрузки из файла*/
        assertEquals(histToControl, hist, "Списки в истории не совпадают.");
        assertEquals(histToControl, hist, "Списки в истории совпадают.");
    }
}
