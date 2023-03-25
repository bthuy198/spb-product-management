package com.example.service.productAvatar;

import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.repository.ProductAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductAvatarServiceImpl implements IProductAvatarService{
    @Autowired
    private ProductAvatarRepository productAvatarRepository;
    @Override
    public List<ProductAvatar> findAll() {
        return productAvatarRepository.findAll();
    }

    @Override
    public Optional<ProductAvatar> findById(String id) {
        return productAvatarRepository.findById(id);
    }

    @Override
    public Boolean existById(String id) {
        return productAvatarRepository.existsById(id);
    }

    @Override
    public ProductAvatar save(ProductAvatar productAvatar) {
        return productAvatarRepository.save(productAvatar);
    }

    @Override
    public void delete(ProductAvatar productAvatar) {

    }

    @Override
    public Optional<ProductAvatar> findByProduct(Product product) {
        return productAvatarRepository.findByProduct(product);
    }
}
