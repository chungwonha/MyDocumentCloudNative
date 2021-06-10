package com.chung.product.mydocumentCN.parsingservice;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

public class Message {

    private String jobId;
    private String status;
    private String api;
    private String timestamp;
    private JsonNode documentLocation;

    public String getJobId() {
        return jobId;
    }

    @JsonSetter("JobId")
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    @JsonSetter("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    public String getApi() {
        return api;
    }

    @JsonSetter("API")
    public void setApi(String api) {
        this.api = api;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @JsonSetter("Timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public JsonNode getDocumentLocation() {
        return documentLocation;
    }

    @JsonSetter("DocumentLocation")
    public void setDocumentLocation(JsonNode documentLocation) {
        this.documentLocation = documentLocation;
    }
}
