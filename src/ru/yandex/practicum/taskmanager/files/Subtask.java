package ru.yandex.practicum.taskmanager.files;

import com.google.gson.annotations.SerializedName;

public class Subtask extends Task {
    @SerializedName("subtaskType")
    protected TaskType taskType;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(TaskType taskType,
                   String name,
                   TaskStatus status,
                   String description,
                   long duration,
                   String startTime,
                   int epicId
    ) {
        super(taskType, name, status, description, duration, startTime);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id,
                   TaskType taskType,
                   String name,
                   TaskStatus status,
                   String description,
                   long duration,
                   String startTime,
                   int epicId
    ) {
        super(id, taskType, name, status, description, duration, startTime);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public boolean isEpic() {
        return false;
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + "'" +
                ", description='" + description + "'" +
                ", id=" + id +
                ", status='" + status + "'" +
                ", epicId=" + epicId +
                ", type=" + taskType +
                ", duration=" + duration.toMinutes() +
                ", startTime=\"" + startTime + "\"" +
                "}";
    }
}