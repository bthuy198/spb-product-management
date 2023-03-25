package com.example.service.productAvatar;

import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.service.IGeneralStringService;

import java.util.Optional;

public interface IProductAvatarService extends IGeneralStringService<ProductAvatar> {
    Optional<ProductAvatar> findByProduct(Product product);
}
