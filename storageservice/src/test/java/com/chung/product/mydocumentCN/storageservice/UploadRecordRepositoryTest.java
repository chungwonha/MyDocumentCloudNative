package com.chung.product.mydocumentCN.storageservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UploadRecordRepositoryTest {

    @Autowired
    UploadRecordRepository uploadRecordRepository;

    @Test
    public void testUpdateEtagByDocNameAndUserId(){

        String etag1="test_etag123";
        String docName = "testDocName";
        String userId = "test user id";
        String docCatByUser = "99";
        UploadRecord ur = new UploadRecord();
        ur.setEtag(etag1);
        ur.setDocName(docName);
        ur.setUserId(ur.getEtag());
        ur.setDocCategoryByUser("BILL");
        uploadRecordRepository.save(ur);

        String etag2="test_etag9999";
        uploadRecordRepository.setEtagFor(etag2,docName,userId);

    }
}
