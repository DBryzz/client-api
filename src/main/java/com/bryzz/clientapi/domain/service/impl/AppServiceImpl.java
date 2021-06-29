package com.bryzz.clientapi.domain.service.impl;


import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.dto.AppSourceDTO;
import com.bryzz.clientapi.domain.dto.AppSourcePostDTO;
import com.bryzz.clientapi.domain.dto.ImageDTO;
import com.bryzz.clientapi.domain.dto.ImagePostDTO;
import com.bryzz.clientapi.domain.model.AppSource;
import com.bryzz.clientapi.domain.model.DockerImage;
import com.bryzz.clientapi.domain.model.User;
import com.bryzz.clientapi.domain.repository.AppSourceRepository;
import com.bryzz.clientapi.domain.repository.ImageRepository;
import com.bryzz.clientapi.domain.repository.UserRepository;
import com.bryzz.clientapi.domain.service.AppService;
import com.bryzz.clientapi.domain.service.FileStorageService;
import com.bryzz.clientapi.exceptions.IOExceptionHandler;
import com.bryzz.clientapi.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private ImageRepository imageRepository;


    @Autowired
    public AppServiceImpl(AppSourceRepository appSourceRepository, FileStorageService storageService,
                              UserRepository userRepository, ImageRepository imageRepository) {
        this.appSourceRepository = appSourceRepository;
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
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
            imageUrl = storageService.save(appSourceImage, userId, appSourcePostDTO.getAppName());

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
        appSource.setAppStatus(AppStatus.PENDING);


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

        //Iterable<AppSource> appSources = appSourceRepository.findAllBySellerUserId(userId);
        return loadAppSourceDTOS(appSources);
    }

    @Override
    public AppSourceDTO getApplication(Long id) {

        AppSource appSource = appSourceRepository.findById(id).get();

        return copyAppSourceToAppSourceDTO(appSource, new AppSourceDTO());
    }

    @Override
    public void removeApplication(Long deployerId, Long id, HttpServletRequest request) {
        String fileUrl ="";
        if (appSourceRepository.findById(id).isPresent()) {
            AppSource appSource = appSourceRepository.findById(id).get();
            fileUrl = appSource.getAppCodeUrl();
            String appName = appSource.getAppName();

            storageService.deleteFile(deployerId, appName, fileUrl, request);
            appSourceRepository.deleteById(id);
        }


    }

    @Override
    public String updateApplication(Long deployerId, Long id, AppSourcePostDTO appSourceDTO, MultipartFile appSourceImage, HttpServletRequest request) {
        Optional<AppSource> appSourceOpt = appSourceRepository.findById(id);

        String imageUrl = "";
        String message = "";

        if (appSourceOpt.isPresent()) {




            Optional<User> userOptional = userRepository.findById(deployerId);

            if (!userOptional.isPresent()) {
                throw new ResourceNotFoundException("User Not Found: UserId - " +deployerId );
            }



            AppSource appSourceToUpdate = appSourceOpt.get();
            storageService.deleteFile(deployerId, appSourceDTO.getAppName(), appSourceToUpdate.getAppCodeUrl(), request);

            try {
                imageUrl = storageService.save(appSourceImage, deployerId, appSourceDTO.getAppName());

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
    public Resource loadImage(Long userId, String appName, String filename) {
        return storageService.load(userId, appName, filename);
    }

    /** Containerization **/
    @Override
    public ImageDTO containerize(Long userId, ImagePostDTO imagePostDTO) {

        AppSource srcCode = imagePostDTO.getImgSourceCode();

        String pathToShellScript = System.getProperty("user.dir")+"/src/main/resources/static/assets/imageScript.sh"; // "/src/main/resources/static/assets/imageScript.sh";
        String typeOfShell = "bash";
        String name = imagePostDTO.getImgName();
        String tag = imagePostDTO.getImgTag();
        AppStatus status = imagePostDTO.getImageStatus();
        String appType = srcCode.getAppType().toString();
        String appUrl = srcCode.getAppCodeUrl();
        String extension = appUrl.substring(appUrl.length()-4);
        String appDir = System.getProperty("user.dir")+"/src/main/resources/static/src-code-dir/"+userId+"/"+srcCode.getAppName();

        String s = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/apps//src/main/resources/static/src-code-dir/" + userId + "/" + srcCode.getAppName()+"/")
                .toUriString();
        String sourceCodeName = appUrl.substring(s.length());

//        String sourceCodeName = appUrl+.substring() System.getProperty("user.dir")+"/src/main/resources/static/src-code-dir/"+userId+"/"+srcCode.getAppName();

        ImageDTO imageDTO = new ImageDTO();
        DockerImage dockerImage = new DockerImage();

        /** Assert it is not windows */
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        if (isWindows) {
            logger.info("This is a Windows Operating System ==> Valid only for Linux or MacOS");
            throw new IOExceptionHandler("Valid only for Linux or MacOS");
        }

        /** Read Scripts File */
        Process process;
        try {
            List<String> cmdList = new ArrayList<String>();

            /** adding command and args to the list */

            cmdList.add(typeOfShell);
            cmdList.add(pathToShellScript);
            cmdList.add(name);
            cmdList.add(tag);
            cmdList.add(appType);
            cmdList.add(status.toString());
            cmdList.add(appUrl);
            cmdList.add(appDir);
            cmdList.add(extension);
            cmdList.add(sourceCodeName);
            ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
            process = processBuilder.start();
            logger.info("{}", process);
            logger.info("{}", process);

            process.waitFor();

            BufferedReader reader=new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            /*DockerImage save = imageRepository.save(copyImageDTOToImage(userId, imagePostDTO));

            AppSource appSourceById = appSourceRepository.getById(srcCode.getAppId());
            appSourceById.setAppStatus(status);
            appSourceRepository.save(appSourceById);


            imageDTO = copyImageToImageDTO(userId, save); */

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return imageDTO;
    }

    private ImageDTO copyImageToImageDTO(Long userId, DockerImage dockerImage) {
        ImageDTO imageDTO = new ImageDTO();

        imageDTO.setImgId(dockerImage.getImgId());
        imageDTO.setImageStatus(dockerImage.getImageStatus());
        imageDTO.setImgName(dockerImage.getImgName());
        imageDTO.setImgTag(dockerImage.getImgTag());
        imageDTO.setImgSourceCode(dockerImage.getImgSourceCode());
        imageDTO.setDeployer(userRepository.getById(userId));
        imageDTO.setCreatedDate(LocalDateTime.now());
        imageDTO.setModifiedDate(LocalDateTime.now());

        return imageDTO;
    }

    private DockerImage copyImageDTOToImage(Long userId, ImagePostDTO imagePostDTO) {
        DockerImage dockerImage = new DockerImage();

        dockerImage.setImageStatus(imagePostDTO.getImageStatus());
        dockerImage.setImgName(imagePostDTO.getImgName());
        dockerImage.setImgTag(imagePostDTO.getImgTag());
        dockerImage.setImgSourceCode(imagePostDTO.getImgSourceCode());
        dockerImage.setDeployer(userRepository.getById(userId));
        dockerImage.setCreatedDate(LocalDateTime.now());
        dockerImage.setModifiedDate(LocalDateTime.now());

        return dockerImage;
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
