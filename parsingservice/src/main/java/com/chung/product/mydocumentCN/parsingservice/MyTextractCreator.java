package com.chung.product.mydocumentCN.parsingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties("mydocument.aws.textract")
public class MyTextractCreator {

    private static final Logger logger = LoggerFactory.getLogger(MyTextractCreator.class);
    private String serviceEndpoint;
    private String signingRegion;

    @Autowired
    MyTextractFromPdf myTextractFromPdf;
    @Autowired
    MyTextractFromImage myTextractFromImage;

    /**
     * This is to return an appropriate textract object depending on a file extension.
     * Initially tried to use content type but for some reason, the content-type in S3 Object in S3 is not set correctly.
     * @param myDocumentInS3
     * @return
     */
    public MyTextract getMyTextract(MyDocumentInS3 myDocumentInS3){

        String fileExtenstion = MyDocumentUtil.getFileExtenstion(myDocumentInS3.getDocument());
        String contentType =myDocumentInS3.getContentType();
        if(fileExtenstion.toLowerCase().equals("pdf")){
            return this.myTextractFromPdf;//new MyTextractFromPdf(serviceEndpoint,signingRegion);
        }else if(fileExtenstion.toLowerCase().equals("jpeg")||fileExtenstion.toLowerCase().equals("jpg")||fileExtenstion.toLowerCase().equals("png")) {
            return this.myTextractFromImage;//new MyTextractFromImage(serviceEndpoint,signingRegion);
        }
//        else if(myDocumentInS3.getContentType().equals("application/octet-stream")){
//            return new MyTextractFromImage();
//        }
        return null;
    }

    @Bean
    public MyTextractFromPdf getMyTextractFromPdf(){
        return this.myTextractFromPdf;

    }

    @Bean
    public MyTextractFromImage getMyTextractFromImage(){
        return this.myTextractFromImage;

    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getSigningRegion() {
        return signingRegion;
    }

    public void setSigningRegion(String signingRegion) {
        this.signingRegion = signingRegion;
    }
}
