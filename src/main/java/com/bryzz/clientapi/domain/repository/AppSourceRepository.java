package com.bryzz.clientapi.domain.repository;

import com.bryzz.clientapi.domain.model.AppSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AppSourceRepository extends JpaRepository<AppSource, Long> {
    Iterable<AppSource> findByAppName(String appName);
    Iterable<AppSource> findByAppNameOrderByAppNameAsc(String appName);
    Iterable<AppSource> findByAppNameOrderByAppNameDesc(String appName);

    Iterable<AppSource> findAllByDeployerUserId(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppNameAsc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppNameDesc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppTypeAsc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByAppTypeDesc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByCreatedDateAsc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByCreatedDateDesc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByUsersAsc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByModifiedDateDesc(long userId);
    Iterable<AppSource> findAllByDeployerUserIdOrderByModifiedDateAsc(long userId);











//    @Query(value = "SELECT * FROM AppSource where productPrice between :priceLowerBound and :priceUpperBound order by productPrice ASC ")
//    Iterable<AppSource> findAllByProductPriceBetweenOrderByProductPriceAsc(Long priceLowerBound, Long priceUpperBound);


}
