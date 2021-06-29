package com.bryzz.clientapi.domain.service.impl;


import com.bryzz.clientapi.domain.service.FileStorageService;
import com.bryzz.clientapi.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private static Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final Path root = Paths.get("src/main/resources/static/src-code-dir");


    @Override
    public void init() {
        try {
            if (Files.exists(root)) {
                logger.info(root.toString() + " already exist");
                System.out.println(root.toString() + " already exist");
            } else {
                Files.createDirectory(root);
            }

        } catch (IOException e) {
            logger.info("Could not initialize folder for upload!");
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file, Long deployerId, String appName) {
        Path imagePath = Paths.get("src/main/resources/static/src-code-dir/"+deployerId+"/"+appName);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        /** creating source code folder **/
        try {
            if (Files.exists(imagePath)) {
                logger.info(imagePath.toString() + " already exist");
                System.out.println(imagePath.toString() + " already exist");
            } else {
                Files.createDirectories(imagePath);
            }

        } catch (IOException e) {
            String msg = "Could not create src/main/resources/static/src-code-dir/"+deployerId+"/"+appName;
            logger.info(msg);
            throw new RuntimeException(msg);
        }


        /** saving source code **/

        Path imageUrl = imagePath.resolve(fileName);

        try {
            Resource resource = new UrlResource(imageUrl.toUri());
            if (resource.exists() || resource.isReadable()) {
                return "Error: An image with the same name already exist";
            } else {

                Files.copy(file.getInputStream(), imageUrl, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: Could not store the file. Error: " + e.getMessage());
        }

        String productUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/apps/" + imageUrl.toString())
                .toUriString();

        return productUri;
    }

    @Override
    public Resource load(Long deployerId, String appName, String filename) {
        try {
            Path file = root.resolve(deployerId+"/"+appName+"/"+filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public void deleteFile(Long deployerId, String appName, String fileUrl, HttpServletRequest request) {

        String filename = StringUtils.getFilename(fileUrl).replace("%20", " ");

        String normalFilename = StringUtils.getFilename(fileUrl);

        System.out.println(fileUrl + " --> " + fileUrl.length());
        System.out.println(filename + " --> " + filename.length());
        System.out.println(normalFilename + " --> " + normalFilename.length());


        try {
            Path file = root.resolve(deployerId+"/"+appName+"/"+filename).normalize();
            Path dir = root.resolve(deployerId+"/"+appName).normalize();
            Resource resource = new UrlResource(file.toUri());
            File file1 = new File(file.toString());
            if (resource.exists() || resource.isReadable()) {
                file1.delete();
                Files.delete(dir);
            } else {
                throw new ResourceNotFoundException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

}
