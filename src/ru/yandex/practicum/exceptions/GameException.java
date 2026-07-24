package ru.yandex.practicum.exceptions;

import java.io.Serial;

public class GameException extends Exception {
    @Serial
    private static final long serialVersionUID = 4013902075304427242L;

    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
