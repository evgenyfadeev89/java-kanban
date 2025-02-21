package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.files.*;

import java.time.Duration;


public class CSVTaskFormat {
    public static String toString(Task task) {
        if (task.getTaskType().equals(TaskType.SUBTASK)) {
            return task.getId() + "," +
                    task.getTaskType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + "," +
                    task.getDuration().toMinutes() + "," +
//                    (task.getStartTime() != null ? task.getStartTime().format(task.formatter) : task.getStartTime()) + "," +
                    task.getStartTime() + "," +
                    task.getEndTime() + "," +
//                    (task.getEndTime() != null ? task.getEndTime().format(task.formatter) : task.getEndTime()) + "," +
                    task.getEpicId()
                    ;
        }
        return task.getId() + "," +
                task.getTaskType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getDuration().toMinutes() + "," +
//                (task.getStartTime() != null ? task.getStartTime().format(task.formatter) : task.getStartTime()) + "," +
                task.getStartTime() + "," +
                task.getEndTime()
//                (task.getStartTime() != null ? task.getStartTime().format(task.formatter) : task.getStartTime()) + "," +
//                (task.getEndTime() != null ? task.getEndTime().format(task.formatter) : task.getEndTime())
                ;
    }

    public static Task taskFromString(String value) {
        Task task = null;
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType taskType = TaskType.valueOf(values[1]);
        final String name = String.valueOf(values[2]);
        final TaskStatus status = TaskStatus.valueOf(values[3]);
        final String description = String.valueOf(values[4]);
        final Duration duration = Duration.ofMinutes(Integer.parseInt((values[5])));
        final String startTime = String.valueOf(values[6]);
        final String endTime = String.valueOf(values[7]);

        switch (taskType) {
            case TASK:
                task = new Task(id, taskType, name, status, description, duration, startTime);
                return task;
            case EPIC:
                task = new Epic(id, taskType, name, status, description, duration, startTime, endTime);
                return task;
            case SUBTASK:
                final int epicId = Integer.parseInt(values[8]);
                task = new Subtask(id, taskType, name, status, description, duration, startTime, epicId);
        }

        return task;
    }
}
