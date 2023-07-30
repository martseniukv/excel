package ru.otus.exportsrv.utils.factory;

import ru.otus.exportsrv.model.ResponseDto;

import java.util.ArrayList;

public class ResponseDtoFactory {

    private ResponseDtoFactory() {
    }

    public static <T> ResponseDto<T> getResponse(T data) {
        return ResponseDto.<T>builder()
                .data(data)
                .errors(new ArrayList<>())
                .build();
    }
}
