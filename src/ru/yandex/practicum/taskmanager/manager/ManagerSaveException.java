package ru.yandex.practicum.taskmanager.manager;


public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
    }

    public ManagerSaveException(String message) {
        super(message);
    }

    public ManagerSaveException(String message, Throwable error) {
        super(message, error);
    }
}
