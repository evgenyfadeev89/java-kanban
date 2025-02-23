import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.Task;
import ru.yandex.practicum.taskmanager.files.TaskStatus;
import ru.yandex.practicum.taskmanager.files.TaskType;
import ru.yandex.practicum.taskmanager.manager.FileBackedTaskManager;
import ru.yandex.practicum.taskmanager.manager.TaskManager;
import ru.yandex.practicum.taskmanager.manager.TimeCheckException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {
    Path testManager = Files.createTempFile("testManager", ".csv");
    File testFile = new File(testManager.toUri());

    FileBackedTaskManagerTest() throws IOException {
    }

    @Override
    protected TaskManager createTaskManager() {
        return new FileBackedTaskManager(testFile);
    }

    @Test
    void fileBackedHistTest() throws TimeCheckException {

        List<Task> histToControl = new ArrayList<>();

        Task task1 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTask1Test description",
                10, "2025-02-22 10:00");
        final int task1Id = taskManager.addNewTask(task1);
        histToControl.add(0, taskManager.getTask(task1Id));

        Task task2 = new Task(TaskType.TASK,
                "Test addNewTask2",
                TaskStatus.NEW,
                "Test addNewTask2Test description",
                10,
                "2025-02-22 10:20");
        final int task2Id = taskManager.addNewTask(task2);
        histToControl.add(1, taskManager.getTask(task2Id));

        final TaskManager fileBackedTaskManagerTestAfterLoad = FileBackedTaskManager.loadFromFile(testFile);
        List<Task> hist = fileBackedTaskManagerTestAfterLoad.getTasks();

        assertEquals(histToControl, hist, "Списки в истории не совпадают.");
    }

    @Test
    public void fileNotFoundExceptionTest() {
        assertThrows(FileNotFoundException.class, () -> {
            new FileInputStream("non_existent_file.txt");
        }, "Чтение несуществующего файла должно вызывать FileNotFoundException");
    }

    @Test
    public void fileReadSuccessTest() {
        assertDoesNotThrow(() -> {
            new FileInputStream(testFile);
        }, "Чтение существующего файла не должно вызывать исключений");
    }
}
