package com.project.filesupload.service.impl;

import com.project.filesupload.model.FileEntity;
import com.project.filesupload.model.RandomCodeGenerator;
import com.project.filesupload.repository.FileRepository;
import com.project.filesupload.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final Path fileStorageLocation;

    public FileServiceImpl(FileRepository fileRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FileEntity storeFile(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(originalFilename.contains("..")){
                throw new RuntimeException("Filename contains invalid path sequence " + originalFilename);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        String uniqueCode = RandomCodeGenerator.generatedCode();
        String storedFilename = uniqueCode+"_"+ file.getOriginalFilename();
        Path path = this.fileStorageLocation.resolve(storedFilename);
        Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
        FileEntity fileEntity = FileEntity.builder()
                .fileName(originalFilename)
                .filePath(path.toString())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .downloadCode(uniqueCode)
                .downloadCount(0L)
                .build();
        return fileRepository.save(fileEntity);
    }

    @Override
    public Resource downloadFile(String downloadCode) {
        FileEntity fileEntity = fileRepository.findByDownloadCode(downloadCode).orElseThrow(()-> new RuntimeException("File NOt Found"));
        try {
            Path filePath = Paths.get(fileEntity.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                fileEntity.setDownloadCount(fileEntity.getDownloadCount()+1);
                fileRepository.save(fileEntity);
                return resource;
            }
            else  {
                throw new RuntimeException("File Not Found");
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FileEntity getFileMetaData(String downloadCode) {
        return   fileRepository.findByDownloadCode(downloadCode).orElseThrow(()-> new RuntimeException("File Not Found"));
    }
}
