package com.br.j38.storage.service;

import com.br.j38.storage.entity.FileData;
import com.br.j38.storage.entity.FileSytemData;
import com.br.j38.storage.repository.FileSystemStorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService {

    private final FileSystemStorageRepository fileSystemStorageRepository;
    @Value("${props.folder.path}")
    private String FOLDER_SYSTEM_PATH;

    public FileSystemStorageService(FileSystemStorageRepository fileSystemStorageRepository) {
        this.fileSystemStorageRepository = fileSystemStorageRepository;
    }

    public String uploadFileToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_SYSTEM_PATH + file.getOriginalFilename();
        fileSystemStorageRepository.save(FileSytemData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());
        file.transferTo(new File(filePath));
        return "file uploaded successfully : " + filePath;
    }

    public byte[] downloadImageFromFileSystem(UUID id) throws IOException {
        Optional<FileSytemData> fileSytemData = fileSystemStorageRepository.findById(id);
        return Files.readAllBytes(new File(fileSytemData.get().getFilePath()).toPath());
    }

    public FileData downloadFileFromFileSystem(UUID id) throws IOException {
        Optional<FileSytemData> fileSytemData = fileSystemStorageRepository.findById(id);

        if(fileSytemData.isPresent()){
            byte[] bytesFile = Files.readAllBytes(new File(fileSytemData.get().getFilePath()).toPath());
            FileData fileData = new FileData();
            fileData.setId(fileSytemData.get().getId());
            fileData.setName(fileSytemData.get().getName());
            fileData.setData(bytesFile);
            return  fileData;
        }
        return null;
    }

    public Stream<FileSytemData> getAllFiles() {
        List<FileSytemData> files = fileSystemStorageRepository.findAll();
        return  files.stream();
    }

}
