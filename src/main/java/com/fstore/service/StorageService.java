package com.fstore.service;

import java.io.IOException;

public interface StorageService {
    byte[] load(String fileName, String path) throws IOException;

    String upload(String fileName, String path, byte[] content);

    boolean delete(String fileName, String path);
}
