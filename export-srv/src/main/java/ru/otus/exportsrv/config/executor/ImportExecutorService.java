package ru.otus.exportsrv.config.executor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Getter
@Component
public class ImportExecutorService {

    private final ExecutorService executorService;

    public ImportExecutorService(@Value("${service-settings.executor.import-pool-size}") Integer poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }
}