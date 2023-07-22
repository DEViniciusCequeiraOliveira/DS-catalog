package com.devinicius.DsCatolog.tests;

import com.devinicius.DsCatolog.dto.ProductDTO;
import com.devinicius.DsCatolog.entities.Category;
import com.devinicius.DsCatolog.entities.Product;

import java.math.BigDecimal;
import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Tv", "Smart TV 4K ", new BigDecimal(4200), "https://images.samsung.com/is/image/samsung/p6pim/br/qn55q65cagxzd/gallery/br-qled-q60c-qn55q65cagxzd-537587569?$650_519_PNG$", Instant.now());
        product.getCategories().add(new Category(2L, "Eletronics"));

        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
