package ru.yandex.practicum.taskmanager.manager;

import java.io.*;
import java.nio.charset.StandardCharsets;

import ru.yandex.practicum.taskmanager.files.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            Task task;
            int id = 1;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.equals("id,type,name,status,description,duration,startTime,endTime,epic")) {
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
            getTasks().stream()
                    .peek(task -> {
                        if (task == null) {
                            throw new ManagerSaveException("Отсутствуют задачи для сохранения");
                        }
                    })
                    .forEach(task -> {
                        try {
                            writer.write(CSVTaskFormat.toString(task) + "," + "\n");
                        } catch (IOException e) {
                            throw new ManagerSaveException("Ошибка записи в файл", e);
                        }
                    });
            getEpics().stream()
                    .peek(epic -> {
                        if (epic == null) {
                            throw new ManagerSaveException("Отсутствуют задачи для сохранения");
                        }
                    })
                    .forEach(epic -> {
                        try {
                            writer.write(CSVTaskFormat.toString(epic) + "," + "\n");
                        } catch (IOException e) {
                            throw new ManagerSaveException("Ошибка записи в файл", e);
                        }
                    });
            getSubtasks().stream()
                    .peek(subtask -> {
                        if (subtask == null) {
                            throw new ManagerSaveException("Отсутствуют задачи для сохранения");
                        }
                    })
                    .forEach(subtask -> {
                        try {
                            writer.write(CSVTaskFormat.toString(subtask) + "," + "\n");
                        } catch (IOException e) {
                            throw new ManagerSaveException("Ошибка записи в файл", e);
                        }
                    });
        } catch (ManagerSaveException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int addNewTask(Task task) throws TimeCheckException {
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
    public int addNewSubtask(Subtask subtask) throws TimeCheckException {
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
}
