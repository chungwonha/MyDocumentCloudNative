package com.chung.product.mydocumentCN.searchservice;

public class MyDocumentInS3 {

    //document variable is a document key which consists of folder name and document name in S3.
    private String document;

    private String bucket;
    private String contentType;
    private String etag;

//    public BufferedImage getDocumentFromS3() throws Exception{
//
//
//        S3ObjectInputStream inputStream = s3object.getObjectContent();
//        BufferedImage image = ImageIO.read(inputStream);
//
//        return image;
//
//    }

//    public String getDocumentContentType(){
//
//        System.out.println("context type:" +);
//
//    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    //    public static void main(String[] s){
//        String document = "1/IMG_4842.jpg";//2017 Disclosure Report Master.pdf";//IMG_4842.jpg";
//        String bucket = "docbankrepository";
//        MyDocumentInS3 myDocumentInS3 = new MyDocumentInS3(document,bucket);
//
//
//    }

}
