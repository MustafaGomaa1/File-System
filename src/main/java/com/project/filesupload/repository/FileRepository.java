package com.project.filesupload.repository;

import com.project.filesupload.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {
    Optional<FileEntity>findByDownloadCode(String downloadCode);

    Optional<FileEntity> findByFileName(String fileName);

    List<FileEntity> findByFileType(String fileType);
}
