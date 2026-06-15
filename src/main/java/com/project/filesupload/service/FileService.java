package com.project.filesupload.service;

import com.project.filesupload.model.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileEntity storeFile(MultipartFile file) throws IOException;
    Resource downloadFile(String downloadCode);
    FileEntity getFileMetaData(String downloadCode);
}
