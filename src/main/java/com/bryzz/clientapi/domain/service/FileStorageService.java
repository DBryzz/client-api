package com.bryzz.clientapi.domain.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    public void init();

    public String save(MultipartFile file, String sellerId);

    public Resource load(String filename);

    public void deleteAll();

    public void deleteFile(String fileUrl, HttpServletRequest request);

    public Stream<Path> loadAll();
}
