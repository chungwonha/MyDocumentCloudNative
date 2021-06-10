package com.chung.product.mydocumentCN.storageservice;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UploadRecordRepository extends CrudRepository<UploadRecord,Long> {
    Optional<UploadRecord> findByEtag(String etag);
    List<UploadRecord> findByUserId(String userId);
//    Optional<UploadRecord> findByDocumentAndUserId(String document, String userId);

//    @Query("select p from upload_record p where p.doc_name = :docname and p.user_id = :userid")
//    Optional<UploadRecord> findByForenameAndSurname(@Param("docname") String document,
//                                                    @Param("userid") String userId);

    @Transactional
    @Modifying
    @Query("update UploadRecord u set u.etag = :etag where u.docName = :docname and u.userId = :userId")
    void setEtagFor(@Param("etag") String etag,
                    @Param("docname") String docName,
                    @Param("userId") String userId);
}
