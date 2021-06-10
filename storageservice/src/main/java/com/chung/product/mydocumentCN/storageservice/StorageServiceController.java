package com.chung.product.mydocumentCN.storageservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("storage")
public class StorageServiceController {
    Logger logger = LoggerFactory.getLogger(StorageServiceController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MyDocumentUploadMessenger myDocumentUploadMessenger;

    @Autowired
    StorageService storageService;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("ownerId") String ownerId){
        logger.info("upload");

        String status = storageService.store(file,ownerId);
        ObjectUploadedMessage oum = new ObjectUploadedMessage();
        oum.setObjectKey(ownerId+"/"+file.getOriginalFilename());
        oum.setStatus(status);
        logger.info(myDocumentUploadMessenger ==null?"myDocumentMessenger NULL":"myDocumentMessenger NOT NULL");
        logger.info("rabbitTemplate.getConnectionFactory().getUsername(): "+rabbitTemplate.getConnectionFactory().getUsername());
        rabbitTemplate.convertAndSend(myDocumentUploadMessenger.getTopicExchangeName(),
                                      myDocumentUploadMessenger.getRoutingKey(),
                                     ownerId+"/"+file.getOriginalFilename()+":"+status);
        return status;
    }

    @GetMapping("/{ownerId}/{documentName}")
    public MyDocumentInS3 getMyDocument(@PathVariable("documentName") String documentName, @PathVariable("ownerId") String ownerId){
        MyDocumentInS3 myDocumentInS3 = storageService.getDocument(documentName,ownerId);
        logger.info("myDocumentInS3.getContentType(): "+myDocumentInS3.getContentType());
        return myDocumentInS3;
    }

}
