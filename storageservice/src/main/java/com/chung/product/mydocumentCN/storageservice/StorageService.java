package com.chung.product.mydocumentCN.storageservice;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file, String ownerId);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void deleteObject(String bucket, String object);

    public MyDocumentInS3 getDocument(String documentName, String ownerId);
}
