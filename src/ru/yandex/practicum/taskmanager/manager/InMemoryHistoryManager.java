package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.files.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    //protected int historyCntPosition = 0;
    protected final List<Task> viewTasks = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (viewTasks.size() == 10) {
            viewTasks.remove(0);
        }
        viewTasks.add(task);
    };

    @Override
    public List<Task> getHistory() {
        return viewTasks;
    };
}
