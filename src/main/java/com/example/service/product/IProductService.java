package com.example.service.product;

import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.model.dto.ProductCreateAvatarResDTO;
import com.example.model.dto.ProductDTO;
import com.example.model.dto.ProductResDTO;
import com.example.model.dto.ProductUpdateAvatarResDTO;
import com.example.service.IGeneralService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService extends IGeneralService<Product> {
    List<Product> findAllByDeletedIsFalse();
    List<ProductDTO> findAllProductDTOByDeletedIsFalse();

    ProductCreateAvatarResDTO createWithAvatar(Product product, MultipartFile fileAvatar);
    ProductUpdateAvatarResDTO updateWithAvatar(Product product, MultipartFile avatarFile) throws IOException;
    List<ProductResDTO> findAllProductResDTOByDeletedIsFalse();
    Optional<ProductResDTO> findProductResDTOByDeletedIsFalse(Long id);
    ProductUpdateAvatarResDTO update(Product product);
}
