package ru.yandex.practicum.taskmanager.files;


public class Task {
    protected int id;
    protected String name;
    protected TaskStatus status;
    protected String description;
    protected TaskType taskType;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.taskType = TaskType.TASK;
    }

    public Task(int id, TaskType taskType, String name, TaskStatus status, String description) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
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

    public Object getEpicId() {
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                "}";
    }
}
