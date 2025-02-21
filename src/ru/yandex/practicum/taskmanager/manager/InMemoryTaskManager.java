package ru.yandex.practicum.taskmanager.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import ru.yandex.practicum.taskmanager.files.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();

    Comparator<Task> comparator = Comparator.comparing(task -> LocalDateTime.parse(task.getStartTime(), formatter));
    protected final Set<Task> sortedTaskSet = new TreeSet(comparator);

    private final HistoryManager historyManager = Managers.getDefaultHistory();


    private int generateId() {
        return ++id;
    }

    public void setId(int id) {
        this.id = id;
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
        for (int id : subtaskIds) {
            epicSubtasksArrayList.add(getSubtask(id));
        }
        return epicSubtasksArrayList;
    }

    @Override
    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
        }
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.get(id) != null) {
            historyManager.add(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
        }
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public int addNewTask(Task task) {
        if (findCrossTask(task)) {
            System.out.println("Добавляемая задача пересекается по времени с другой");
//            return -1;
        }
        task.setId(id);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            addToSortedTaskSet(task);
        }
        generateId();
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(id);
        epics.put(epic.getId(), epic);
        generateId();
        return epic.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        if (findCrossTask(subtask)) {
            System.out.println("Добавляемая задача пересекается по времени с другой");
//            return -1;
        }
        if (subtask.isEpic()) {
            System.out.println("Вы пытаетесь добавить Epic в Subtasks");
            return -1;
        }
        subtask.setId(id);
        Epic epic = getEpic(subtask.getEpicId());
        if (epic == null) {
            System.out.println("такого эпика нет" + subtask.getEpicId());
            return -1;
        }
        epic.addSubtaskId(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        generateId();
        updateEpic(epic);
        if (subtask.getStartTime() != null) {
            addToSortedTaskSet(subtask);
        }
        return subtask.getId();
    }

    @Override
    public void updateEpicStatus(Subtask subtask) {
        Set<TaskStatus> allStatus = new HashSet<>();
        Epic epic = getEpic(subtask.getEpicId());
        for (int idSubtask : epic.getSubtaskIds()) {
            allStatus.add(subtasks.get(idSubtask).getStatus());
        }
        if (allStatus.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        } else if (allStatus.size() == 1) {
            if (allStatus.contains(TaskStatus.NEW)) {
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
        for (int idSubtask : epic.getSubtaskIds()) {
            allStatus.add(subtasks.get(idSubtask).getStatus());
        }
        if (allStatus.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        } else if (allStatus.size() == 1) {
            if (allStatus.contains(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.NEW);
                return;
            } else if (allStatus.contains(TaskStatus.DONE)) {
                epic.setStatus(TaskStatus.DONE);
                return;
            }
        }
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    public void updateEpicTimeFields(int epicId) {
        if (getEpicSubtasks(epicId).isEmpty()) {
            getEpic(epicId).setDuration(0);
            getEpic(epicId).setStartTime(null);
            getEpic(epicId).setEndTime(null);
            return;
        }

        // Самое раннее время начала и самое позднее время завершения
        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        long totalDuration = 0;

        for (Subtask subtask: getEpicSubtasks(epicId)) {
            if (subtask.getStartTime() != null) {
                if (earliestStart == null || LocalDateTime.parse(subtask.getStartTime(), formatter).isBefore(earliestStart)) {
                    earliestStart = LocalDateTime.parse(subtask.getStartTime(), formatter);
                }
            }
            if (subtask.getEndTime() != null) {
                if (latestEnd == null || LocalDateTime.parse(subtask.getEndTime(), formatter).isAfter(latestEnd)) {
                    latestEnd = LocalDateTime.parse(subtask.getEndTime(), formatter);
                }
            }
            if (subtask.getDuration() != null) {
                totalDuration += subtask.getDuration().toMinutes();
            }
        }

        getEpic(epicId).setStartTime(earliestStart != null ? earliestStart.format(formatter) : null);
        getEpic(epicId).setEndTime(latestEnd != null ? latestEnd.format(formatter) : null);
        getEpic(epicId).setDuration(totalDuration);
    }

    @Override
    public void updateTask(Task task) {
        if (getTask(task.getId()).equals(task) && getTask(task.getId()).hashCode() == task.hashCode()) { //проверяем, что задачи идентичны
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (getEpic(epic.getId()).equals(epic) && getEpic(epic.getId()).hashCode() == epic.hashCode()) { //проверяем, что задачи идентичны
            updateEpicStatus(epic.getId());
            updateEpicTimeFields(epic.getId());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (getSubtask(subtask.getId()).equals(subtask) && getSubtask(subtask.getId()).hashCode() == subtask.hashCode()) { //проверяем, что задачи идентичны
            subtasks.put(subtask.getId(), subtask);
            updateEpic(getEpic(subtask.getEpicId()));
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id); //добавили
    }

    @Override
    public void deleteEpic(int idEpic) {
        for (int idSubtask : epics.get(idEpic).getSubtaskIds()) {
            deleteSubtask(idSubtask);
            if (epics.get(idEpic).getSubtaskIds().isEmpty()) {
                break;
            }
        }
        epics.remove(idEpic);
        historyManager.remove(idEpic);
    }

    @Override
    public void deleteSubtask(int idSubtask) {
        int idEpic = getSubtask(idSubtask).getEpicId();
        subtasks.remove(idSubtask);
        epics.get(idEpic).getSubtaskIds().remove((Object) idSubtask);
        updateEpic(epics.get(idEpic));
        historyManager.remove(idSubtask);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        deleteSubtasks();
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : getEpics()) {
            updateEpic(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void addToSortedTaskSet (Task task) {
        if (task.getStartTime() != null || task.getDuration() != null) {
            sortedTaskSet.add(task);
        }
    }
    @Override
    public Set<Task> getPrioritizedTasks() {
        return sortedTaskSet;
    }

    public boolean findCrossTask (Task task) {
        if (task.getStartTime() == null) {// || task.getEndTime() == null) {
            return false; // Если время не задано, пересечения нет
        }

        return getPrioritizedTasks().stream().
                filter(task1 -> task1.equals(task)).
                anyMatch(task1 -> !LocalDateTime.parse(task.getEndTime(), formatter).
                                          isBefore(LocalDateTime.parse(task.getStartTime(), formatter)) &&
                                  !LocalDateTime.parse(task1.getStartTime(), formatter).
                                          isAfter(LocalDateTime.parse(task.getStartTime(), formatter)));
    }

    @Override
    public void printAllTasks(TaskManager manager) {
        // Вывод задач
        System.out.println("Задачи:");
        manager.getTasks().stream()
                .forEach(System.out::println);

        // Вывод эпиков и их подзадач
        System.out.println("Эпики:");
        manager.getEpics().stream()
                .peek(System.out::println) // Печатаем эпик
                .flatMap(epic -> manager.getEpicSubtasks(epic.getId()).stream()) // Получаем подзадачи эпика
                .forEach(subtask -> System.out.println("--> " + subtask)); // Печатаем подзадачи

        // Вывод подзадач
        System.out.println("Подзадачи:");
        manager.getSubtasks()//.stream()
                .forEach(System.out::println);

        // Вывод истории
        System.out.println("История:");
        historyManager.getHistory()//.stream()
                .forEach(System.out::println);
    }
//    @Override
//    public void printAllTasks(TaskManager manager) {
//        System.out.println("Задачи:");
//        for (Task task : manager.getTasks()) {
//            System.out.println(task);
//        }
//        System.out.println("Эпики:");
//        for (Task epic : manager.getEpics()) {
//            System.out.println(epic);
//
//            for (Task task : manager.getEpicSubtasks(epic.getId())) {
//                System.out.println("--> " + task);
//            }
//        }
//        System.out.println("Подзадачи:");
//        for (Task subtask : manager.getSubtasks()) {
//            System.out.println(subtask);
//        }
//
//        System.out.println("История:");
//        for (Task task : historyManager.getHistory()) {
//            System.out.println(task);
//        }
//    }
}
