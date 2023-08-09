package ru.otus.exportsrv.utils.factory;

import ru.otus.exportsrv.model.ErrorDto;
import ru.otus.exportsrv.model.ResponseDto;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.exportsrv.model.enums.Status.OK;
import static ru.otus.exportsrv.model.enums.Status.FAILED;

public class ResponseDtoFactory {

    private ResponseDtoFactory() {
    }

    public static <T> ResponseDto<T> getOkResponse(T data) {
        return ResponseDto.<T>builder()
                .data(data)
                .status(OK)
                .errors(new ArrayList<>())
                .build();
    }

    public static <T> ResponseDto<T> getFailedResponse(List<ErrorDto> errors) {
        return ResponseDto.<T>builder()
                .status(FAILED)
                .errors(errors)
                .build();
    }
}
