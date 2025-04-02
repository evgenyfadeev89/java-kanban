package ru.yandex.practicum.taskmanager.manager;

public class TimeCheckException extends RuntimeException {

    public TimeCheckException() {
        super();
    }

    public TimeCheckException(String message) {
        super(message);
    }

    public TimeCheckException(String message, Throwable error) {
        super(message, error);
    }

    public TimeCheckException(Throwable error) {
        super(error);
    }
}
