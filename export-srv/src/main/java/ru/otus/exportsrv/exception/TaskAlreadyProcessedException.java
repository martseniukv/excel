package ru.otus.exportsrv.exception;

public class TaskAlreadyProcessedException extends RuntimeException{

    public TaskAlreadyProcessedException() {
    }

    public TaskAlreadyProcessedException(String message) {
        super(message);
    }
}
