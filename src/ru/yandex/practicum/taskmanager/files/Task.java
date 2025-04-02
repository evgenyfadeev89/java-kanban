package ru.yandex.practicum.taskmanager.files;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected int id;
    protected String name;
    protected TaskStatus status;
    protected String description;
    protected TaskType taskType;
    protected int epicId;
    protected Duration duration = Duration.ZERO;
    protected LocalDateTime startTime;
//    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.taskType = TaskType.TASK;
    }

    public Task(TaskType taskType,
                String name,
                TaskStatus status,
                String description) {
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id,
                TaskType taskType,
                String name,
                TaskStatus status,
                String description) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(TaskType taskType,
                String name,
                TaskStatus status,
                String description,
                long duration,
                String startTime) {
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime != null ? LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
    }

    public Task(int id,
                TaskType taskType,
                String name,
                TaskStatus status,
                String description,
                long duration,
                String startTime) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime != null ? LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    public int getEpicId() {
        return epicId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndTime() {
        if (startTime == null || (duration == null || duration.isZero())) {
            return null;
        }
        return startTime.plus(duration).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public String getStartTime() {
        return startTime != null ? startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime != null ? LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        int total = 31;
        total = (total + id) * 31;
        return total;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + "'" +
                ", description='" + description + "'" +
                ", id=" + id +
                ", status='" + status + "'" +
                ", type=" + taskType +
                ", duration=" + duration.toSeconds() +
                ", startTime=\"" + startTime +
                "\"}";
    }
}
