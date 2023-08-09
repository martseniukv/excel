package ru.otus.exportsrv.utils.factory;

import ru.otus.exportsrv.model.ErrorDto;

public class ErrorDtoFactory {

    private ErrorDtoFactory() {
    }

    public static ErrorDto getError(String message){
        return ErrorDto.builder()
                .message(message)
                .build();
    }
}