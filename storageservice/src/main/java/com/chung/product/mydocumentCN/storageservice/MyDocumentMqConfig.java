package com.chung.product.mydocumentCN.storageservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="mydocument.mq.upload")
public class MyDocumentMqConfig {

    private String topicExchangeName;
    private String queueName;
    private String routingKey;
    private boolean mqEnabled;

    public String getTopicExchangeName() {
        return topicExchangeName;
    }

    public void setTopicExchangeName(String topicExchangeName) {
        this.topicExchangeName = topicExchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }


}
