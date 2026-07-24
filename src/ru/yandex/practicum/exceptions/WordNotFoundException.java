package ru.yandex.practicum.exceptions;

public class WordNotFoundException extends GameException {
    public WordNotFoundException(String message) {
        super(message);
    }

    public WordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
