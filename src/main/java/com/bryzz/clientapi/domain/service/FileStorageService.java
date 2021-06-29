package com.bryzz.clientapi.domain.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    public void init();

    public String save(MultipartFile file, Long deployerId, String appName);

    public Resource load(Long userId, String appName, String filename);

    public void deleteAll();

    public void deleteFile(Long deployerId, String appName, String fileUrl, HttpServletRequest request);

    public Stream<Path> loadAll();
}
