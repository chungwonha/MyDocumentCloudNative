package com.chung.product.mydocumentCN.parsingservice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
@ConfigurationProperties(prefix="mydocument.service.storage")
@Data
public class Receiver {

    @Autowired
    MyDocumentParsingService myDocumentParsingService;

    private String serviceEndPoint;

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        log.info("Received <" + message + ">");
        String[] msgTokens = message.split(":");

        RestTemplate restTemplate = new RestTemplate();
        log.info("-------------------->   "+this.serviceEndPoint+"/"+msgTokens[0]+ "<----------------------------");
        MyDocumentInS3 myDocumentInS3 = restTemplate.getForObject(this.serviceEndPoint+"/"+msgTokens[0],
                                                                    MyDocumentInS3.class);
        log.info("myDocumentInS3.getDocument():"+myDocumentInS3.getDocument());
        log.info("myDocumentInS3.getContentType(): "+myDocumentInS3.getContentType());
        myDocumentParsingService.parse(myDocumentInS3);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}