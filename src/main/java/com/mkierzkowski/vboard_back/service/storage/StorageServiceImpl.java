package com.mkierzkowski.vboard_back.service.storage;

import com.mkierzkowski.vboard_back.config.StorageProperties;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(rootLocation);
    }

    @Override
    public String store(MultipartFile file, String filename, String relativePath) {
        Path pathToSave = rootLocation.resolve(relativePath);

        try (InputStream inputStream = file.getInputStream()) {
            Files.createDirectories(pathToSave);
            Files.copy(inputStream, pathToSave.resolve(filename));
        } catch (IOException e) {
            throw VBoardException.throwException(EntityType.FILE, ExceptionType.FAILED);
        }

        return filename;
    }

    @Override
    public Path load(String filename, String relativePath) {
        Path relativePathToLoad = rootLocation.resolve(relativePath);
        return relativePathToLoad.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, String relativePath) {
        try {
            Path file = load(filename, relativePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw VBoardException.throwException(EntityType.FILE, ExceptionType.ENTITY_NOT_FOUND, filename);
            }
        } catch (MalformedURLException e) {
            throw VBoardException.throwException(EntityType.FILE, ExceptionType.ENTITY_NOT_FOUND, filename);
        }
    }

    @Override
    public void delete(String filename, String relativePath) {
        Path pathToDelete = rootLocation.resolve(relativePath);

        try {
            Files.delete(pathToDelete.resolve(filename));
        } catch (IOException e) {
            throw VBoardException.throwException(EntityType.PROFILE_PIC, ExceptionType.FAILED);
        }
    }
}
