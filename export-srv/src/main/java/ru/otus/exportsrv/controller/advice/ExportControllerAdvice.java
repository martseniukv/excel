package ru.otus.exportsrv.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.exportsrv.exception.ExportException;
import ru.otus.exportsrv.exception.TaskAlreadyProcessedException;

@ControllerAdvice
public class ExportControllerAdvice {

    @ExceptionHandler(ExportException.class)
    public ResponseEntity<String> handleExportException(ExportException exception){
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(TaskAlreadyProcessedException.class)
    public ResponseEntity<String> handleExportException(TaskAlreadyProcessedException exception){
        return ResponseEntity.ok(exception.getMessage());
    }
}
