package com.bryzz.clientapi.domain.service.impl;


import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.dto.AppSourceDTO;
import com.bryzz.clientapi.domain.dto.AppSourcePostDTO;
import com.bryzz.clientapi.domain.model.AppSource;
import com.bryzz.clientapi.domain.model.User;
import com.bryzz.clientapi.domain.repository.AppSourceRepository;
import com.bryzz.clientapi.domain.repository.UserRepository;
import com.bryzz.clientapi.domain.service.AppService;
import com.bryzz.clientapi.domain.service.FileStorageService;
import com.bryzz.clientapi.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppServiceImpl implements AppService {

    private static Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);


//    private final Path root = Paths.get("uploads");

    private AppSourceRepository appSourceRepository;
    private FileStorageService storageService;
    private UserRepository userRepository;


    @Autowired
    public AppServiceImpl(AppSourceRepository appSourceRepository, FileStorageService storageService,
                              UserRepository userRepository) {
        this.appSourceRepository = appSourceRepository;
        this.storageService = storageService;
        this.userRepository = userRepository;
    }

    @Override
    public String saveApplication(Long userId,
                              AppSourcePostDTO appSourcePostDTO, MultipartFile appSourceImage) {

        //AppSourcePostDTO appSourceDTO = getJson(appSourceRequest);

        String imageUrl = "";
        String message = "";

        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            message = "User Not Found: UserId - " +userId;
            logger.info(message);
            throw new ResourceNotFoundException(message);
        }

        try {
            imageUrl = storageService.save(appSourceImage, userId);

            message = "Uploaded the file successfully: " + appSourceImage.getOriginalFilename();
            System.out.println(message);

        } catch (Exception e) {
            message = "Could not upload the file: " + appSourceImage.getOriginalFilename() + "  !";
            throw new RuntimeException(message);
        }

        if (imageUrl.contains("Error")) {
            return imageUrl;
        }

        AppSource appSource = copyAppSourceDTOtoAppSource(appSourcePostDTO, new AppSource());
        appSource.setDeployer(userOptional.get());
        appSource.setAppCodeUrl(imageUrl);


        appSourceRepository.save(appSource);



        message = "AppSource was successfully created \n appSource url: " + appSource.getAppCodeUrl();
        return message;

    }

    @Override
    public List<AppSourceDTO> getAllApplications(String orderBy) {
        Iterable<AppSource> appSources = new ArrayList<>();

        switch (orderBy) {
            case "nameAsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.ASC, "appSourceName"));
                break;
            case "nameDsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.DESC, "appSourceName"));
                break;
            case "priceAsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.ASC, "appSourcePrice"));
                break;
            case "priceDsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.DESC, "appSourcePrice"));
                break;
            case "createdDateAsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));
                break;
            case "createdDateDsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
                break;
            case "modifiedDateAsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.ASC, "modifiedDate"));
                break;
            case "modifiedDateDsc":
                appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.DESC, "modifiedDate"));
                break;
            default:
                appSources = appSourceRepository.findAll();

        }

//        Iterable<AppSource> appSources = appSourceRepository.findAll(Sort.by(Sort.Direction.ASC, "appSourcePrice"));
        return loadAppSourceDTOS(appSources);

    }

    @Override
    public List<AppSourceDTO> getAllApplicationsOwnedBy(Long userId, String orderBy) {

        Iterable<AppSource> appSources = new ArrayList<>();


        switch (orderBy) {
            case "nameAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppNameAsc(userId);
                break;
            case "nameDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppNameDesc(userId);
                break;
            case "typeAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppTypeAsc(userId);
                break;
            case "typeDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppTypeDesc(userId);
                break;
            case "createdDateAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByCreatedDateAsc(userId);
                break;
            case "createdDateDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByCreatedDateDesc(userId);
                break;
            case "modifiedDateAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByModifiedDateAsc(userId);
                break;
            case "modifiedDateDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByModifiedDateDesc(userId);
                break;
            default:
                appSources = appSourceRepository.findAllByDeployerUserId(userId);

        }

        //Iterable<AppSource> appSources = appSourceRepository.findAllBySellerUserId(userId);
        return loadAppSourceDTOS(appSources);
    }

    @Override
    public List<AppSourceDTO> getAllApplicationsWithStatusAndOwnedBy(Long userId, String appStatus) {
        logger.info("Top of class");

        Iterable<AppSource> appSources;
        Iterable<AppSource> appSources1;
        Iterable<AppSource> appSources2;

        if(appStatus.equals("DnP")) {
            appSources1 = appSourceRepository.findAllByDeployerUserIdAndAppStatus(userId, AppStatus.DEPLOYED);
            appSources2 = appSourceRepository.findAllByDeployerUserIdAndAppStatus(userId, AppStatus.PUSHED);

            List<AppSourceDTO> appSourceDTOS = loadAppSourceDTOS(appSources1);
            List<AppSourceDTO> appSourceDTOS1 = loadAppSourceDTOS(appSources2);
            appSourceDTOS1.forEach(appSourceDTO -> appSourceDTOS.add(appSourceDTO));

            return appSourceDTOS;
        }

        logger.info("I got here");
        appSources = appSourceRepository.findAllByDeployerUserIdAndAppStatus(userId, AppStatus.valueOf(appStatus));

        logger.info("{}", appSources);

        /*
        switch (orderBy) {
            case "nameAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppNameAsc(userId);
                break;
            case "nameDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppNameDesc(userId);
                break;
            case "typeAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppTypeAsc(userId);
                break;
            case "typeDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByAppTypeDesc(userId);
                break;
            case "createdDateAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByCreatedDateAsc(userId);
                break;
            case "createdDateDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByCreatedDateDesc(userId);
                break;
            case "modifiedDateAsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByModifiedDateAsc(userId);
                break;
            case "modifiedDateDsc":
                appSources = appSourceRepository.findAllByDeployerUserIdOrderByModifiedDateDesc(userId);
                break;
            default:
                appSources = appSourceRepository.findAllByDeployerUserId(userId);

        }
*/
        //Iterable<AppSource> appSources = appSourceRepository.findAllBySellerUserId(userId);
        return loadAppSourceDTOS(appSources);
    }

    @Override
    public AppSourceDTO getApplication(Long id) {

        AppSource appSource = appSourceRepository.findById(id).get();

        return copyAppSourceToAppSourceDTO(appSource, new AppSourceDTO());
    }

    @Override
    public void removeApplication(Long id, HttpServletRequest request) {
        String fileUrl ="";
        if (appSourceRepository.findById(id).isPresent()) {
            fileUrl = appSourceRepository.findById(id).get().getAppCodeUrl();
            storageService.deleteFile(fileUrl, request);
            appSourceRepository.deleteById(id);
        }


    }

    @Override
    public String updateApplication(Long userId, Long id, AppSourcePostDTO appSourceDTO, MultipartFile appSourceImage, HttpServletRequest request) {
        Optional<AppSource> appSourceOpt = appSourceRepository.findById(id);

        String imageUrl = "";
        String message = "";

        if (appSourceOpt.isPresent()) {




            Optional<User> userOptional = userRepository.findById(userId);

            if (!userOptional.isPresent()) {
                throw new ResourceNotFoundException("User Not Found: UserId - " +userId );
            }



            AppSource appSourceToUpdate = appSourceOpt.get();
            storageService.deleteFile(appSourceToUpdate.getAppCodeUrl(), request);

            try {
                imageUrl = storageService.save(appSourceImage, userId);

                message = "Uploaded the file successfully: " + appSourceImage.getOriginalFilename();
                System.out.println(message);

            } catch (Exception e) {
                message = "Could not upload the file: " + appSourceImage.getOriginalFilename() + "!";
                throw new RuntimeException(message);
            }

            if (imageUrl.contains("Error")) {
                return imageUrl;
            }

            appSourceToUpdate.setAppName(appSourceDTO.getAppName());
            appSourceToUpdate.setAppDescription(appSourceDTO.getAppDescription());
            appSourceToUpdate.setDeployer(userOptional.get());
            //appSourceToUpdate.setAppCodeUrl(AppType.JAVA.toString());
            appSourceToUpdate.setAppCodeUrl(imageUrl);
            appSourceToUpdate.setModifiedDate(LocalDateTime.now());

            appSourceRepository.save(appSourceToUpdate);



            message = "AppSource was successfully created \n appSource url: " + appSourceToUpdate.getAppCodeUrl();
            return message;

        } else {
            throw new ResourceNotFoundException("AppSource - "+id+" does not Exist");
        }


    }

    @Override
    public Resource loadImage(String filename) {
        return storageService.load(filename);
    }

    public List<AppSourceDTO> loadAppSourceDTOS(Iterable<AppSource> appSources) {
        List<AppSourceDTO> appSourceDTOS = new ArrayList<>();
        for (AppSource appSource : appSources) {
            appSourceDTOS.add(copyAppSourceToAppSourceDTO(appSource, new AppSourceDTO()));
        }

        return appSourceDTOS;

    }

    private AppSourceDTO copyAppSourceToAppSourceDTO(AppSource appSource, AppSourceDTO appSourceDTO) {

        appSourceDTO.setAppId(appSource.getAppId());
        appSourceDTO.setAppName(appSource.getAppName());
        appSourceDTO.setAppDescription(appSource.getAppDescription());
        appSourceDTO.setAppCodeUrl(appSource.getAppCodeUrl());
        appSourceDTO.setDeployer(appSource.getDeployer());
        appSourceDTO.setAppType(appSource.getAppType());
        appSourceDTO.setAppStatus(appSource.getAppStatus());

        return appSourceDTO;
    }

    private AppSource copyAppSourceDTOtoAppSource(AppSourcePostDTO appSourcePostDTO, AppSource appSource) {

        appSource.setAppName(appSourcePostDTO.getAppName());
        appSource.setAppDescription(appSourcePostDTO.getAppDescription());
        appSource.setAppType(appSourcePostDTO.getAppType());
        appSource.setCreatedDate(LocalDateTime.now());
        appSource.setModifiedDate(LocalDateTime.now());

        return appSource;
    }


    /**
     * Just a method to convert a string into JSON
     * Converts a String (appSource) to AppSourcePostDTO Object
     * */

/*
    public AppSourcePostDTO getJson(String appSource) {
        AppSourcePostDTO appSourceJson = new AppSourcePostDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            appSourceJson = objectMapper.readValue(appSource, AppSourcePostDTO.class);
        } catch (IOException err) {
            System.out.printf("Error: ", err.toString());
        }

        return appSourceJson;
    }
*/

}
