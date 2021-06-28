package com.bryzz.clientapi.domain.repository;

import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.model.AppSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppSourceRepository extends JpaRepository<AppSource, Long> {
    Iterable<AppSource> findByAppName(String appName);
    Iterable<AppSource> findByAppNameOrderByAppNameAsc(String appName);
    Iterable<AppSource> findByAppNameOrderByAppNameDesc(String appName);

    Iterable<AppSource> findAllByDeployerUserId(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppNameAsc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppNameDesc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppTypeAsc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppTypeDesc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByCreatedDateAsc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByCreatedDateDesc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByUsersAsc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByModifiedDateDesc(Long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByModifiedDateAsc(Long userId);

    Iterable<AppSource> findAllByDeployerUserIdAndAppStatus(Long userId, AppStatus appStatus);
    Iterable<AppSource> findAllByAppStatusAndDeployer_UserId(AppStatus appStatus, Long userId);
    Iterable<AppSource> findAllByAppStatus(AppStatus appStatus);












//    @Query(value = "SELECT * FROM AppSource where productPrice between :priceLowerBound and :priceUpperBound order by productPrice ASC ")
//    Iterable<AppSource> findAllByProductPriceBetweenOrderByProductPriceAsc(Long priceLowerBound, Long priceUpperBound);


}
