package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.Manager.*;
import ru.yandex.practicum.taskmanager.Files.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Тест!");

        TaskManager manager = new TaskManager();

        //создание
        Task task1 = new Task(1,"Task_1", "Task_1 description", "NEW");
        Task task2 = new Task(2,"Task_2", "Task_2 description", "IN_PROGRESS");
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic(3,"Epic_1", "Epic_1 description", "NEW");
        Epic epic2 = new Epic(4,"Epic_2", "Epic_2 description", "NEW");
        Epic epic8 = new Epic(8,"Epic_2", "Epic_2 description", "NEW");
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);
        final int epicId8 = manager.addNewEpic(epic8);

        Subtask subtask1 = new Subtask(5,"Subtask_1-1", "Subtask_1 description", "NEW", epicId1);
        Subtask subtask2 = new Subtask(6,"Subtask_2-1", "Subtask_1 description", "NEW", epicId1);
        Subtask subtask3 = new Subtask(7,"Subtask_3-2", "Subtask_1 description", "DONE", epicId2);
        final Integer subtaskId1 = manager.addNewSubtask(subtask1);
        final Integer subtaskId2 = manager.addNewSubtask(subtask2);
        final Integer subtaskId3 = manager.addNewSubtask(subtask3);

        System.out.println("Список Task");
        System.out.println(manager.getTasks());

        System.out.println("Список Epic");
        System.out.println(manager.getEpics());

        System.out.println("Список Subtask");
        System.out.println(manager.getSubtasks());

        //обновление
        final Task task = manager.getTask(taskId2);
        task.setStatus("DONE");
        manager.updateTask(task);
        System.out.println("Change status: Task2 IN_PROGRESS -> DONE");
        System.out.println("Задачи:");
        for(Task t: manager.getTasks()) {
            System.out.println(t);
        }

        Subtask subtask = manager.getSubtask(subtaskId2);
        subtask.setStatus("DONE");
        manager.updateSubtask(subtask);
        System.out.println("Change status: Subtask6 NEW -> DONE");
        subtask = manager.getSubtask(subtaskId3);
        subtask.setStatus("NEW");
        manager.updateSubtask(subtask);
        System.out.println("Change status: Subtask7 DONE -> NEW");

        System.out.println("Список Subtask");
        System.out.println(manager.getSubtasks());

        System.out.println("Эпики");
        for(Epic e: manager.getEpics()) {
            System.out.println(e);
        }

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
    }
}
