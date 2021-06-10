package com.chung.product.mydocumentCN.parsingservice;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Body {

    private String type;
    private String messageId;
    private String topicArn;
    private String message;
    private String timestamp;
    private String signatureVersion;
    private String signature;
    private String signingCertURL;
    private String unsubscribeURL;


    @JsonGetter("Type")
    public String getType() {
        return type;
    }

    @JsonSetter("Type")
    public void setType(String type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    @JsonSetter("MessageId")
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopicArn() {
        return topicArn;
    }

    @JsonSetter("TopicArn")
    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    @JsonGetter("Message")
    public String getMessage() {
        return message;
    }

    @JsonSetter("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @JsonSetter("Timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignatureVersion() {
        return signatureVersion;
    }

    @JsonSetter("SignatureVersion")
    public void setSignatureVersion(String signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    public String getSignature() {
        return signature;
    }

    @JsonSetter("Signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSigningCertURL() {
        return signingCertURL;
    }

    @JsonSetter("SigningCertURL")
    public void setSigningCertURL(String signingCertURL) {
        this.signingCertURL = signingCertURL;
    }

    public String getUnsubscribeURL() {
        return unsubscribeURL;
    }

    @JsonSetter("UnsubscribeURL")
    public void setUnsubscribeURL(String unsubscribeURL) {
        this.unsubscribeURL = unsubscribeURL;
    }
}
