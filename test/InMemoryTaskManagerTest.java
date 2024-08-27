package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.files.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest {

    final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void addNewSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", epicId);
        final int subtaskId = taskManager.addNewSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void HistoryManagerTest () {
        final TaskManager taskManagerStatus = new InMemoryTaskManager();
        List<Task> histToControl = new ArrayList<>();

        Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = taskManagerStatus.addNewTask(task);

        Task task2 =  taskManagerStatus.getTask(taskId);
        task2.setStatus(TaskStatus.DONE);
        taskManagerStatus.updateTask(task2);
        histToControl.add(taskManagerStatus.getTask(task2.getId()));

        /*Проверка обновления статуса таски*/
        assertEquals(task, task2, "Задачи не совпадают.");
        assertEquals(task, task2, "Задачи совпадают.");

        Task task3 = new Task("Test addNewTask3", "Test addNewTask3 description");
        final int taskId3 = taskManagerStatus.addNewTask(task3);
        taskManagerStatus.getTask(taskId3);
        histToControl.add(taskManagerStatus.getTask(taskId3));

        /*Проверка работы истории до удаления таски*/
        List<Task> histBeforeDel = taskManagerStatus.getHistory();
        assertNotNull(histBeforeDel, "Задачи не возвращаются.");
        assertEquals(histToControl, histBeforeDel, "Списки в истории не совпадают.");
        assertEquals(histToControl, histBeforeDel, "Списки в истории совпадают.");

        /*Проверка работы истории после удаления таски*/
        taskManagerStatus.deleteTask(taskId);
        List<Task> hist = taskManagerStatus.getHistory();
        histToControl.remove(task);
        assertEquals(histToControl, hist, "Списки в истории не совпадают.");
        assertEquals(histToControl, hist, "Списки в истории совпадают.");

    }
}