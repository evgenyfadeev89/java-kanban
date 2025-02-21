package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.files.*;

public class CSVTaskFormat {
    public static String toString(Task task) {
        if (task.getTaskType().equals(TaskType.SUBTASK)) {
            return task.getId() + "," +
                    task.getTaskType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + "," +
                    task.getEpicId();
        }
        return task.getId() + "," +
                task.getTaskType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription();
    }

    public static Task taskFromString(String value) {
        Task task = null;
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType taskType = TaskType.valueOf(values[1]);
        final String name = String.valueOf(values[2]);
        final TaskStatus status = TaskStatus.valueOf(values[3]);
        final String description = String.valueOf(values[4]);

        switch (taskType) {
            case TASK:
                task = new Task(id, taskType, name, status, description);
                return task;
            case EPIC:
                task = new Epic(id, taskType, name, status, description);
                return task;
            case SUBTASK:
                final int epicId = Integer.parseInt(values[5]);
                task = new Subtask(id, taskType, name, status, description, epicId);
        }

        return task;
    }
}
