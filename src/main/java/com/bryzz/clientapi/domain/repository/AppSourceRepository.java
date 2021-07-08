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

    Iterable<AppSource> findAllByUploaderUserId(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByAppNameAsc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByAppNameDesc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByAppTypeAsc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByAppTypeDesc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByCreatedDateAsc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByCreatedDateDesc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByUploaderAsc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByModifiedDateDesc(Long userId);
    Iterable<AppSource> findAllByUploaderUserIdOrderByModifiedDateAsc(Long userId);

    Iterable<AppSource> findAllByUploaderUserIdAndAppStatus(Long userId, AppStatus appStatus);
    Iterable<AppSource> findAllByAppStatusAndUploader_UserId(AppStatus appStatus, Long userId);
    Iterable<AppSource> findAllByAppStatus(AppStatus appStatus);












//    @Query(value = "SELECT * FROM AppSource where productPrice between :priceLowerBound and :priceUpperBound order by productPrice ASC ")
//    Iterable<AppSource> findAllByProductPriceBetweenOrderByProductPriceAsc(Long priceLowerBound, Long priceUpperBound);


}
