package ru.yandex.practicum.taskmanager.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import ru.yandex.practicum.taskmanager.Files.*;

public class TaskManager implements ITaskManager {
    private int id = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();


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
        tasks.put(task.getId(), task);
        id++;
        return task.getId();
    }

    @Override
    public int addNewEpic (Epic epic) {
        epics.put(epic.getId(), epic);
        id++;
        return epic.getId();
    }

    @Override
     public int addNewSubtask(Subtask subtask) {
        Epic epic = getEpic(subtask.getEpicId());
        if(epic == null) {
            System.out.println("такого эпика нет" + subtask.getEpicId());
            return -1;
        }
        epic.addSubtaskId(subtask.getId());
        subtasks.put(subtask.getId(), subtask);

        updateEpicStatus(subtask);
        return subtask.getId();
    }


    @Override
    public void updateEpicStatus(Subtask subtask) {
        Set<String> allStatus = new HashSet<String>();
        Epic epic = getEpic(subtask.getEpicId());
        for(int idSubtask: epic.getSubtaskIds()){
            allStatus.add(subtasks.get(idSubtask).getStatus());
        }
        if(allStatus.isEmpty()) {
            epic.setStatus("NEW");
            return;
        } else if (allStatus.size() == 1) {
            if(allStatus.iterator().next().equals("NEW")) {
                epic.setStatus("NEW");
                return;
            } else if (allStatus.iterator().next().equals("DONE")) {
                epic.setStatus("DONE");
                return;
            }
        }
        System.out.println("дошел сюда конец"); //debug
        epic.setStatus("IN_PROGRESS");
    }

    public void updateEpicStatus(int idEpic) {
        Set<String> allStatus = new HashSet<String>();
        Epic epic = getEpic(idEpic);
        for(int idSubtask: epic.getSubtaskIds()){
            allStatus.add(subtasks.get(idSubtask).getStatus());
        }
        if(allStatus.isEmpty() || allStatus.equals("NEW")) {
            epic.setStatus("NEW");
            return;
        }
        if(allStatus.equals("DONE")) {
            epic.setStatus("DONE");
            return;
        }
        epic.setStatus("IN_PROGRESS");
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
