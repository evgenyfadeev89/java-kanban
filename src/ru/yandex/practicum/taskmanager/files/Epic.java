package ru.yandex.practicum.taskmanager.files;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.taskType = TaskType.EPIC;
    }

public Epic(int id, TaskType taskType, String name, TaskStatus status, String description) {
    super(id, taskType, name, status, description);
    this.taskType = TaskType.EPIC;
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

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + "'" +
                ", description='" + description + "'" +
                ", id=" + id +
                ", status='" + status + "'" +
                ", subtaskIds=" + subtaskIds +
                ", type=" + taskType +
                "}";
    }
}
