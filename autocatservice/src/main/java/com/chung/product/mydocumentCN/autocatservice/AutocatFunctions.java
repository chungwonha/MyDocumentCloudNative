package com.chung.product.mydocumentCN.autocatservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AutocatFunctions {

    @Autowired
    MyDocumentCategorizer myDocumentCategorizer;

    @Bean
    public Consumer<AutocatMessage> trainautocat(AutocatService autocatService){
        return autocatMessage -> {
            log.info("training autocat with etag "+autocatMessage.getEtag());
            myDocumentCategorizer.setModelFileName("doc_cat_model2.bin");
            myDocumentCategorizer.trainModel("temp.train");
        };
    }

}
