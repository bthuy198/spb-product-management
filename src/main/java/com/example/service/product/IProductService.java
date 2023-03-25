package com.example.service.product;

import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.model.dto.ProductCreateAvatarResDTO;
import com.example.model.dto.ProductDTO;
import com.example.model.dto.ProductResDTO;
import com.example.service.IGeneralService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService extends IGeneralService<Product> {
    List<Product> findAllByDeletedIsFalse();
    List<ProductDTO> findAllProductDTOByDeletedIsFalse();

    ProductCreateAvatarResDTO createWithAvatar(Product product, MultipartFile fileAvatar);
    List<ProductResDTO> findAllProductResDTOByDeletedIsFalse();
}
