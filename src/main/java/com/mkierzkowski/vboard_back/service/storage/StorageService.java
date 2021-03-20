package com.mkierzkowski.vboard_back.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    void init() throws IOException;

    String store(MultipartFile profilePic, String filename, String relativePath);

    Path load(String filename, String relativePath);

    Resource loadAsResource(String filename, String relativePath);

    void delete(String filename, String relativePath);
}
