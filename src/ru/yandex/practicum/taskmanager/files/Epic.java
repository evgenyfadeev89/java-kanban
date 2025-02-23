package ru.yandex.practicum.taskmanager.files;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.taskType = TaskType.EPIC;
    }

    public Epic(TaskType taskType,
                String name,
                TaskStatus status,
                String description) {
        super(taskType, name, status, description);
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id,
                TaskType taskType,
                String name,
                TaskStatus status,
                String description) {
        super(id, taskType, name, status, description);
        this.taskType = TaskType.EPIC;
    }

    public Epic(TaskType taskType,
                String name,
                TaskStatus status,
                String description,
                long duration,
                String startTime,
                String endTime) {
        super(taskType, name, status, description, duration, startTime);
        this.taskType = TaskType.EPIC;
        this.endTime = endTime != null ? LocalDateTime.parse(endTime, formatter) : null;
    }

    public Epic(int id,
                TaskType taskType,
                String name,
                TaskStatus status,
                String description,
                long duration,
                String startTime,
                String endTime) {
        super(id, taskType, name, status, description, duration, startTime);
        this.taskType = TaskType.EPIC;
        this.endTime = endTime != null ? LocalDateTime.parse(endTime, formatter) : null;
    }

    public void setNullSubtaskIds() {
        subtaskIds = new ArrayList<>();
    }

    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public boolean isEpic() {
        return true;
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime != null ? LocalDateTime.parse(endTime, formatter) : null;
    }

    public String getEndTime() {
        if (getStartTime() == null || Duration.ofMinutes(getDuration()) == null) {
            return null;
        }
        return endTime != null ? endTime.format(formatter) : null;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + "'" +
                ", description='" + description + "'" +
                ", id=" + id +
                ", status='" + status + "'" +
                ", subtaskIds=" + subtaskIds +
                ", type=" + taskType +
                ", duration=" + duration.toSeconds() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                "}";
    }
}
