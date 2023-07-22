package com.devinicius.DsCatolog.repositories;

import com.devinicius.DsCatolog.entities.Product;
import com.devinicius.DsCatolog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdNonExists() {
        productRepository.deleteById(nonExistingId);

        Optional<Product> result = productRepository.findById(nonExistingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldIncrementIdWithObjectWhenIdNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        Product result = productRepository.save(product);

        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(product.getId(), 1L + countTotalProducts);
    }

    @Test
    public void findShouldFindObjectWhenIdExists() {
        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findShouldNotFoundObjectWhenIdNonExists() {
        Optional<Product> result = productRepository.findById(nonExistingId);

        Assertions.assertFalse(result.isPresent());
    }


}
