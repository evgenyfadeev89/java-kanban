import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.*;
import ru.yandex.practicum.taskmanager.manager.*;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest {
    @Override
    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void findCrossTaskTest() {
        Task task1 = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");
        taskManager.addNewTask(task1);
        Task task2 = new Task(TaskType.TASK,
                "Test addNewTask2",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:05");

        boolean crossFlag = true;

        assertEquals(crossFlag, taskManager.findCrossTask(task2), "Задачи пересекаются.");
    }
}