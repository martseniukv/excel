package ru.otus.exportsrv.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.exportsrv.exception.ExportException;
import ru.otus.exportsrv.exception.NotFoundException;
import ru.otus.exportsrv.exception.TaskAlreadyProcessedException;
import ru.otus.exportsrv.model.ResponseDto;

import java.util.List;

import static ru.otus.exportsrv.utils.factory.ErrorDtoFactory.getError;
import static ru.otus.exportsrv.utils.factory.ResponseDtoFactory.getFailedResponse;

@ControllerAdvice
public class ExportControllerAdvice {

    @ExceptionHandler(ExportException.class)
    public ResponseEntity<String> handleExportException(ExportException exception){
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(TaskAlreadyProcessedException.class)
    public ResponseEntity<ResponseDto<Object>> handleExportException(TaskAlreadyProcessedException exception){
        var failedResponse = getFailedResponse(List.of(getError(exception.getMessage())));
        return new ResponseEntity<>(failedResponse, getHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDto<Object>> handleIllegalStateException(IllegalStateException exception){
        var failedResponse = getFailedResponse(List.of(getError(exception.getMessage())));
        return new ResponseEntity<>(failedResponse, getHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Object>> handleIllegalArgumentException(IllegalArgumentException exception){
        var failedResponse = getFailedResponse(List.of(getError(exception.getMessage())));
        return new ResponseEntity<>(failedResponse, getHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<Object>> handleNotFoundException(NotFoundException exception){
        var failedResponse = getFailedResponse(List.of(getError(exception.getMessage())));
        return new ResponseEntity<>(failedResponse, getHeaders(), HttpStatus.OK);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
