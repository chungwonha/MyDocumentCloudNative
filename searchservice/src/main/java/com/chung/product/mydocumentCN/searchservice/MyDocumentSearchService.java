package com.chung.product.mydocumentCN.searchservice;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClientBuilder;
import com.amazonaws.services.cloudsearchdomain.model.Hit;
import com.amazonaws.services.cloudsearchdomain.model.SearchRequest;
import com.fasterxml.jackson.core.JsonToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Array;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@Slf4j
@ConfigurationProperties(prefix = "mydocument.aws.cloudsearch")
@Data
public class MyDocumentSearchService {

    private String searchEndPointUrl;
    private String serviceEndpoint;
    private String signingRegion;
    private String endpoint;

    @Autowired
    MyDocumentSearchConfiguration myDocumentSearchConfiguration;

    public MyDocumentInS3[] search(String words){
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.
                                                              EndpointConfiguration(this.serviceEndpoint,
                                                                                    this.signingRegion);

        AmazonCloudSearchDomain amazonCloudSearchDomain = AmazonCloudSearchDomainClientBuilder.standard().withEndpointConfiguration(endpoint).build();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(words);



        com.amazonaws.services.cloudsearchdomain.model.SearchResult searchResult = amazonCloudSearchDomain.search(searchRequest);

        log.info("-------------------->   "+this.searchEndPointUrl+ "<----------------------------");

        log.info("searchResult.getHits().getFound()--------->>>>>>>"+searchResult.getHits().getFound());
//        searchResult.getHits().getHit().stream().forEach(h->log.info("h.getId()------------->"+h.getId()));
        String concatenatedIds = searchResult.getHits().getHit().stream().map(Hit::getId).reduce("",(a,b)->a+","+b);
        RestTemplate restTemplate = new RestTemplate();
        concatenatedIds = concatenatedIds.replaceFirst(",","");
        log.info("----------->>>>>>>>>>>>>>>>>>"+myDocumentSearchConfiguration.getStorageServiceEndPoint()+"/"+concatenatedIds+"<<<<<<<<<<<<<<<------------");
        MyDocumentInS3[] myDocumentInS3Array = restTemplate.getForObject(myDocumentSearchConfiguration.getStorageServiceEndPoint()+"/"+concatenatedIds,MyDocumentInS3[].class);
        log.info("Integer.toString(myDocumentInS3List.length)-------------->"+Integer.toString(myDocumentInS3Array.length));


        return myDocumentInS3Array;
    }

    @Bean
    @ConfigurationProperties(prefix="mydocument.service")
    public MyDocumentSearchConfiguration configMyDocumentSearch(){
        return new MyDocumentSearchConfiguration();
    }


}
