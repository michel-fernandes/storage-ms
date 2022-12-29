package com.br.j38.storage.repository;

import com.br.j38.storage.entity.FileSytemData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileSystemStorageRepository extends JpaRepository<FileSytemData, UUID> {
}
