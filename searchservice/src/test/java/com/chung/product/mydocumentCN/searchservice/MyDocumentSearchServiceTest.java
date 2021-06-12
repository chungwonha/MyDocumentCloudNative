package com.chung.product.mydocumentCN.searchservice;

import com.amazonaws.services.cloudsearchdomain.model.Hit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class MyDocumentSearchServiceTest {

    @Autowired
    MyDocumentSearchService myDocumentSearchService;

    @Test
    public void testSearch(){
        List<Hit> results = myDocumentSearchService.search("pet");
        results.stream().forEach(h->log.info(h.getId()));

    }
}
