package com.example.api;

import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.model.dto.*;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductAvatarRepository;
import com.example.repository.ProductRepository;
import com.example.service.category.CategoryService;
import com.example.service.category.ICategoryService;
import com.example.service.product.IProductService;
import com.example.service.product.ProductService;
import com.example.service.productAvatar.IProductAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductAvatarService productAvatarService;

    @GetMapping("/category")
    public ResponseEntity<?>getAllCategory(){
        List<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
        List<ProductResDTO> productResDTOS = productService.findAllProductResDTOByDeletedIsFalse();
        List<ProductDTO> productDTOS = new ArrayList<>();

        for(ProductResDTO item : productResDTOS){
            ProductAvatarDTO productAvatarDTO = new ProductAvatarDTO();
            productAvatarDTO.setId(item.getAvatarId());
            productAvatarDTO.setFileFolder(item.getFileFolder());
            productAvatarDTO.setFileUrl(item.getFileUrl());
            productAvatarDTO.setFileName(item.getFileName());
            ProductDTO productDTO = item.toProductDTO(productAvatarDTO);
            productDTOS.add(productDTO);
        }

        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(ProductCreateAvatarReqDTO productCreateAvatarReqDTO){
        MultipartFile file = productCreateAvatarReqDTO.getFile();
        Long categoryId = Long.parseLong(productCreateAvatarReqDTO.getCategoryId());
        Optional<Category> optionalCategory = categoryService.findById(categoryId);
        if(!optionalCategory.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductDTO productDTO = productCreateAvatarReqDTO.toProductDTO(optionalCategory.get());

        if(file != null){
            Product product = productDTO.toProduct();
            ProductCreateAvatarResDTO productCreateAvatarResDTO = productService.createWithAvatar(product, file);

            return new ResponseEntity<>(productCreateAvatarResDTO, HttpStatus.CREATED);
        } else{
            productDTO.setId(null);
            Product product = productDTO.toProduct();
            productService.save(product);

            productDTO = product.toProductDTO();
            return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
        }
    }
}
