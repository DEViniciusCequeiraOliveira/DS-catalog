package com.devinicius.DsCatolog.services;

import com.devinicius.DsCatolog.dto.CategoryDTO;
import com.devinicius.DsCatolog.entities.Category;
import com.devinicius.DsCatolog.repositories.CategoryRepository;
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
public class CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        Category category = obj.orElseThrow(() -> new EntityNotFoundExceptions("Entity not found"));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insertOne(CategoryDTO dto) {
        Category category = categoryRepository.save(new Category(dto));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updateOne(Long id, CategoryDTO dto) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(dto.getName());
            return new CategoryDTO(category);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundExceptions("Id not found " + id);
        }
    }

    public void deleteOne(Long id) {
        try {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
            } else {
                throw new EntityNotFoundExceptions("Id not found " + id);
            }
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseExceptions("Integrity violation");
        } catch (EntityNotFoundExceptions e) {
            throw new EntityNotFoundExceptions(e.getMessage());
        }
    }
}
