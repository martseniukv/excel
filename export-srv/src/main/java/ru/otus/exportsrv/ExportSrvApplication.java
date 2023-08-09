package ru.otus.exportsrv;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "excel-import-srv", description = "Service for export import excel file"))
@SpringBootApplication
public class ExportSrvApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExportSrvApplication.class, args);
    }
}
