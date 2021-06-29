package com.bryzz.clientapi.domain.service;


import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.dto.AppSourceDTO;
import com.bryzz.clientapi.domain.dto.AppSourcePostDTO;
import com.bryzz.clientapi.domain.dto.ImageDTO;
import com.bryzz.clientapi.domain.dto.ImagePostDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppService {

    String saveApplication(Long userId, AppSourcePostDTO appSourcePostDTO, MultipartFile productImage);

    List<AppSourceDTO> getAllApplications(String orderBy);

    List<AppSourceDTO> getAllApplicationsOwnedBy(Long userId, String orderBy);
    List<AppSourceDTO> getAllApplicationsWithStatusAndOwnedBy(Long userId, String status);

    AppSourceDTO getApplication(Long id);

    void removeApplication(Long deployerId, Long id, HttpServletRequest request);

    String updateApplication(Long userId, Long id, AppSourcePostDTO appSourcePostDTO, MultipartFile productImage, HttpServletRequest request);

    Resource loadImage(Long userId, String appName, String filename);

    ImageDTO containerize(Long userId, ImagePostDTO image);


}
