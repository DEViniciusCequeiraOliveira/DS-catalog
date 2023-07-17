package com.devinicius.DsCatolog.resource;

import com.devinicius.DsCatolog.dto.CategoryDTO;
import com.devinicius.DsCatolog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(categoryService.findAllPaged(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insertOne(@RequestBody CategoryDTO dto) {
        CategoryDTO category = categoryService.insertOne(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();

        return ResponseEntity.created(location).body(category);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> updateOne(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        CategoryDTO category = categoryService.updateOne(id, dto);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        categoryService.deleteOne(id);
        return ResponseEntity.noContent().build();
    }
}

