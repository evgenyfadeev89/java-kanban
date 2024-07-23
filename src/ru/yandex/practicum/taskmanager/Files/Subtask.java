package ru.yandex.practicum.taskmanager.Files;


public class Subtask extends Task {
    protected int epicId;

    public Subtask(int id, String name, String description, String status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + "'" +
                ", description='" + description + "'" +
                ", id=" + id +
                ", status='" + status + "'" +
                ", epicId=" + epicId +
                "}";
    }
}