package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.manager.*;
import ru.yandex.practicum.taskmanager.files.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Тест!");

        TaskManager manager = new InMemoryTaskManager();
        HistoryManager histManage = Managers.getDefaultHistory();


        //создание
        Task task1 = new Task("Task_1", "Task_1 description");
        Task task2 = new Task("Task_2", "Task_2 description");
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Epic_1", "Epic_1 description");
        Epic epic2 = new Epic("Epic_2", "Epic_2 description");
        Epic epic8 = new Epic("Epic_2", "Epic_2 description");
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);
        final int epicId8 = manager.addNewEpic(epic8);

        Subtask subtask1 = new Subtask("Subtask_1-1", "Subtask_1 description", epicId1);
        Subtask subtask2 = new Subtask("Subtask_2-1", "Subtask_1 description", epicId1);
        Subtask subtask3 = new Subtask("Subtask_3-2", "Subtask_1 description", epicId2);
        final Integer subtaskId1 = manager.addNewSubtask(subtask1);
        final Integer subtaskId2 = manager.addNewSubtask(subtask2);
        final Integer subtaskId3 = manager.addNewSubtask(subtask3);


        //обновление
        final Task task = manager.getTask(taskId2);
        task.setStatus(TaskStatus.DONE);
        manager.updateTask(task);
        System.out.println("Change status: Task2 NEW -> DONE");
        System.out.println("Задачи:");
        for(Task t: manager.getTasks()) {
            System.out.println(t);
        }

        Subtask subtask = manager.getSubtask(subtaskId2);
        subtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);
        System.out.println("Change status: Subtask6 NEW -> DONE");
        subtask = manager.getSubtask(subtaskId3);
        subtask.setStatus(TaskStatus.NEW);
        manager.updateSubtask(subtask);
        System.out.println("Change status: Subtask7 DONE -> NEW");


        //Удаление
        System.out.println("Удаляем элементы");
        manager.deleteTask(2);
        for(Task t: manager.getTasks()) {
            System.out.println(t);
        };

        manager.deleteSubtask(subtaskId2);
        System.out.println(manager.getSubtasks());
        for(Epic e: manager.getEpics()) {
            System.out.println(e);
        }

        manager.deleteEpic(epicId1);
        System.out.println(manager.getSubtasks());
        for(Epic e: manager.getEpics()) {
            System.out.println(e);
        }


        manager.getSubtask(subtaskId2);
        manager.getSubtask(subtaskId1);
        manager.getSubtask(subtaskId3);

        System.out.println("Общий вывод всего");
        manager.printAllTasks(manager);
    }
}
