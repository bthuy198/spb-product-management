package com.example.model.dto;

import com.example.model.Product;
import com.example.model.ProductAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductAvatarDTO {
    private String id;
    private String fileName;
    private String fileFolder;
    private String fileUrl;
    private String fileType;
    private String cloudId;
    public ProductAvatar toProductAvatar(Product product){
        return new ProductAvatar()
                .setId(id)
                .setFileName(fileName)
                .setFileFolder(fileFolder)
                .setFileUrl(fileUrl)
                .setFileType(fileType)
                .setCloudId(cloudId)
                .setProduct(product)
                ;
    }
}
