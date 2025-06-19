package com.fstore.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.fstore.service.utility.ApplicationConstants.PRODUCT_PLACEHOLDER_IMAGE;

@Slf4j
public class ResourceStorageService implements StorageService {
    @Override
    public byte[] load(String fileName, String path) throws IOException {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources", path, fileName);
            return Files.readAllBytes(resourcesPath);
        } catch (Exception exception) {
            log.error("Reading data from resource: %s failed".formatted(fileName));

            Path defaultImagePath = Paths.get("src", "main", "resources", path, PRODUCT_PLACEHOLDER_IMAGE);
            return Files.readAllBytes(defaultImagePath);
        }
    }

    @Override
    public String upload(String fileName, String path, byte[] content) {
        String generatedName = UUID.randomUUID() + "_" + fileName;

        try {
            Path resourcesPath = Paths.get("src", "main", "resources", path, generatedName);
            Files.write(resourcesPath, content);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "An exception occurred while writing data with filename: %s"
                            .formatted(fileName), exception);
        }

        return generatedName;
    }

    @Override
    public boolean delete(String fileName, String path) {
        Path resourcesPath = Paths.get("src", "main", "resources", path, fileName);
        try {
            return Files.deleteIfExists(resourcesPath);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "An exception occurred while deleting data with filename: %s"
                            .formatted(fileName), exception);
        }
    }
}
