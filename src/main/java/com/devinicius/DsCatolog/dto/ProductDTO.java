package com.devinicius.DsCatolog.dto;


import com.devinicius.DsCatolog.entities.Category;
import com.devinicius.DsCatolog.entities.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Instant date;
    private String imgUrl;
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, BigDecimal price, Instant date, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.date = date;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.date = product.getDate();
        this.imgUrl = product.getImgUrl();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public ProductDTO(Product product, Set<Category> category) {
        this(product);
        category.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }
}
