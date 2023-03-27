package com.example.service.product;

import com.example.exception.DataInputException;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.model.dto.ProductCreateAvatarResDTO;
import com.example.model.dto.ProductDTO;
import com.example.model.dto.ProductResDTO;
import com.example.model.dto.ProductUpdateAvatarResDTO;
import com.example.repository.ProductAvatarRepository;
import com.example.repository.ProductRepository;
import com.example.service.uploadMedia.UploadService;
import com.example.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductService implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductAvatarRepository productAvatarRepository;
    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadUtils uploadUtils;
    @Override
    public List<Product> findAll() {
        return productRepository.findAllByDeletedIsFalse();
    }

    @Override
    public List<Product> findAllByDeletedIsFalse() {
        return productRepository.findAllByDeletedIsFalse();
    }

    @Override
    public List<ProductDTO> findAllProductDTOByDeletedIsFalse() {
        return productRepository.findAllProductDTOByDeletedIsFalse();
    }

    @Override
    public List<ProductResDTO> findAllProductResDTOByDeletedIsFalse() {
        return productRepository.findAllProductResDTOByDeletedIsFalse();
    }

    @Override
    public Optional<ProductResDTO> findProductResDTOByDeletedIsFalse(Long id) {
        return productRepository.findProductResDTOByDeletedIsFalse(id);
    }

    @Override
    public ProductUpdateAvatarResDTO update(Product product) {
        productRepository.save(product);
        ProductAvatar productAvatar = productAvatarRepository.findByProduct(product).get();

        return new ProductUpdateAvatarResDTO(product, productAvatar.toProductAvatarDTO());
    }


    @Override
    public ProductUpdateAvatarResDTO updateWithAvatar(Product product, MultipartFile avatarFile) throws IOException {
        productRepository.save(product);

        Optional<ProductAvatar> productAvatarOptional = productAvatarRepository.findByProduct(product);

        ProductAvatar productAvatar = new ProductAvatar();

        if (!productAvatarOptional.isPresent()) {
            productAvatar.setProduct(product);

            productAvatarRepository.save(productAvatar);

            uploadAndSaveCustomerAvatar(avatarFile, productAvatar);
        }
        else {
            productAvatar = productAvatarOptional.get();
            uploadService.destroyImage(productAvatar.getCloudId(), uploadUtils.buildImageUploadParams(productAvatar));
            uploadAndSaveCustomerAvatar(avatarFile, productAvatar);
        }

        return new ProductUpdateAvatarResDTO(product, productAvatar.toProductAvatarDTO());
    }

    private void uploadAndSaveCustomerAvatar(MultipartFile file, ProductAvatar productAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(file, uploadUtils.buildImageUploadParams(productAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productAvatar.setFileName(productAvatar.getId() + "." + fileFormat);
            productAvatar.setFileUrl(fileUrl);
            productAvatar.setFileFolder(uploadUtils.IMAGE_UPLOAD_FOLDER);
            productAvatar.setCloudId(productAvatar.getFileFolder() + "/" + productAvatar.getId());
            productAvatarRepository.save(productAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload image fail");
        }
    }
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public ProductCreateAvatarResDTO createWithAvatar(Product product, MultipartFile fileAvatar) {
        Category category = product.getCategory();

        product.setCategory(category);
        productRepository.save(product);

        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar.setProduct(product);
        productAvatarRepository.save(productAvatar);

        uploadAndSaveProductAvatar(fileAvatar, productAvatar);

        return new ProductCreateAvatarResDTO(product, productAvatar.toProductAvatarDTO());
    }

    private void uploadAndSaveProductAvatar(MultipartFile file, ProductAvatar productAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(file, uploadUtils.buildImageUploadParams(productAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productAvatar.setFileName(productAvatar.getId() + "." + fileFormat);
            productAvatar.setFileUrl(fileUrl);
            productAvatar.setFileFolder(uploadUtils.IMAGE_UPLOAD_FOLDER);
            productAvatar.setCloudId(productAvatar.getFileFolder() + "/" + productAvatar.getId());
            productAvatarRepository.save(productAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

}
