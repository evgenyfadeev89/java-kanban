package ru.yandex.practicum.taskmanager.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import ru.yandex.practicum.taskmanager.files.*;

public class TaskManager implements ITaskManager {
    private int id = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    private int generateId() {
        return ++id;
    }
    public int getId() {
        return id;
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        ArrayList<Subtask> epicSubtasksArrayList = new ArrayList<>(subtaskIds.size());
        for(int id: subtaskIds) {
            epicSubtasksArrayList.add(getSubtask(id));
        }
        return epicSubtasksArrayList;
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public int addNewTask (Task task) {
        task.setId(getId());
        tasks.put(task.getId(), task);
        generateId(); //добавил вызов на увеличение при создании задач
        return task.getId();
    }

    @Override
    public int addNewEpic (Epic epic) {
        epic.setId(getId());
        epics.put(epic.getId(), epic);
        generateId();
        return epic.getId();
    }

    @Override
     public int addNewSubtask(Subtask subtask) {
        subtask.setId(getId());
        Epic epic = getEpic(subtask.getEpicId());
        if(epic == null) {
            System.out.println("такого эпика нет" + subtask.getEpicId());
            return -1;
        }
        epic.addSubtaskId(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        generateId();
        updateEpicStatus(subtask);
        return subtask.getId();
    }


    @Override
    public void updateEpicStatus(Subtask subtask) {
        Set<TaskStatus> allStatus = new HashSet<>();
        Epic epic = getEpic(subtask.getEpicId());
        for(int idSubtask: epic.getSubtaskIds()){
            allStatus.add(subtasks.get(idSubtask).getStatus());
        }
        if(allStatus.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        } else if (allStatus.size() == 1) {
            if(allStatus.contains(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.NEW);
                return;
            } else if (allStatus.contains(TaskStatus.DONE)) {
                epic.setStatus(TaskStatus.DONE);
                return;
            }
        }
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    private void updateEpicStatus(int idEpic) {
        Set<TaskStatus> allStatus = new HashSet<>();
        Epic epic = getEpic(idEpic);
        for(int idSubtask: epic.getSubtaskIds()){
            allStatus.add(subtasks.get(idSubtask).getStatus());
        }
        if(allStatus.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        } else if (allStatus.size() == 1) {
            if(allStatus.contains(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.NEW);
                return;
            } else if (allStatus.contains(TaskStatus.DONE)) {
                epic.setStatus(TaskStatus.DONE);
                return;
            }
        }
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public void updateTask(Task task) {
        if(getTask(task.getId()).equals(task) && getTask(task.getId()).hashCode() == task.hashCode()) { //проверяем, что задачи идентичны
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if(getEpic(epic.getId()).equals(epic) && getEpic(epic.getId()).hashCode() == epic.hashCode()) { //проверяем, что задачи идентичны
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if(getSubtask(subtask.getId()).equals(subtask) && getSubtask(subtask.getId()).hashCode() == subtask.hashCode()) { //проверяем, что задачи идентичны
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask);
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int idEpic) {
        for (int idSubtask : epics.get(idEpic).getSubtaskIds()) {
            deleteSubtask(idSubtask);
            if(epics.get(idEpic).getSubtaskIds().isEmpty()) {
                break;
            }
        }
        epics.remove(idEpic);
    }

    @Override
    public void deleteSubtask(int idSubtask) {
        int idEpic = getSubtask(idSubtask).getEpicId();
        subtasks.remove(idSubtask);
        epics.get(idEpic).getSubtaskIds().remove((Object)idSubtask);
        updateEpicStatus(idEpic);
    }

    @Override
    public void deleteTasks () {
        tasks.clear();
    }

    @Override
    public void deleteEpics () {
        deleteSubtasks();
        epics.clear();
    }

    @Override
    public void deleteSubtasks () {
        subtasks.clear();
        for(Epic epic: getEpics()) {
            updateEpic(epic);
        }
    }


}
