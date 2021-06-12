package com.chung.product.mydocumentCN.searchservice;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClientBuilder;
import com.amazonaws.services.cloudsearchdomain.model.Hit;
import com.amazonaws.services.cloudsearchdomain.model.SearchRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    public List<Hit> search(String words){
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.
                                                              EndpointConfiguration(this.serviceEndpoint,
                                                                                    this.signingRegion);

        AmazonCloudSearchDomain amazonCloudSearchDomain = AmazonCloudSearchDomainClientBuilder.standard().withEndpointConfiguration(endpoint).build();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(words);

        com.amazonaws.services.cloudsearchdomain.model.SearchResult searchResult = amazonCloudSearchDomain.search(searchRequest);

        log.info("-------------------->   "+this.searchEndPointUrl+ "<----------------------------");

        log.info("searchResult.getHits().getFound()--------->>>>>>>"+searchResult.getHits().getFound());
        searchResult.getHits().getHit().stream().forEach(h->log.info("h.getId()------------->"+h.getId()));
        return searchResult.getHits().getHit();
    }
}
