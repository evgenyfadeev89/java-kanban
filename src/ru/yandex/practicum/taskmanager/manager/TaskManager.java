package ru.yandex.practicum.taskmanager.manager;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.practicum.taskmanager.files.*;

public interface TaskManager {
    ArrayList<Task> getTasks();
    ArrayList<Epic> getEpics();
    ArrayList<Subtask> getSubtasks();
    ArrayList<Subtask> getEpicSubtasks (int epicId);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);

    void updateEpicStatus(Subtask subtask);

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

    void printAllTasks(TaskManager manager);
}
