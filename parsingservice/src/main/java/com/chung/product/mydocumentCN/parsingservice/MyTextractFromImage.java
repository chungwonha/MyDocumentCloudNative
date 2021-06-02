package com.chung.product.mydocumentCN.parsingservice;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@ConfigurationProperties("mydocument.aws.textract")
public class MyTextractFromImage extends MyTextractSuper{

    private static final Logger logger = LoggerFactory.getLogger(MyTextractFromImage.class);

    private String serviceEndpoint;
    private String signingRegion;


    public String getText(String document, String bucket){
        // Call AnalyzeDocument
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(this.serviceEndpoint,
                                                                                                     this.signingRegion);

        AmazonTextract client = AmazonTextractClientBuilder.standard().withEndpointConfiguration(endpoint).build();

        AnalyzeDocumentRequest request = new AnalyzeDocumentRequest()
                .withFeatureTypes("TABLES","FORMS")
                .withDocument(new Document().withS3Object(new S3Object().withName(document).withBucket(bucket)));

        AnalyzeDocumentResult result = client.analyzeDocument(request);

        logger.debug("result.toString(): "+result.toString());
        List<Block> blocks = result.getBlocks();

        String text = MyDocumentUtil.getTextFromBlocks(blocks);

        return text;
    }

    @Override
    public String getTextType() {
        return MyDocumentConstants.IMAGE;
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
}
