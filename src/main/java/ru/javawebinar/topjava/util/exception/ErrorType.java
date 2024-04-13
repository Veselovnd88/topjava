package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR("errorCode.appError"),
    DATA_NOT_FOUND("errorCode.notFound"),
    DATA_ERROR("errorCode.conflict"),
    VALIDATION_ERROR("errorCode.validation");

    private final String errorCode;

    ErrorType(String code) {
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
