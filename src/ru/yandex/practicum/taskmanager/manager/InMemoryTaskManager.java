package ru.yandex.practicum.taskmanager.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        return !tasks.values().isEmpty() ? new ArrayList<>(tasks.values()) : new ArrayList<>();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return !epics.values().isEmpty() ? new ArrayList<>(epics.values()) : new ArrayList<>();
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return !subtasks.values().isEmpty() ? new ArrayList<>(subtasks.values()) : new ArrayList<>();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        return subtaskIds.stream()
                .map(id -> getSubtask(id))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void printPrioritizedTasks() {
        sortedTaskSet.forEach(System.out::println);
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
    public int addNewTask(Task task) throws TimeCheckException {
        if (findCrossTask(task)) {
            throw new TimeCheckException("Добавляемая задача пересекается по времени с другой");
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
    public int addNewSubtask(Subtask subtask) throws TimeCheckException {
        if (findCrossTask(subtask)) {
            throw new TimeCheckException("Добавляемая задача пересекается по времени с другой");
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

        if (epic.getSubtaskIds() != null) {
            allStatus = epic.getSubtaskIds().stream()
                    .map(idSubtask -> subtasks.get(idSubtask).getStatus())
                    .collect(Collectors.toCollection(HashSet::new));
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

        if (epic.getSubtaskIds() != null) {
            allStatus = epic.getSubtaskIds().stream()
                    .map(idSubtask -> subtasks.get(idSubtask).getStatus())
                    .collect(Collectors.toCollection(HashSet::new));
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

        List<Subtask> subtasks = getEpicSubtasks(epicId);

        Optional<LocalDateTime> earliestStartOpt = subtasks.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .map(subtask -> LocalDateTime.parse(subtask.getStartTime(), formatter))
                .min(LocalDateTime::compareTo);

        LocalDateTime earliestStart = earliestStartOpt.orElse(null);

        Optional<LocalDateTime> latestEndOpt = subtasks.stream()
                .filter(subtask -> subtask.getEndTime() != null)
                .map(subtask -> LocalDateTime.parse(subtask.getEndTime(), formatter))
                .max(LocalDateTime::compareTo);

        LocalDateTime latestEnd = latestEndOpt.orElse(null);

        long totalDuration = subtasks.stream()
                .filter(subtask -> Duration.ofMinutes(subtask.getDuration()) != null)
                .mapToLong(subtask -> subtask.getDuration())
                .sum();

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
        List<Integer> subtaskIds = new ArrayList<>(epics.get(idEpic).getSubtaskIds());

        subtaskIds.forEach(this::deleteSubtask);

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
        subtasks.values().forEach(subtask -> getEpic(subtask.getEpicId()).setNullSubtaskIds());
        subtasks.clear();
        getEpics().stream().forEach(this::updateEpic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void addToSortedTaskSet(Task task) {
        if (task.getStartTime() != null || Duration.ofMinutes(task.getDuration()) != null) {
            sortedTaskSet.add(task);
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return sortedTaskSet;
    }

    public boolean findCrossTask(Task task) {
        if (task.getStartTime() == null) {
            return false;
        }

        return getPrioritizedTasks().stream()
                .filter(task1 -> !task1.equals(task))
                .anyMatch(task1 -> LocalDateTime.parse(task1.getEndTime(), formatter)
                        .isAfter(LocalDateTime.parse(task.getStartTime(), formatter))
                        & LocalDateTime.parse(task.getEndTime(), formatter)
                        .isAfter(LocalDateTime.parse(task1.getStartTime(), formatter)));
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
                .peek(System.out::println)
                .flatMap(epic -> manager.getEpicSubtasks(epic.getId()).stream())
                .forEach(subtask -> System.out.println("--> " + subtask));

        // Вывод подзадач
        System.out.println("Подзадачи:");
        manager.getSubtasks()//.stream()
                .forEach(System.out::println);

        // Вывод истории
        System.out.println("История:");
        historyManager.getHistory()//.stream()
                .forEach(System.out::println);
    }
}
