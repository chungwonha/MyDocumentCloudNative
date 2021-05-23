package com.chung.product.mydocumentCN.storageservice;

import com.chung.product.mydocumentCN.storageservice.MyDocumentInS3;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.File;

@SpringBootTest
public class MyDocumentInS3Test {
    private static final Logger logger = LoggerFactory.getLogger(MyDocumentInS3Test.class);

    @Autowired
    MyDocumentS3Service myDocumentS3Service;

    String bucket = "docbankrepository";

    @Test
    public void testUpload(){
        logger.info("testUpload begins---------------------");
        String documentKey = "IMG_4862111.JPG";
        File file = new File(documentKey);
        String userId = "99";
        this.myDocumentS3Service.upload(file,this.bucket,documentKey,userId);
        logger.info("testUpload ends---------------------");
    }


    @Test
    public void testGetDocument(){
        logger.info("testGetDocument begins---------------------");
        String document = "IMG_4842.jpg";//2017 Disclosure Report Master.pdf";//IMG_4842.jpg";
        String userId = "1";
        MyDocumentInS3 myDocumentInS3 = myDocumentS3Service.getDocument(document,bucket,userId);
        Assert.isTrue(myDocumentInS3.getDocument().equals(document),"Document is good");
        Assert.isTrue(myDocumentInS3.getBucket().equals(bucket),"Bucket is good");
        logger.info("testGetDocument ends------------------------");
    }


    @Test
    public void testGetCategorizeModelFile(){
        this.myDocumentS3Service.retrieveCatModelFile();
    }




}
