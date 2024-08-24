package ru.clevertec.check.exception;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException() {
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }

    public NotEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }

}
