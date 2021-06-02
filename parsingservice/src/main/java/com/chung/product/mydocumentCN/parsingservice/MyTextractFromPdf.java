package com.chung.product.mydocumentCN.parsingservice;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@ConfigurationProperties("mydocument.aws.textract")
public class MyTextractFromPdf extends MyTextractSuper{

    private static final Logger logger = LoggerFactory.getLogger(MyTextractFromPdf.class);

    private String serviceEndpoint;
    private String signingRegion;

    //jobId: dc47b2156cdaf4de4304a86b74fc2d3bf3b600b4f1091789d8d15df0448e3c91 : DocumentDetectionToken
    //jobId: fbc214cf1d14faf317006e02a33863bce34e8240386eb223a1b80969e0c9eae0 : DocumentDetectionToken123
    //jobId: dc56bd6b7aa3d466e1d9d1f1e0df94377d68bfe240dde77113145b11119fe17d : DocumentDetectionTokenForDPOR2017
    //jobId: f6c0307b51e5bae023ba80aaf121badcb53e09cf198438458c3f0d4cb09530ed : MyTextractClientToken
    //jobid: cba77b9a470e57df2a4ee0193f93071505f55c3371efc0e20443773e1b4d3515 : MyTextractClientToken123
    //jobid: 311d562a5b1cae89d7c2684e93166226ec4cb0e468395d19b52a601b038eaf38 : MyTextractClientToken123999999
    //jobid: 63beee0b3a16516240058c1a68e8538c6593727c85a5d380a4d24ed364c1e550 : MyTextractClientToken1239999990000000000333333333333333333330

    public String getText(String document, String bucket){
        // Call AnalyzeDocument
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
                this.serviceEndpoint, this.signingRegion);

        AmazonTextract client = AmazonTextractClientBuilder.standard()
                .withEndpointConfiguration(endpoint).build();

        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date();
        String myTextractClientToken = "MyTextractClientToken"+now.getTime();
        logger.info("myTextractClientToken: "+myTextractClientToken);

        StartDocumentTextDetectionRequest startDocumentTextDetectionRequest = new StartDocumentTextDetectionRequest()
                .withDocumentLocation(new DocumentLocation().withS3Object(new S3Object().withName(document).withBucket(bucket)))
                .withClientRequestToken(myTextractClientToken)
                .withNotificationChannel(new NotificationChannel()
                                                .withSNSTopicArn("arn:aws:sns:us-east-1:079960855147:TextractPDFJobTopic")//arn:aws:sns:us-east-1:079960855147:dynamodb")
                                                .withRoleArn("arn:aws:iam::079960855147:role/DocumentBankTextractRole"));

        StartDocumentTextDetectionResult result = client.startDocumentTextDetection(startDocumentTextDetectionRequest);
        String jobId = result.getJobId();
//        MySqsMonitoringForPdf mySqsMonitoringForPdf = new MySqsMonitoringForPdf(jobId);
//        mySqsMonitoringForPdf.setMonitoringSwtich(true);
//        mySqsMonitoringForPdf.runMontioring();

        logger.info("getText after mySqsMonitoringForPdf.runMontioring()");

        return jobId;
    }

    private long generateClientToken(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    @Override
    public String getTextType() {
        return MyDocumentConstants.PDF;
    }

    public String getTextByJobId(String jobId){

        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
                this.serviceEndpoint, this.signingRegion);

        AmazonTextract client = AmazonTextractClientBuilder.standard()
                .withEndpointConfiguration(endpoint).build();

        GetDocumentTextDetectionResult getDocumentTextDetectionResult = client.getDocumentTextDetection(new GetDocumentTextDetectionRequest().withJobId(jobId));


        logger.info("jobid - "+jobId+" status: "+getDocumentTextDetectionResult.getJobStatus());

        List<Block> blocks = getDocumentTextDetectionResult.getBlocks();
        logger.info("blocks size: "+blocks.size());

        String text = MyDocumentUtil.getTextFromBlocks(blocks);

        return text;
    }

    public void showTextractJob(String jobId){
        AmazonTextract client = AmazonTextractClientBuilder.standard()
                .withEndpointConfiguration(super.getEndPoint()).build();

        GetDocumentTextDetectionResult getDocumentTextDetectionResult = client.getDocumentTextDetection(new GetDocumentTextDetectionRequest().withJobId(jobId));


        logger.info("jobid - "+jobId+" status: "+getDocumentTextDetectionResult.getJobStatus());

        List<Block> blocks = getDocumentTextDetectionResult.getBlocks();
        logger.info("blocks size: "+blocks.size());

        String text = MyDocumentUtil.getTextFromBlocks(blocks);

        logger.debug(text);
    }

    @Override
    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    @Override
    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    @Override
    public String getSigningRegion() {
        return signingRegion;
    }

    @Override
    public void setSigningRegion(String signingRegion) {
        this.signingRegion = signingRegion;
    }

    //    public static void main(String[] s){
//        MyTextractFromPdf myTextractFromPdf = new MyTextractFromPdf();
//        myTextractFromPdf.showTextractJob("f6c0307b51e5bae023ba80aaf121badcb53e09cf198438458c3f0d4cb09530ed");
//    }

}
