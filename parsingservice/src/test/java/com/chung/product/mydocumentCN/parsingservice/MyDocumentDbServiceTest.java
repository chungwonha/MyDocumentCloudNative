package com.chung.product.mydocumentCN.parsingservice;

import com.amazonaws.services.codeartifact.model.HashAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class MyDocumentDbServiceTest {

    @Autowired
    MyDynamoDbService myDynamoDbService;

@Test
public void testAddItem(){

    Document document = new Document();
    document.setEtag("testtesttestETAGETAG");
    document.setText("test TEXT TEXT TEXT");
    HashMap<String,String> data = new HashMap<>();
    data.put("etag","testtesttestETAGETAG");
    data.put("text","test TEXT TEXT TEXT");
    myDynamoDbService.addItem(data);
}

}
