package com.example.repository;

import com.example.model.Product;
import com.example.model.ProductAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductAvatarRepository extends JpaRepository<ProductAvatar, String> {
    Optional<ProductAvatar> findByProduct(Product product);
}
