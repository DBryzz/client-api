package com.bryzz.clientapi.domain.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppService {

    String saveProduct(String userId, PostProductDTO product, MultipartFile productImage);

    List<ProductDTO> getAllProducts(String orderBy);

    List<ProductDTO> getAllProductsOwnedBy(String userId, String ownedBy);

    List<ProductDTO> getAllProductsInCategory(Long categoryId);

    List<ProductDTO> getAllProductsWithProductPriceBetween(Long lowerBound, Long upperBound);

    ProductDTO getProduct(Long id);

    void removeProduct(Long id, HttpServletRequest request);

    void updateDatabaseIfPaymentSuccess(OrderDTO orderDTO);
    void updateDatabaseIfPaymentFailure(OrderDTO orderDTO);

    String updateProduct(String userId, Long id, PostProductDTO product, MultipartFile productImage, HttpServletRequest request);

    Resource loadImage(String filename);
}
