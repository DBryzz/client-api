package com.bryzz.clientapi.domain.service.impl;


import com.bryzz.clientapi.domain.repository.UserRepository;
import com.bryzz.clientapi.domain.service.AppService;
import com.bryzz.clientapi.domain.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppServiceImpl implements AppService {

//    private final Path root = Paths.get("uploads");

    private AppRepository appRepository;
    private FileStorageService storageService;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;


    @Autowired
    public AppServiceImpl(AppRepository appRepository, FileStorageService storageService,
                              UserRepository userRepository, CategoryRepository categoryRepository,
                              ItemRepository itemRepository, OrderRepository orderRepository) {
        this.appRepository = productRepository;
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public String saveProduct(String userId,
                              PostProductDTO productDTO, MultipartFile productImage) {

        //PostProductDTO productDTO = getJson(productRequest);

        String imageUrl = "";
        String message = "";

        Long catId = productDTO.getProductCategory().getCategoryId();

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Category> categoryOptional = categoryRepository.findById(catId);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User Not Found: UserId - " +userId );
        }

        if (!categoryOptional.isPresent()) {
            throw new ResourceNotFoundException("Category Not Found: CategoryId - " +catId );
        }


        try {
            imageUrl = storageService.save(productImage, userId);

            message = "Uploaded the file successfully: " + productImage.getOriginalFilename();
            System.out.println(message);

        } catch (Exception e) {
            message = "Could not upload the file: " + productImage.getOriginalFilename() + "!";
            throw new RuntimeException(message);
        }

        if (imageUrl.contains("Error")) {
            return imageUrl;
        }

        Product product = copyProductDTOtoProduct(productDTO, new Product());
        product.setSeller(userOptional.get());
        product.setProductCategory(categoryOptional.get());
        product.setProductImageUrl(imageUrl);


        productRepository.save(product);



        message = "Product was successfully created \n product url: " + product.getProductImageUrl();
        return message;

    }

    @Override
    public List<ProductDTO> getAllProducts(String orderBy) {
        Iterable<Product> products = new ArrayList<>();

        switch (orderBy) {
            case "nameAsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "productName"));
                break;
            case "nameDsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "productName"));
                break;
            case "priceAsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "productPrice"));
                break;
            case "priceDsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "productPrice"));
                break;
            case "createdDateAsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));
                break;
            case "createdDateDsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
                break;
            case "modifiedDateAsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "modifiedDate"));
                break;
            case "modifiedDateDsc":
                products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "modifiedDate"));
                break;
            default:
                products = productRepository.findAll();

        }

//        Iterable<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "productPrice"));
        return loadProductDTOS(products);

    }

    @Override
    public List<ProductDTO> getAllProductsOwnedBy(String userId, String orderBy) {

        Iterable<Product> products = new ArrayList<>();


        switch (orderBy) {
            case "nameAsc":
                products = productRepository.findAllBySellerUserIdOrderByProductNameAsc(userId);
                break;
            case "nameDsc":
                products = productRepository.findAllBySellerUserIdOrderByProductNameDesc(userId);
                break;
            case "priceAsc":
                products = productRepository.findAllBySellerUserIdOrderByProductPriceAsc(userId);
                break;
            case "priceDsc":
                products = productRepository.findAllBySellerUserIdOrderByProductPriceDesc(userId);
                break;
            case "categoryAsc":
                products = productRepository.findAllBySellerUserIdOrderByProductCategoryAsc(userId);
                break;
            case "categoryDsc":
                products = productRepository.findAllBySellerUserIdOrderByProductCategoryDesc(userId);
                break;
            case "quantityAsc":
                products = productRepository.findAllBySellerUserIdOrderByProductAvailableQuantityAsc(userId);
                break;
            case "quantityDsc":
                products = productRepository.findAllBySellerUserIdOrderByProductAvailableQuantityDesc(userId);
                break;
            case "createdDateAsc":
                products = productRepository.findAllBySellerUserIdOrderByCreatedDateAsc(userId);
                break;
            case "createdDateDsc":
                products = productRepository.findAllBySellerUserIdOrderByCreatedDateDesc(userId);
                break;
            case "modifiedDateAsc":
                products = productRepository.findAllBySellerUserIdOrderByModifiedDateAsc(userId);
                break;
            case "modifiedDateDsc":
                products = productRepository.findAllBySellerUserIdOrderByModifiedDateDesc(userId);
                break;
            default:
                products = productRepository.findAllBySellerUserId(userId);

        }

        //Iterable<Product> products = productRepository.findAllBySellerUserId(userId);
        return loadProductDTOS(products);
    }

    @Override
    public List<ProductDTO> getAllProductsInCategory(Long categoryId) {
        Iterable<Product> products = productRepository.findAllByProductCategoryCategoryId(categoryId);
        return loadProductDTOS(products);
    }

    @Override
    public List<ProductDTO> getAllProductsWithProductPriceBetween(Long lowerBound, Long upperBound) {
        Iterable<Product> products = productRepository.findAllByProductPriceBetweenOrderByProductPriceAsc(lowerBound, upperBound);
        return loadProductDTOS(products);
    }


    @Override
    public ProductDTO getProduct(Long id) {

        Product product = productRepository.findById(id).get();

        return copyProductToProductDTO(product, new ProductDTO());
    }

    @Override
    public void removeProduct(Long id, HttpServletRequest request) {
        String fileUrl ="";
        if (productRepository.findById(id).isPresent()) {
            fileUrl = productRepository.findById(id).get().getProductImageUrl();
            storageService.deleteFile(fileUrl, request);
            productRepository.deleteById(id);
        }


    }

    @Override
    public void updateDatabaseIfPaymentSuccess(OrderDTO orderDTO) {
        List<Item> orderItems = orderDTO.getOrderItems();
        orderItems.forEach(item ->
        {
            Optional<Product> productOpt = productRepository.findById(item.getItemProduct().getProductId());
            if (productOpt.isPresent()) {

                Product product = productOpt.get();
                product.setProductAvailableQuantity(product.getProductAvailableQuantity() - item.getItemQuantity());

                productRepository.save(product);
                itemRepository.save(item);
            } else {
                throw new ResourceNotFoundException("Product does not exist --> ProductId = " + item.getItemProduct().getProductId());
            }

        });

        Order order = orderRepository.findById(orderDTO.getOrderId()).get();

        order.setOrderStatus(OrderStatus.valueOf(orderDTO.getOrderStatus()));
        order.setOrderItems(orderItems);
        order.setBuyer(orderDTO.getBuyer());
        order.setBuyerPhoneNum(orderDTO.getBuyerPhoneNum());
        order.setPlacedOn(orderDTO.getPlacedOn());
        order.setTotalPrice(orderDTO.getTotalPrice());

        orderRepository.save(order);



    }

    @Override
    public void updateDatabaseIfPaymentFailure(OrderDTO orderDTO) {
      /*  List<Item> orderItems = orderDTO.getOrderItems();
        orderItems.forEach(item ->
        {
            Optional<Product> productOpt = productRepository.findById(item.getItemProduct().getProductId());
            if (productOpt.isPresent()) {

                Product product = productOpt.get();
                product.setProductAvailableQuantity(product.getProductAvailableQuantity() - item.getItemQuantity());

                productRepository.save(product);
                itemRepository.save(item);
            } else {
                throw new ResourceNotFoundException("Product does not exist --> ProductId = " + item.getItemProduct().getProductId());
            }

        });*/

        Order order = orderRepository.findById(orderDTO.getOrderId()).get();

        order.setOrderStatus(OrderStatus.CANCELED);
        order.setOrderItems(orderDTO.getOrderItems());
        order.setBuyer(orderDTO.getBuyer());
        order.setBuyerPhoneNum(orderDTO.getBuyerPhoneNum());
        order.setPlacedOn(orderDTO.getPlacedOn());
        order.setTotalPrice(orderDTO.getTotalPrice());

        orderRepository.save(order);



    }

    @Override
    public String updateProduct(String userId, Long id, PostProductDTO productDTO, MultipartFile productImage, HttpServletRequest request) {
        Optional<Product> productOpt = productRepository.findById(id);

        String imageUrl = "";
        String message = "";

        if (productOpt.isPresent()) {



            Long catId = productDTO.getProductCategory().getCategoryId();

            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Category> categoryOptional = categoryRepository.findById(catId);

            if (!userOptional.isPresent()) {
                throw new ResourceNotFoundException("User Not Found: UserId - " +userId );
            }

            if (!categoryOptional.isPresent()) {
                throw new ResourceNotFoundException("Category Not Found: CategoryId - " +catId );
            }

            Product productToUpdate = productOpt.get();
            storageService.deleteFile(productToUpdate.getProductImageUrl(), request);

            try {
                imageUrl = storageService.save(productImage, userId);

                message = "Uploaded the file successfully: " + productImage.getOriginalFilename();
                System.out.println(message);

            } catch (Exception e) {
                message = "Could not upload the file: " + productImage.getOriginalFilename() + "!";
                throw new RuntimeException(message);
            }

            if (imageUrl.contains("Error")) {
                return imageUrl;
            }

            productToUpdate.setProductName(productDTO.getProductName());
            productToUpdate.setProductDescription(productDTO.getProductDescription());
            productToUpdate.setProductPrice(productDTO.getProductPrice());
            productToUpdate.setProductAvailableQuantity(productDTO.getProductAvailableQuantity());
            productToUpdate.setSeller(userOptional.get());
            productToUpdate.setProductCategory(categoryOptional.get());
            productToUpdate.setProductImageUrl(imageUrl);
            productToUpdate.setModifiedDate(LocalDateTime.now());

            productRepository.save(productToUpdate);



            message = "Product was successfully created \n product url: " + productToUpdate.getProductImageUrl();
            return message;

        } else {
            throw new ResourceNotFoundException("Product - "+id+" does not Exist");
        }


    }

    @Override
    public Resource loadImage(String filename) {
        return storageService.load(filename);
    }

    public List<ProductDTO> loadProductDTOS(Iterable<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            productDTOS.add(copyProductToProductDTO(product, new ProductDTO()));
        }

        return productDTOS;

    }

    private ProductDTO copyProductToProductDTO(Product product, ProductDTO productDTO) {

        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setProductQuantity(product.getProductAvailableQuantity());
        productDTO.setProductImageUrl(product.getProductImageUrl());
        productDTO.setSeller(product.getSeller());
        productDTO.setProductCategory(product.getProductCategory());

        return productDTO;
    }

    private Product copyProductDTOtoProduct(PostProductDTO productDTO, Product product) {

        product.setProductName(productDTO.getProductName());
        product.setProductDescription(productDTO.getProductDescription());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductAvailableQuantity(productDTO.getProductAvailableQuantity());
        product.setCreatedDate(LocalDateTime.now());
        product.setModifiedDate(LocalDateTime.now());

        return product;
    }


    /**
     * Just a method to convert a string into JSON
     * Converts a String (product) to PostProductDTO Object
     * */

/*
    public PostProductDTO getJson(String product) {
        PostProductDTO productJson = new PostProductDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productJson = objectMapper.readValue(product, PostProductDTO.class);
        } catch (IOException err) {
            System.out.printf("Error: ", err.toString());
        }

        return productJson;
    }
*/

}
