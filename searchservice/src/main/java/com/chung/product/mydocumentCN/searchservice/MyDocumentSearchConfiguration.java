package com.chung.product.mydocumentCN.searchservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data @AllArgsConstructor @NoArgsConstructor
public class MyDocumentSearchConfiguration {
    private String storageServiceEndPoint;

}
