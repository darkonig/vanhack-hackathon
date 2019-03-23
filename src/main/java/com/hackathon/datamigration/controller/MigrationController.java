package com.hackathon.datamigration.controller;

import com.hackathon.datamigration.controller.error.domain.ErrorMessage;
import com.hackathon.datamigration.core.DataProcessor;
import com.hackathon.datamigration.core.DataProcessorFactory;
import com.hackathon.datamigration.domain.DataColumnRequest;
import com.hackathon.datamigration.domain.FileUpdateResponse;
import com.hackathon.datamigration.model.DataTable;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("migration")
public class MigrationController {

    private static final Logger logger = LoggerFactory.getLogger(MigrationController.class);

    @Value("${tmpDir}")
    private String tmpDir;

    public MigrationController() { }

    @PostMapping("/upload")
    public ResponseEntity<?> update(@RequestParam("file") MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = UUID.randomUUID().toString() + "." + extension;
        Path path = Paths.get(tmpDir);
        File tmp = Paths.get(tmpDir, name).toFile();

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            file.transferTo(tmp);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorMessage.builder()
                            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                            .message("Error on saving file.").build());
        }

        return ResponseEntity.ok(FileUpdateResponse.builder()
                .filename(name)
                .build());
    }

    @GetMapping("/{file}")
    public ResponseEntity<?> loadColumns(@PathVariable("file") String fileName) {
        logger.debug("[MigrationController] Load cols from {}", fileName);
        DataProcessor processor = DataProcessorFactory.get(fileName);

        if (processor == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorMessage.builder()
                            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                            .message("No processor to this type of file.").build());
        }

        try {
            return ResponseEntity.ok(processor.getColumns(Paths.get(tmpDir, fileName)));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorMessage.builder()
                            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                            .message("Error on reading file.").build());
        }
    }

    @PostMapping("/{file}")
    public ResponseEntity<?> processFile(@PathVariable("file") String fileName, @RequestBody DataColumnRequest columns) {
        logger.debug("[MigrationController] Processing from {}", fileName);
        DataProcessor processor = DataProcessorFactory.get(fileName);

        if (processor == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorMessage.builder()
                            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                            .message("No processor to this type of file.").build());
        }

        try {
            DataTable dataTable = processor.getDataTable(Paths.get(tmpDir, fileName), columns.getColumns());
            return ResponseEntity.ok(dataTable);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorMessage.builder()
                            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                            .message("Error on reading file.").build());
        }
    }
}
