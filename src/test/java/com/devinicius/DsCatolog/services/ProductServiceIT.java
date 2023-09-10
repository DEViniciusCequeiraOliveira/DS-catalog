package com.devinicius.DsCatolog.services;

import com.devinicius.DsCatolog.dto.ProductDTO;
import com.devinicius.DsCatolog.repositories.ProductRepository;
import com.devinicius.DsCatolog.services.exceptions.EntityNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    public void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        productService.deleteOne(existingId);

        Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EntityNotFoundExceptions.class, () -> {
            productService.deleteOne(nonExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {
        Integer page = 0, size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(page, result.getNumber());
        Assertions.assertEquals(size, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());

    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        Integer pageNotExists = 50;

        PageRequest pageRequest = PageRequest.of(pageNotExists, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}
