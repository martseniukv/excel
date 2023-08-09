package ru.otus.exportsrv.exception;

public class ExportException extends RuntimeException {

    public ExportException() {
    }

    public ExportException(String message) {
        super(message);
    }
}
