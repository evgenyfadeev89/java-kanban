package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.files.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    protected int historyCntPosition = 0;
    protected final List<Task> viewTasks = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyCntPosition == 10) {
            historyCntPosition = 0;
        }

        try {
            viewTasks.set(historyCntPosition, task); //если элемента нет в списке - ловим исключение
        } catch (IndexOutOfBoundsException e) {
            viewTasks.add(historyCntPosition, task);
        }
        ++historyCntPosition;
    };

    @Override
    public List<Task> getHistory() {
        return viewTasks;
    };
}
