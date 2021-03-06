package com.chung.product.mydocumentCN.storageservice;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
//import com.chung.product.mydocument.vo.MyDocumentInS3;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

/**
 * Author: Chung Ha
 *
 */
@Service
@ConfigurationProperties("mydocument.aws.s3")
public class MyDocumentS3Service implements StorageService{
    private static final Logger logger = LoggerFactory.getLogger(MyDocumentS3Service.class);

    private AmazonS3 s3client;

    private String SUFFIX ="/";

    private String serviceEndpoint;
    private String signingRegion;
    private String systemBucket;
    private String userBucket;
    private String catModelFile;
//    private String bucket = "docbankrepository";

    @Autowired
    UploadRecordRepository uploadRecordRepository;
    /**
     * documentName is
     * @param documentName
     * @return
     */
    public MyDocumentInS3 getDocument(String documentName,String ownerId){
        logger.info("getDocument for "+ownerId+"/"+documentName +" and " + "bucket: "+this.userBucket);

        this.s3client = this.getS3Client();

        MyDocumentInS3 myDocumentInS3 = new MyDocumentInS3();
        myDocumentInS3.setDocument(ownerId+"/"+documentName);
        myDocumentInS3.setBucket(this.userBucket);

        // Get the document from S3
        S3Object s3object = this.s3client.getObject(this.userBucket, ownerId+this.SUFFIX+documentName);
        logger.info("s3object.getKey(): "+s3object.getKey());
        myDocumentInS3.setContentType(s3object.getObjectMetadata().getContentType());
        myDocumentInS3.setEtag(s3object.getObjectMetadata().getETag());
        return myDocumentInS3;
    }

    public MyDocumentInS3 getDocumentByEtag(String etag){
        logger.info("getDocumentByEtag for "+etag);
        Optional<List<UploadRecord>> result = uploadRecordRepository.findByEtag(etag);
        if(!result.isEmpty()){
            List<UploadRecord> uploadRecordsList = result.get();
            Optional<UploadRecord> lastUploadRecord = uploadRecordsList.stream().max(comparing(UploadRecord::getLastModifiedDate));
            if(!lastUploadRecord.isEmpty()) {

                UploadRecord uploadRecord = lastUploadRecord.get();
                MyDocumentInS3 myDocumentInS3 = new MyDocumentInS3();
                myDocumentInS3.setDocument(uploadRecord.getDocName());
                myDocumentInS3.setDocument(uploadRecord.getUserId() + "/" + uploadRecord.getDocName());
                myDocumentInS3.setEtag(uploadRecord.getEtag());
                return myDocumentInS3;
            }else{
                logger.info("No last upload record");
            }
        }else{
            logger.info("uploadResult is empty");
        }

        return null;
    }

    /**
     * This is a method to upload a file to S3 bucket.
     * A new folder will be created with userId as its name.
     * documentName+SUFFIX+documentName is a key name for putting an object in S3.
     * If there ia a folder with the userId already existing, it won't do anything.
     * It returns Entity Tag (ETag) of Object in S3.
     * @param file
     * @param bucketName
     * @param documentName
     * @param ownerId
     */
    public String upload(File file, String bucketName, String documentName, String ownerId){

        logger.info("******************upload**************************");
        logger.info("this.serviceEndpoint: "+this.serviceEndpoint);
        logger.info("this.signingRegion: "+this.signingRegion);

        this.s3client = this.getS3Client();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        String fileExtension = getFileExtenstion(documentName);
        ObjectListing objectListing = this.s3client.listObjects(bucketName);
        for(String s : objectListing.getCommonPrefixes()) {
            logger.info("s: "+s);
        }
        logger.info("-----------------------------");
//        for(S3ObjectSummary os : objectListing.getObjectSummaries()){
//            logger.info("os.getKey(): "+os.getKey());
//
//        }

        if(fileExtension.toLowerCase().equals("jpg")||fileExtension.toLowerCase().equals("jpeg")||fileExtension.toLowerCase().equals("png")){
            logger.info(documentName+" is "+ContentType.IMAGE_JPEG.toString());
            metadata.setContentType(ContentType.IMAGE_JPEG.toString());
        }else if(fileExtension.toLowerCase().equals("pdf")){
            logger.info(documentName+" is application/pdf");
            metadata.setContentType("application/pdf");
        }

        try {
            PutObjectResult por = this.s3client.putObject(bucketName, ownerId+"/"+documentName, file);
            return por.getETag();
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            System.exit(1);
        }
        return null;
    }

    private String getFileExtenstion(String documentOrFileName){
        String[] strings = documentOrFileName.split("\\.");
        logger.info("documentName: "+documentOrFileName);
        if(strings.length==2) {
            logger.info("file name: "+strings[0]);
            logger.info("file extension: "+strings[1]);
            return strings[1];
        }else{
            logger.info("documentOrFileName is not splited");
        }
        return null;
    }

    private AmazonS3 getS3Client(){
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.serviceEndpoint,
                        this.signingRegion))
                .build();
    }

    public void retrieveCatModelFile(){

        AmazonS3 s3client = this.getS3Client();
        S3Object s3object = s3client.getObject(this.systemBucket,this.catModelFile);
        S3ObjectInputStream s3ObjectInputStream = s3object.getObjectContent();
        logger.info("Content-Type: " + s3object.getObjectMetadata().getContentType());
        logger.debug("Content: ");
        try {
            FileUtils.copyInputStreamToFile(s3ObjectInputStream, new File(this.catModelFile));
            if(logger.isDebugEnabled()) {
            displayTextInputStream(s3object.getObjectContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while ((line = reader.readLine()) != null) {
            logger.debug(line);
        }
        System.out.println();
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

    public String getSystemBucket() {
        return systemBucket;
    }

    public void setSystemBucket(String systemBucket) {
        this.systemBucket = systemBucket;
    }

    public String getCatModelFile() {
        return catModelFile;
    }

    public void setCatModelFile(String catModelFile) {
        this.catModelFile = catModelFile;
    }

    public String getUserBucket() {
        return userBucket;
    }

    public void setUserBucket(String userBucket) {
        this.userBucket = userBucket;
    }

    @Override
    public void init() {

    }

    @Override
    public String store(MultipartFile file, String ownerId, String docCatByUser) {
        logger.info("store");
        logger.info("file name: "+file.getName());
        logger.info("original file name: "+file.getOriginalFilename());

        try {
            InputStream in = file.getInputStream();
            final File tempFile = File.createTempFile("aaaaa", "bbbbbb");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(in, out);
            }
            logger.info(file.getName()+ " upload success");
            String etag = upload(tempFile,this.userBucket,file.getOriginalFilename(),ownerId);
            UploadRecord uploadRecord = new UploadRecord();
            uploadRecord.setEtag(etag);
            uploadRecord.setUserId(ownerId);
            uploadRecord.setDocName(file.getOriginalFilename());
            uploadRecord.setDocCategoryByUser(docCatByUser);
            uploadRecordRepository.save(uploadRecord);
            return etag+":"+StorageServiceConstants.UPLOAD_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info(file.getName()+ " failed to upload");
        return StorageServiceConstants.UPLOAD_FAILED;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteObject(String bucket, String object) {
        this.s3client.deleteObject(bucket,object);
    }


}
