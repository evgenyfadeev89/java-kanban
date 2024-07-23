package ru.yandex.practicum.taskmanager.Files;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }


    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + "'" +
                ", description='" + description + "'" +
                ", id=" + id +
                ", status='" + status + "'" +
                ", subtaskIds=" + subtaskIds +
                "}";
    }
}
