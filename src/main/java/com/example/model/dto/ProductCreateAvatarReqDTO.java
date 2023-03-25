package com.example.model.dto;

import com.example.model.Category;
import com.example.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateAvatarReqDTO implements Validator {
    private Long id;
    private String productName;
    private String price;
    private String quantity;
    private String description;
    private String categoryId;
    private MultipartFile file;


    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCreateAvatarReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCreateAvatarReqDTO productCreateDTO = (ProductCreateAvatarReqDTO) target;

        String productName = productCreateDTO.getProductName().trim();
        String price = productCreateDTO.getPrice();
        String quantity = productCreateDTO.getQuantity();
        String categoryId = productCreateDTO.getCategoryId();

        if (productName == null) {
            errors.rejectValue("productName", "productName.null", "Product Name must not be null");
        }

        if (price != null && price.length() > 0) {
            if (!price.matches("(^$|[0-9]*$)")) {
                errors.rejectValue("price", "price.number", "Price must be a number");
            }
        } else{
            errors.rejectValue("price", "price.null", "Price must not be null");
        }

        if (quantity != null && quantity.length() > 0) {
            if (!quantity.matches("(^$|[0-9]*$)")) {
                errors.rejectValue("quantity", "quantity.number", "Quantity must be a number");
            }
        } else{
            errors.rejectValue("price", "price.null", "Quantity must not be null");
        }

        if (categoryId != null && categoryId.length() > 0) {
            if (!categoryId.matches("(^$|[0-9]*$)")) {
                errors.rejectValue("categoryId", "categoryId.number", "Category's id must be a number");
            }
        } else{
            errors.rejectValue("categoryId", "categoryId.null", "Category's id must not be null");
        }
    }

    public Product toProduct(Category category){
        return new Product()
                .setId(id)
                .setProductName(productName)
                .setPrice(BigDecimal.valueOf(Long.parseLong(String.valueOf(price))))
                .setQuantity(Long.parseLong(quantity))
                .setDescription(description)
                .setCategory(category)
                ;
    }

    public ProductDTO toProductDTO(Category category){
        return new ProductDTO()
                .setId(id)
                .setProductName(productName)
                .setPrice(BigDecimal.valueOf(Long.parseLong(price)))
                .setQuantity(Long.parseLong(quantity))
                .setDescription(description)
                .setCategory(category)
                ;

    }
}
