package com.chung.product.mydocumentCN.parsingservice;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
@Slf4j
public class MyDocumentParsingService {

    private String serviceEndpoint;
    private String signingRegion;
    @Autowired
    MyTextractCreator myTextractCreator;

    /**
     * This is to persist metadata for a document uploaded.
     * If the document is an image file, texts in the file will be saved with other data.
     * If the document is PDF, a job ID will be returned for Textract to extract text asynchronously.
     * @param myDocumentInS3
     */
    public void parse(MyDocumentInS3 myDocumentInS3) {
        log.info("-------------------persistDocument begins-----------------");

        MyTextract myTextract = myTextractCreator.getMyTextract(myDocumentInS3);

        if (myTextract.getTextType().equals(MyDocumentConstants.IMAGE)){
            String text = myTextract.getText(myDocumentInS3.getDocument(),myDocumentInS3.getBucket());
            log.info("text======>>  "+text);

        }else if(myTextract.getTextType().equals(MyDocumentConstants.PDF)){
            String jobId = myTextract.getText(myDocumentInS3.getDocument(),myDocumentInS3.getBucket());
            log.info("after myTextract.getText for PDF");
            if (jobId != null) {
                log.info("jobId in persistDocument method: " + jobId);

            } else {
                log.info("jobId is null");
            }
        }
        log.info("-------------------persistDocument ends-----------------");
    }
}
