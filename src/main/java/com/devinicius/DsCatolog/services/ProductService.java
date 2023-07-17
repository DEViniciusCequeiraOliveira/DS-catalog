package com.devinicius.DsCatolog.services;

import com.devinicius.DsCatolog.dto.ProductDTO;
import com.devinicius.DsCatolog.entities.Category;
import com.devinicius.DsCatolog.entities.Product;
import com.devinicius.DsCatolog.repositories.CategoryRepository;
import com.devinicius.DsCatolog.repositories.ProductRepository;
import com.devinicius.DsCatolog.services.exceptions.DatabaseExceptions;
import com.devinicius.DsCatolog.services.exceptions.EntityNotFoundExceptions;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findbyId(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product product = obj.orElseThrow(() -> new EntityNotFoundExceptions("Entity not found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insertOne(ProductDTO dto) {
        Product product = new Product();
        copyDtoToEntity(dto, product);
        product = productRepository.save(product);
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO updateOne(Long id, ProductDTO dto) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, product);
            return new ProductDTO(product, product.getCategories());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundExceptions("Id not found " + id);
        }
    }

    public void deleteOne(Long id) {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            } else  {
                throw new EntityNotFoundException();
            }
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundExceptions("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseExceptions("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImgUrl(dto.getImgUrl());
        product.setPrice(dto.getPrice());
        product.setDate(dto.getDate());
        product.setPrice(dto.getPrice());

        product.getCategories().clear();
        dto.getCategories().forEach(categoryDTO -> {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            product.getCategories().add(category);
        });
    }

}
