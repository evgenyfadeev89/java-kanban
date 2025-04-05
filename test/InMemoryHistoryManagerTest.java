import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.*;
import ru.yandex.practicum.taskmanager.manager.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends InMemoryHistoryManager {

    protected HistoryManager taskManager;

    @BeforeEach
    public void setTaskManager() {
        taskManager = new InMemoryHistoryManager();
    }

    Task task1 = new Task(1,
            TaskType.TASK,
            "Test addNewTask1",
            TaskStatus.NEW,
            "Test addNewTask1Test description",
            10,
            "2025-02-22 10:00"
            );

    Task task2 = new Task(2,
            TaskType.TASK,
            "Test addNewTask2",
            TaskStatus.NEW,
            "Test addNewTask2Test description",
            10,
            "2025-02-22 10:15");

    Task task3 = new Task(3,
            TaskType.TASK,
            "Test addNewTask3",
            TaskStatus.NEW,
            "Test addNewTask3Test description",
            10,
            "2025-02-22 10:30");

    ArrayList<Task> viewTasks = new ArrayList<>();

    @Test
    void getHistoryTest() {
        assertNotNull(taskManager.getHistory(), "История задач не пустая");
        assertEquals(viewTasks, taskManager.getHistory(), "История не совпадает.");
    }

    @Test
    void addTest() {
        taskManager.add(task1);
        viewTasks.add(0, task1);

        assertEquals(viewTasks, taskManager.getHistory(), "История не совпадает.");
    }

    @Test
    void doublicateTest() {
        taskManager.add(task1);
        taskManager.add(task1);
        viewTasks.add(0, task1);

        assertEquals(viewTasks, taskManager.getHistory(), "История не совпадает.");
    }

    @Test
    void removeLastTest() {
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);
        taskManager.remove(1);
        viewTasks.add(0, task2);
        viewTasks.add(0, task3);

        assertEquals(viewTasks, taskManager.getHistory(), "История не совпадает.");
    }

    @Test
    void removeMiddleTest() {
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);
        taskManager.remove(2);
        viewTasks.add(0, task1);
        viewTasks.add(0, task3);

        assertEquals(viewTasks, taskManager.getHistory(), "История не совпадает.");
    }

    @Test
    void removeFirstTest() {
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);
        taskManager.remove(3);
        viewTasks.add(0, task1);
        viewTasks.add(0, task2);

        assertEquals(viewTasks, taskManager.getHistory(), "История не совпадает.");
    }
}
