package com.example.api;

import com.example.exception.ResourceNotFoundException;
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
import com.example.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private AppUtils appUtils;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        Optional<ProductResDTO> optionalProductResDTO = productService.findProductResDTOByDeletedIsFalse(id);
        ProductResDTO productResDTO = optionalProductResDTO.get();

        ProductAvatarDTO productAvatarDTO = new ProductAvatarDTO();
        productAvatarDTO.setId(productResDTO.getAvatarId());
        productAvatarDTO.setFileFolder(productResDTO.getFileFolder());
        productAvatarDTO.setFileUrl(productResDTO.getFileUrl());
        productAvatarDTO.setFileName(productResDTO.getFileName());
        ProductDTO productDTO = productResDTO.toProductDTO(productAvatarDTO);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Validated ProductCreateAvatarReqDTO productCreateAvatarReqDTO, BindingResult bindingResult){
        new ProductCreateAvatarReqDTO().validate(productCreateAvatarReqDTO, bindingResult);
        if(bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }
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

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateWithAvatar(@PathVariable Long id, MultipartFile file,@Validated ProductUpdateReqDTO productUpdateReqDTO, BindingResult bindingResult) throws IOException {
        new ProductUpdateReqDTO().validate(productUpdateReqDTO, bindingResult);
        if(bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }
        Optional<Product> productOptional = productService.findById(id);

        Long categoryId = Long.valueOf(productUpdateReqDTO.getCategoryId());
        Optional<Category> categoryOptional = categoryService.findById(categoryId);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found this product");
        }

        if (file == null) {

            ProductDTO productDTO = productUpdateReqDTO.toProductDTO(categoryOptional.get());
            Product product = productDTO.toProduct();
            product.setId(id);

            ProductUpdateAvatarResDTO productUpdateAvatarResDTO = productService.update(product);
            return new ResponseEntity<>(productUpdateAvatarResDTO, HttpStatus.OK);
        }
        else {
            ProductDTO productDTO = productUpdateReqDTO.toProductDTO(categoryOptional.get());
            Product product = productDTO.toProduct();
            product.setId(id);

            ProductUpdateAvatarResDTO productUpdateAvatarResDTO = productService.updateWithAvatar(product, file);

            return new ResponseEntity<>(productUpdateAvatarResDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> remove(@PathVariable long id) {
        Optional<Product> productOptional = productService.findById(id);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found this product");
        }

        Product product = productOptional.get();
        productService.delete(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
