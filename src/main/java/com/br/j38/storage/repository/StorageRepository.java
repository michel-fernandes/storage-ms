package com.br.j38.storage.repository;


import com.br.j38.storage.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StorageRepository extends JpaRepository<FileData, Long> {

}
