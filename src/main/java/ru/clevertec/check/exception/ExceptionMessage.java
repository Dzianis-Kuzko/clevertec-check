package ru.clevertec.check.exception;

public enum ExceptionMessage {
    BAD_REQUEST("BAD REQUEST"),
    NOT_ENOUGH_MONEY("NOT ENOUGH MONEY"),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
