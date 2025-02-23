package ru.yandex.practicum.taskmanager.manager;

import java.util.List;
import java.util.Set;

import ru.yandex.practicum.taskmanager.files.*;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Subtask> getEpicSubtasks(int epicId);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    int addNewTask(Task task) throws TimeCheckException;

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask) throws TimeCheckException;

    void updateEpicStatus(Subtask subtask);

    void updateEpicTimeFields(int epicId);

    void updateTask(Task task); //next

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask); //?

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();

    boolean findCrossTask(Task task);

    void printAllTasks(TaskManager manager);

    void printPrioritizedTasks();
}
