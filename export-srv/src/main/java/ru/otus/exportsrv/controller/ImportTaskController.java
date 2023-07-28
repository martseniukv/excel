package ru.otus.exportsrv.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.exportsrv.model.request.task.ImportTaskAddDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;
import ru.otus.exportsrv.service.task.ImportTaskService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImportTaskController {

    private final ImportTaskService importTaskServiceImpl;

    @GetMapping("/task/{id}")
    public ResponseEntity<ImportTaskDto> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(importTaskServiceImpl.getById(id));
    }

    @PostMapping("/task")
    public ResponseEntity<ImportTaskDto> save(@RequestBody ImportTaskAddDto importTaskAddDto) {
        return ResponseEntity.ok(importTaskServiceImpl.saveTask(importTaskAddDto));
    }
}
