package com.chung.product.mydocumentCN.storageservice;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UploadRecordRepository extends CrudRepository<UploadRecord,Long> {
    Optional<UploadRecord> findByDocId(String docId);
    List<UploadRecord> findByUserId(String docId);

}
