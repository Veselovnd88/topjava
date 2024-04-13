package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String localizedType;
    private final String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String localizedType, String[] details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
        this.localizedType = localizedType;
    }
}