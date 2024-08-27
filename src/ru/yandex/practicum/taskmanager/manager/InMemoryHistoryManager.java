package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.files.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {


    private static class Node {
        Task task;
        Node prev;
        Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Task: {" + task + "}; ";
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private ArrayList<Task> getTasks() {
        ArrayList<Task> viewTasks = new ArrayList<>();
        Node node = first; //указываем точку отсчета для итерации по связанному списку

        while (node != null) {
            viewTasks.add(node.task);
            node = node.next;
        }

        return viewTasks;
    }

    private void linkLast(Task task) {
        final Node newNode = new Node(task, last, null);
        final Node oldLast = nodeMap.remove(last.task.getId());

        oldLast.next = newNode;
        newNode.prev = oldLast;
        nodeMap.put(oldLast.task.getId(), oldLast);
        last = newNode;
    }

    private Node linkFirst(Task task) {
        final Node node = new Node(task, null, null);
        first = node;
        last = node;
        return node;
    }


    @Override
    public void add(Task task) {
        remove(task.getId());

        if (nodeMap.isEmpty()) {
            linkFirst(task);
        } else if (nodeMap.size() == 1) {
            linkLast(task);
            first.next = last;
        } else {
            linkLast(task);
        }
        nodeMap.put(task.getId(), last);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        final Node node = nodeMap.remove(id);
        if (node == null || (node.prev == null && node.next == null)) {
            return;
        }

        if (node.prev == null && node.next != null) {               //проверяем, что в списке не один элемент
            // 1. Удаление первого элемента
            first = node.next;
            first.prev = null;
        } else if (node.prev != null && node.next != null) {
            //2. Удаление среднего элемента
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else if (node.next == null) {
            //3. удаление последнего элемента
            last = node.prev;
            node.prev.next = null;
        }
    }

}
