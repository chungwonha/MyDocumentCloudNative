package com.chung.product.mydocumentCN.parsingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTextractFromPdfText {

    @Autowired
    MyTextractFromPdf myTextractFromPdf;

    @Test
    public void testGetText(){
        myTextractFromPdf.getText("99/PetResume.pdf","docbankrepository");
    }
}
