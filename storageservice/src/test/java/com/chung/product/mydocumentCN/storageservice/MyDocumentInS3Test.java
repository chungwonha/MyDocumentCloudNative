package com.chung.product.mydocumentCN.storageservice;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.File;

@SpringBootTest
@ConfigurationProperties(prefix="mydocument.aws.s3")
public class MyDocumentInS3Test {
    private static final Logger logger = LoggerFactory.getLogger(MyDocumentInS3Test.class);

    @Autowired
    MyDocumentS3Service myDocumentS3Service;

    String userBucket;
//    String documentToUpload = "IMG_4862111.JPG";
    String documentToUpload = "PetResume.pdf";
    String userId = "99";

    @Test
    public void testUpload(){
        logger.info("testUpload begins---------------------");
        File file = new File(documentToUpload);
        this.myDocumentS3Service.upload(file,this.userBucket,documentToUpload,userId);
        MyDocumentInS3 myDocumentInS3 = myDocumentS3Service.getDocument(documentToUpload,userId);
        String doc = myDocumentInS3.getDocument();
        Assert.isTrue(doc.equals(userId+"/"+documentToUpload),"Document is good");

        MyDocumentInS3 myDocumentInS3_1 = this.myDocumentS3Service.getDocumentByEtag(myDocumentInS3.getEtag());
        logger.info("etag: "+myDocumentInS3_1.getEtag()+" : "+"Document: "+myDocumentInS3_1.getDocument());
        Assert.isTrue(myDocumentInS3_1.getDocument().equals(userId+"/"+documentToUpload),"Document verified by etag");
        Assert.isTrue(myDocumentInS3_1.getEtag().equals(myDocumentInS3.getEtag()),"Document etag verified");
        logger.info("cleanup started...");
        myDocumentS3Service.deleteObject(userBucket,userId+"/"+documentToUpload);
        logger.info("cleanup done.");

        logger.info("testUpload ends---------------------");
    }




//    @After
//    public void cleanup(){
//        logger.info("cleanup started...");
//        myDocumentS3Service.deleteObject(bucket,userId+"/"+documentToUpload);
//        logger.info("cleanup done.");
//    }

    @Test
    public void testGetCategorizeModelFile(){
        this.myDocumentS3Service.retrieveCatModelFile();
    }

    public String getUserBucket() {
        return userBucket;
    }

    public void setUserBucket(String userBucket) {
        this.userBucket = userBucket;
    }

}
