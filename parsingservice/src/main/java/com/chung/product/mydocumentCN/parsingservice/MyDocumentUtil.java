package com.chung.product.mydocumentCN.parsingservice;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.textract.model.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class MyDocumentUtil {
    private static final Logger logger = LoggerFactory.getLogger(MyDocumentUtil.class);

    public static String getOwnerIdFromDocumentKey(String documentkey){
        StringBuffer stringBuffer = new StringBuffer();
        String[] s= documentkey.split("/");
        return s[0];
    }

    public static String getFileExtenstion(String documentOrFileName){
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

    public static String getTextFromBlocks(List<Block> blocks){
        String text = "";

        for (Block block : blocks) {
            text = text +" "+ block.getText();
        }

        return text.replaceAll("null","");
    }


    public static void main(String[] s){
        logger.debug(getOwnerIdFromDocumentKey("1/sdfdsf.pdf"));
    }


}
