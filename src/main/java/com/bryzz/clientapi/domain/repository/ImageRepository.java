package com.bryzz.clientapi.domain.repository;

import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.model.DockerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<DockerImage, Long> {

    Iterable<DockerImage> findByImgName(String imgName);
    Iterable<DockerImage> findByImgNameOrderByImgNameAsc(String imgName);
    Iterable<DockerImage> findByImgNameOrderByImgNameDesc(String appName);

    Iterable<DockerImage> findAllByDeployerUserId(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByImgNameAsc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByImgNameDesc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByImgTagAsc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByImgTagDesc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByCreatedDateAsc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByCreatedDateDesc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByUsersAsc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByModifiedDateDesc(Long userId);
    Iterable<DockerImage> findAllByDeployerUserIdOrderByModifiedDateAsc(Long userId);

    Iterable<DockerImage> findAllByDeployerUserIdAndImageStatus(Long userId, AppStatus imgStatus);
    Iterable<DockerImage> findAllByImageStatusAndDeployer_UserId(AppStatus appStatus, Long userId);
    Iterable<DockerImage> findAllByImageStatus(AppStatus appStatus);
}
