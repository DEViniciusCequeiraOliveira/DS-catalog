package com.devinicius.DsCatolog.services;

import com.devinicius.DsCatolog.dto.ProductDTO;
import com.devinicius.DsCatolog.entities.Category;
import com.devinicius.DsCatolog.entities.Product;
import com.devinicius.DsCatolog.repositories.CategoryRepository;
import com.devinicius.DsCatolog.repositories.ProductRepository;
import com.devinicius.DsCatolog.services.exceptions.DatabaseExceptions;
import com.devinicius.DsCatolog.services.exceptions.EntityNotFoundExceptions;
import com.devinicius.DsCatolog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    private Long existsId, nonExistsId, integrityViolationId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;

    @BeforeEach
    public void setUp() {
        existsId = 1L;
        nonExistsId = 2L;
        integrityViolationId = 3L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        //deleteShouldDoNothingWhenIdExists
        Mockito.when(productRepository.existsById(existsId)).thenReturn(true);
        Mockito.doNothing().when(productRepository).deleteById(existsId);
        //deleteShouldDoThrowEntityNotFoundExceptionWhenIdNonExists
        Mockito.doThrow(EntityNotFoundException.class).when(productRepository).existsById(nonExistsId);
        //deleteShouldDoThrowDataIntegrityViolationExceptionWhenIdIntegrityViolation
        Mockito.when(productRepository.existsById(integrityViolationId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(integrityViolationId);
        //findAllPagedShouldDoListProductPaged
        page = new PageImpl<>(List.of(product));
        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        //saveShouldSaveOneProduct
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        //
        Mockito.when(productRepository.getReferenceById(existsId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistsId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(categoryRepository.getReferenceById(category.getId())).thenReturn(category);
        //
        Mockito.when(productRepository.findById(existsId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistsId)).thenReturn(Optional.empty());
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            productService.deleteOne(existsId);
        });
        Mockito.verify(productRepository).existsById(existsId);
        Mockito.verify(productRepository).deleteById(existsId);
    }

    @Test
    public void deleteShouldDoThrowEntityNotFoundExceptionWhenIdNonExists() {
        Assertions.assertThrows(EntityNotFoundExceptions.class, () -> {
            productService.deleteOne(nonExistsId);
        });
        Mockito.verify(productRepository).existsById(nonExistsId);
        Mockito.verify(productRepository, Mockito.never()).deleteById(nonExistsId);
    }

    @Test
    public void deleteShouldDoThrowDataIntegrityViolationExceptionWhenIdIntegrityViolation() {
        Assertions.assertThrows(DatabaseExceptions.class, () -> {
            productService.deleteOne(integrityViolationId);
        });

        Mockito.verify(productRepository).existsById(integrityViolationId);
        Mockito.verify(productRepository).deleteById(integrityViolationId);
    }

    @Test
    public void findAllPagedShouldDoListProductPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> productAllPaged = productService.findAllPaged(pageable);

        Assertions.assertNotNull(productAllPaged);
        Mockito.verify(productRepository).findAll(pageable);
    }

    @Test
    public void saveShouldSaveOneProduct() {

        ProductDTO productDTO = productService.insertOne(Factory.createProductDTO());

        Assertions.assertNotNull(productDTO);
        Mockito.verify(productRepository).save(ArgumentMatchers.any());
        Mockito.verify(categoryRepository).getReferenceById(productDTO.getCategories().get(0).getId());
    }

    @Test
    public void updateShouldUpdateOneProductWhenIdExists() {
        ProductDTO productDTO = Factory.createProductDTO();
        ProductDTO entity = productService.updateOne(productDTO.getId(), productDTO);

        Assertions.assertNotNull(entity);
        Mockito.verify(productRepository).getReferenceById(productDTO.getId());
        Mockito.verify(categoryRepository).getReferenceById(productDTO.getCategories().get(0).getId());
    }


    @Test
    public void updateShouldUpdateOneProductWhenIdNonExists() {
        ProductDTO productDTO = Factory.createProductDTO();

        Assertions.assertThrows(EntityNotFoundExceptions.class, () -> {
            ProductDTO entity = productService.updateOne(nonExistsId, productDTO);
            Assertions.assertNull(entity);
        });

        Mockito.verify(productRepository).getReferenceById(nonExistsId);
    }

    @Test
    public void findByIdShouldFindObjectWhenIdExists() {
        ProductDTO productDTO = productService.findbyId(existsId);

        Assertions.assertNotNull(productDTO);

        Mockito.verify(productRepository).findById(existsId);
    }

    @Test
    public void findByIdShouldFindObjectWhenIdNotExists() {
        Assertions.assertThrows(EntityNotFoundExceptions.class, () -> {
            ProductDTO productDTO = productService.findbyId(nonExistsId);
        });

        Mockito.verify(productRepository).findById(nonExistsId);
    }
}
