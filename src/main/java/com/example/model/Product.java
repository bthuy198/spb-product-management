package com.example.model;

import com.example.model.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "quantity", nullable = false)
    private Long quantity;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    public ProductDTO toProductDTO(){
        return new ProductDTO()
                .setId(id)
                .setProductName(productName)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description)
                .setCategory(category)
                ;
    }
}
