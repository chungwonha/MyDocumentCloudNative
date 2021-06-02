package com.chung.product.mydocumentCN.parsingservice;

public interface MyTextract {

    String getText(String document, String bucket);
    String getTextType();

}
