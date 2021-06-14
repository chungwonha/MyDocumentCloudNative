package com.chung.product.mydocumentCN.searchservice;

import com.amazonaws.services.cloudsearchdomain.model.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("search")
public class MyDocumentSearchController {

    @Autowired
    MyDocumentSearchService myDocumentSearchService;


    @GetMapping("/{words}")
    public List<String> search(@PathVariable("words") String words){
        MyDocumentInS3[] myDocumentInS3Array = myDocumentSearchService.search(words);
        List<String> docList = Arrays.stream(myDocumentInS3Array).map(MyDocumentInS3::getDocument).collect(Collectors.toList());
        return docList;
    }

}
