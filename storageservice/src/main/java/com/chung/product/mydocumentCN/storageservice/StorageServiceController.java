package com.chung.product.mydocumentCN.storageservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("storage")
public class StorageServiceController {
    Logger logger = LoggerFactory.getLogger(StorageServiceController.class);

    @Autowired
    StorageService storageService;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("ownerId") String ownerId){
        logger.info("upload");

        String status = storageService.store(file,ownerId);

        return status;
    }
}
