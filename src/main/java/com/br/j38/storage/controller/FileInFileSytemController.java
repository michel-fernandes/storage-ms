package com.br.j38.storage.controller;

import com.br.j38.storage.Dto.ResponseFile;
import com.br.j38.storage.Dto.ResponseMessage;
import com.br.j38.storage.entity.FileData;
import com.br.j38.storage.service.FileSystemStorageService;
import com.br.j38.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/file-system")
public class FileInFileSytemController {

    private final FileSystemStorageService fileSystemStorageService;
    @Value("${props.folder.path}")
    private String FOLDER_SYSTEM_PATH;

    public FileInFileSytemController(FileSystemStorageService fileSystemStorageService) {
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFileToDB(@RequestParam("file") MultipartFile file)  {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseMessage(fileSystemStorageService.uploadFileToFileSystem(file)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
        }

    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable UUID id) throws IOException {
        byte[] imageData= fileSystemStorageService.downloadImageFromFileSystem(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID id) throws IOException {
        FileData fileData = fileSystemStorageService.downloadFileFromFileSystem(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getName() + "\"")
                .body(fileData.getData());
    }

    @GetMapping
    public ResponseEntity<List<ResponseFile>> getListFiles() throws IOException{
        List<ResponseFile> files = fileSystemStorageService.getAllFiles().map(fsFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/file-system/")
                    .path(fsFile.getId().toString())
                    .toUriString();

            long size = 0;
            try {
                size = Files.readAllBytes(new File(FOLDER_SYSTEM_PATH+fsFile.getName()).toPath()).length;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new ResponseFile(
                    fsFile.getName(),
                    fileDownloadUri,
                    fsFile.getType(),
                    size);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

}
