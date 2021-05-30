package com.chung.product.mydocumentCN.storageservice;



public class ObjectUploadedMessage {

    private String objectKey;
    private String status;

    public ObjectUploadedMessage(){

    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
