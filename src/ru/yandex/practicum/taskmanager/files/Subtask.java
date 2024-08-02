package ru.yandex.practicum.taskmanager.files;


public class Subtask extends Task {
    protected int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public boolean isEpic() {
        return false;
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