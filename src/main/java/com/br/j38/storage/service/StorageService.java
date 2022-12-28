package com.br.j38.storage.service;

import com.br.j38.storage.repository.*;
import com.br.j38.storage.entity.FileData;
import com.br.j38.storage.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class StorageService {

    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public String uploadCompressFileToDb(MultipartFile file) throws IOException {

        storageRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(FileUtils.compressImage(file.getBytes())).build());
        return "file uploaded successfully : " + file.getOriginalFilename();
    }

    public byte[] downloadImageFromDb(Long id){
        Optional<FileData> dbImageData = storageRepository.findById(id);
        return FileUtils.decompressImage(dbImageData.get().getData());
    }

    public FileData downloadFileFromDb(Long id){
        Optional<FileData> dbFileData = storageRepository.findById(id);

        if(dbFileData.isPresent()){
            byte[] bytesFile = FileUtils.decompressImage(dbFileData.get().getData());
            FileData dbFile = dbFileData.get();
            dbFile.setData(bytesFile);
            return  dbFile;
        }
        return null;
    }

    public Stream<FileData> getAllFiles() {
        List<FileData> files = storageRepository.findAll();
        files.forEach(p->p.setData(FileUtils.decompressImage(p.getData())));
        return  files.stream();
    }

}
