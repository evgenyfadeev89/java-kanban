import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.*;
import ru.yandex.practicum.taskmanager.manager.TaskManager;
import ru.yandex.practicum.taskmanager.manager.TimeCheckException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setTaskManager() {
        taskManager = createTaskManager();
    }

    @Test
    void addNewTaskTest() throws TimeCheckException {
        Task task = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test addNewTaskTest description",
                10,
                "2025-02-22 10:00");
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void findCrossAddTaskTest() {
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

        assertThrows(TimeCheckException.class, () -> {
            taskManager.addNewTask(task2);
        }, "Добавление задачи, пересекающейся по времени не вызвало исключение TimeCheckException");
    }

    @Test
    void findCrossUpdTaskTest() {
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
                "2025-02-22 10:30");
        taskManager.addNewTask(task2);

        task1.setStartTime("2025-02-22 10:35");

        assertThrows(TimeCheckException.class, () -> {
            taskManager.updateTask(task1);
        }, "Добавление задачи, пересекающейся по времени не вызвало исключение TimeCheckException");
    }

    @Test
    void addNewEpicTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpicTest description");
        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void addNewSubTaskTest() throws TimeCheckException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        final int subtask1Id = taskManager.addNewSubtask(subtask1);

        final Subtask savedSubtask = taskManager.getSubtask(subtask1Id);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void findCrossAddSubTaskTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:05",
                epicId);

        assertThrows(TimeCheckException.class, () -> {
            taskManager.addNewSubtask(subtask2);
        }, "Добавление задачи, пересекающейся по времени не вызвало исключение TimeCheckException");
    }

    @Test
    void findCrossUpdSubTaskTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:20",
                epicId);
        taskManager.addNewSubtask(subtask2);

        subtask1.setStartTime("2025-02-22 10:25");

        assertThrows(TimeCheckException.class, () -> {
            taskManager.updateSubtask(subtask1);
        }, "Добавление задачи, пересекающейся по времени не вызвало исключение TimeCheckException");
    }

    @Test
    void controlIdEpicInSubTaskTest() throws IOException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test addNewSubTaskTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        final int subtask1Id = taskManager.addNewSubtask(subtask1);

        assertEquals(epic.getId(), subtask1.getEpicId(), "Задачи не совпадают.");
    }

    @Test
    void getTasksTest() throws TimeCheckException {
        Task task = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test getTasksTest description",
                10,
                "2025-02-22 10:00");
        taskManager.addNewTask(task);
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getEpicsTest() {
        Epic epic = new Epic("Test addNewEpic", "Test getEpicsTest description");
        taskManager.addNewEpic(epic);
        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void getSubtasksTest() throws TimeCheckException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test getSubtasksTest description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);
        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateEpicStatusNewTest() throws TimeCheckException {
        Epic epic = new Epic("Test addNewEpic", "Test updateEpicStatusNewTest");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test updateEpicStatus description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test updateEpicStatus description2",
                10,
                "2025-02-22 10:30",
                epicId);
        taskManager.addNewSubtask(subtask2);
        final TaskStatus epicStatus = TaskStatus.NEW;

        assertEquals(epicStatus, epic.getStatus(), "Статусы эпиков не совпадает.");
        assertEquals(epicStatus, epic.getStatus(), "Статусы эпиков совпадает.");
    }

    @Test
    void updateEpicStatusDone() throws TimeCheckException {
        Epic epic = new Epic("Test addNewEpic", "Test updateEpicStatusDone");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.DONE,
                "Test updateEpicStatus description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.DONE,
                "Test updateEpicStatus description2",
                10,
                "2025-02-22 10:30",
                epicId);
        taskManager.addNewSubtask(subtask2);
        final TaskStatus epicStatus = TaskStatus.DONE;

        assertEquals(epicStatus, epic.getStatus(), "Статусы эпиков не совпадает.");
        assertEquals(epicStatus, epic.getStatus(), "Статусы эпиков совпадает.");
    }

    @Test
    void updateEpicStatusNewDone() throws TimeCheckException {
        Epic epic = new Epic("Test addNewEpic", "Test updateEpicStatusNewDone");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test updateEpicStatus description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.DONE,
                "Test updateEpicStatus description2",
                10,
                "2025-02-22 10:30",
                epicId);
        taskManager.addNewSubtask(subtask2);
        final TaskStatus epicStatus = TaskStatus.IN_PROGRESS;

        assertEquals(epicStatus, epic.getStatus(), "Статусы эпиков не совпадает.");
        assertEquals(epicStatus, epic.getStatus(), "Статусы эпиков совпадает.");
    }

    @Test
    void updateEpicStatusInProgress() throws TimeCheckException {
        Epic epic = new Epic("Test addNewEpic", "Test updateEpicStatusInProgress");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.IN_PROGRESS,
                "Test updateEpicStatus description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.IN_PROGRESS,
                "Test updateEpicStatus description2",
                10,
                "2025-02-22 10:30",
                epicId);
        taskManager.addNewSubtask(subtask2);
        final TaskStatus epicStatus = TaskStatus.IN_PROGRESS;

        assertEquals(epicStatus, epic.getStatus(), "Статусы эпиков не совпадает.");
    }

    @Test
    void updateEpicTimeFieldsTest() throws TimeCheckException {
        Epic epic = new Epic("Test addNewEpic", "Test updateEpicTimeFieldsTest");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test updateEpicStatus description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(TaskType.SUBTASK,
                "Test subtask2",
                TaskStatus.NEW,
                "Test updateEpicStatus description2",
                10,
                "2025-02-22 10:30",
                epicId);
        taskManager.addNewSubtask(subtask2);
        final String epicStartTime = subtask1.getStartTime();
        final String epicEndTime = subtask2.getEndTime();

        assertEquals(epicStartTime, epic.getStartTime(), "Время начала эпика неверное.");
        assertEquals(epicEndTime, epic.getEndTime(), "Время окончания эпика неверное.");
    }

    @Test
    void deleteTasksTest() {
        Task task = new Task(TaskType.TASK,
                "Test addNewTask1",
                TaskStatus.NEW,
                "Test deleteTasksTest description",
                10,
                "2025-02-22 10:00");
        taskManager.addNewTask(task);
        taskManager.deleteTasks();
        final ArrayList<Task> deleteTasks = new ArrayList<>();

        assertEquals(deleteTasks, taskManager.getTasks(), "Удаление tasks работает некорректно");
    }

    @Test
    void deleteEpicsTest() {
        Epic epic = new Epic("Test addNewEpic", "Test updateEpicTimeFieldsTest");
        taskManager.addNewEpic(epic);
        taskManager.deleteEpics();
        final ArrayList<Epic> deleteEpics = new ArrayList<>();

        assertEquals(deleteEpics, taskManager.getTasks(), "Удаление epiks работает некорректно");
    }

    @Test
    void deleteSubtaskTest() {
        Epic epic = new Epic("Test addNewEpic", "Test updateEpicTimeFieldsTest");
        int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask(TaskType.SUBTASK,
                "Test subtask1",
                TaskStatus.NEW,
                "Test updateEpicStatus description1",
                10,
                "2025-02-22 10:00",
                epicId);
        taskManager.addNewSubtask(subtask1);
        taskManager.deleteSubtasks();
        final ArrayList<Subtask> deleteSubtask = new ArrayList<>();

        assertEquals(deleteSubtask, taskManager.getSubtasks(), "Удаление Subtasks работает некорректно");
    }
}
