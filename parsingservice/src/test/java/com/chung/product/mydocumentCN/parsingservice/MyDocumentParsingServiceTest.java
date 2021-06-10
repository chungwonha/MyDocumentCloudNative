package com.chung.product.mydocumentCN.parsingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyDocumentParsingServiceTest {

    @Autowired
    MyDocumentParsingService myDocumentParsingService;

    @Test
    public void testParse(){

        MyDocumentInS3 myDocumentInS3 = new MyDocumentInS3();
        myDocumentInS3.setEtag("f2c537d7e203661210e62d25cf9012da");
        myDocumentInS3.setBucket("docbankrepository");
        myDocumentInS3.setContentType("png");
        myDocumentInS3.setDocument("99/AEC-PHYSICIAN-BILL-SAMPLE.png");

        myDocumentParsingService.parse(myDocumentInS3);

        MyDocumentInS3 myDocumentInS3_2 = new MyDocumentInS3();
        myDocumentInS3_2.setEtag("bc8a119835ea62b9931248b94d598493");
        myDocumentInS3_2.setBucket("docbankrepository");
        myDocumentInS3_2.setContentType("pdf");
        myDocumentInS3_2.setDocument("99/WeeklyMemo05-27-19.pdf");

        myDocumentParsingService.parse(myDocumentInS3_2);


    }

}
