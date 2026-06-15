package com.project.filesupload.controller;

import com.project.filesupload.model.FileEntity;
import com.project.filesupload.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String,Object>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileEntity saveFile = fileService.storeFile(file);
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/files/download/")
                .path(saveFile.getDownloadCode())
                .toUriString();
        Map<String,Object> response = new HashMap<>();
        response.put("fileName",saveFile.getFileName());
        response.put("downloadUrl",downloadUrl);
        response.put("status",200);
        response.put("size",saveFile.getFileSize());
        response.put("type",saveFile.getFileType());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/download/{code}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String code) throws IOException {
        Resource resource = fileService.downloadFile(code);
        FileEntity metadata = fileService.getFileMetaData(code);
        String ContentType = metadata.getFileType();
        if(ContentType == null){
            ContentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(ContentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+metadata.getFileName()+"\"")
                .body(resource);
    }
}

