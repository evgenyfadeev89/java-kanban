package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.files.*;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    //List<Task> getHistory();
    void remove(int id); //подумать, тут ли надо

    List<Task> getHistory();

}
