package ru.yandex.practicum.taskmanager.manager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import ru.yandex.practicum.taskmanager.files.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    //Восстанавливает из файла менеджер + история просмотров
    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            Task task;
            int id = 1;

            while ((line = reader.readLine()) != null) {
                if (line.equals("id,type,name,status,description,duration,startTime,endTime,epic")) {
                    continue;
                }

                task = CSVTaskFormat.taskFromString(line);

                taskManager.setId(task.getId());

                if (id < task.getId()) {
                    id = task.getId() + 1;
                }

                switch (task.getTaskType()) {
                    case TASK:
                        taskManager.addNewTask(task);
                        break;
                    case EPIC:
                        taskManager.addNewEpic(new Epic(task.getId(),
                                                        task.getTaskType(),
                                                        task.getName(),
                                                        task.getStatus(),
                                                        task.getDescription(),
                                                        task.getDuration(),
                                                        task.getStartTime(),
//                                                        (task.getStartTime() != null ? task.getStartTime().format(task.formatter) :
//                                                                task.getStartTime().toString()),
//                                                        (task.getEndTime() != null ? task.getEndTime().format(task.formatter) :
//                                                                task.getEndTime().toString())));
                                                        task.getEndTime()));
                                                        break;
                    case SUBTASK:
                        taskManager.addNewSubtask(new Subtask(task.getId(),
                                task.getTaskType(),
                                task.getName(),
                                task.getStatus(),
                                task.getDescription(),
                                task.getDuration(),
                                task.getStartTime(),
//                                (task.getStartTime() != null ? task.getStartTime().format(task.formatter) : task.getStartTime().toString()),
                                task.getEpicId()));
                        break;
                }

                taskManager.setId(id);
            }
        } catch (IOException e) {
            System.out.println("Ошибка считывания из файла" + e.getMessage());
        }

        return taskManager;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,duration,startTime,endTime,epic" + "\n");
            for (Task task: getTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "," + "\n");
                if (task.equals(null)) {
                    throw new ManagerSaveException("Отсутствуют задачи для сохранения");
                }
            }
            for (Epic epic: getEpics()) {
                writer.write(CSVTaskFormat.toString(epic) + "," + "\n");
                if (epic.equals(null)) {
                    throw new ManagerSaveException("Отсутствуют задачи для сохранения");
                }
            }
            for (Subtask subtask: getSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask) + "," + "\n");
                if (subtask.equals(null)) {
                    throw new ManagerSaveException("Отсутствуют задачи для сохранения");
                }
            }
        } catch (ManagerSaveException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTask(int id) {
        final Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        final Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public int addNewTask(Task task) {
        final int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        final int id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateEpicStatus(Subtask subtask) {
        super.updateEpicStatus(subtask);
        save();
    }

    @Override
    public void updateEpicTimeFields(int epicId) {
        super.updateEpicTimeFields(epicId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int idEpic) {
        super.deleteEpic(idEpic);
        save();
    }

    @Override
    public void deleteSubtask(int idSubtask) {
        super.deleteSubtask(idSubtask);
        save();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Set<Task> sortedTaskSet = super.getPrioritizedTasks();
//        save();
        return sortedTaskSet;
    }
}
