package ru.yandex.practicum.taskmanager.files;


public class Subtask extends Task {
    protected int epicId;
    protected TaskType taskType;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, TaskType taskType, String name, TaskStatus status, String description, int epicId) {
        super(id, taskType, name, status, description);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

//    @Override
    public Object getEpicId() {
        return epicId;
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
                "}";
    }
}