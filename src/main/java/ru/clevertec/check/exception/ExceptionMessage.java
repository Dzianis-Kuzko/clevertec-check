package ru.clevertec.check.exception;

public enum ExceptionMessage {
    BAD_REQUEST("400"),
    NOT_ENOUGH_MONEY("400"),
    PRODUCT_NOT_FOUND("404"),
    INTERNAL_SERVER_ERROR("500");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
