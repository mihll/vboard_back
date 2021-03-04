package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mkierzkowski.vboard_back.service.storage.RelativePaths.PROFILE_PICS;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    StorageService storageService;

    @GetMapping("/profilePic/{filename:.+}")
    public ResponseEntity<Resource> requestProfilePic(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename, PROFILE_PICS.getPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
