package com.example.repository;

import com.example.model.Product;
import com.example.model.dto.ProductDTO;
import com.example.model.dto.ProductResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByDeletedIsFalse();

    @Query("SELECT NEW com.example.model.dto.ProductDTO (" +
            "p.id, " +
            "p.productName, " +
            "p.price, " +
            "p.quantity, " +
            "p.description, " +
            "p.category" +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false "
    )
    List<ProductDTO> findAllProductDTOByDeletedIsFalse();

    @Query("SELECT NEW com.example.model.dto.ProductResDTO (" +
            "p.id, " +
            "p.productName, " +
            "p.price, " +
            "p.quantity, " +
            "p.description, " +
            "pa.id," +
            "pa.fileFolder, " +
            "pa.fileName, " +
            "pa.fileUrl, " +
            "p.category" +
            ") " +
            "FROM Product AS p " +
            "LEFT JOIN ProductAvatar AS pa " +
            "ON pa.product = p " +
            "WHERE p.deleted = false "
    )
    List<ProductResDTO> findAllProductResDTOByDeletedIsFalse();
}
