package com.chung.product.mydocumentCN.parsingservice;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: Chung Ha
 */
@Component
@Slf4j
@ConfigurationProperties("mydocument.aws.s3")
public class MyTextractFromPdf extends MyTextractSuper{


    @Override
    public String getText(String document, String bucket) {
        AwsClientBuilder.EndpointConfiguration endpoint = super.getEndPoint();
        S3ObjectInputStream inputStream = null;
        FileOutputStream fos = null;
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withEndpointConfiguration(endpoint).build();
        try {

            /*
              Get an object from S3 Bucket and put in InputStream
              Write to a file
             */
            S3Object object = s3.getObject(bucket, document);
            inputStream = object.getObjectContent();
            String fileName = document.split("/")[1];
            fos = new FileOutputStream(new File(fileName));
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer))!=-1){
                fos.write(buffer, 0, length);
            }

            File file = new File(fileName);
            PDDocument pdfdocument = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();

            String text = pdfStripper.getText(pdfdocument);
            log.info("text from pdf---------------> "+text);
            pdfdocument.close();
            return text;
        }catch (AmazonServiceException | IOException e) {

            System.out.println(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public String getTextType() {
        return null;
    }

    @Override
    public String getSigningRegion() {
        return super.getSigningRegion();
    }

    @Override
    public void setSigningRegion(String signingRegion) {
        super.setSigningRegion(signingRegion);
    }

    @Override
    public String getServiceEndpoint() {
        return super.getServiceEndpoint();
    }

    @Override
    public void setServiceEndpoint(String serviceEndpoint) {
        super.setServiceEndpoint(serviceEndpoint);
    }
}
