package com.devinicius.DsCatolog.resource;

import com.devinicius.DsCatolog.dto.ProductDTO;
import com.devinicius.DsCatolog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllPaged(Pageable pageable) {
        return ResponseEntity.ok().body(productService.findAllPaged(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findbyId(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO product) {
        ProductDTO productDTO = productService.insertOne(product);

        URI location = ServletUriComponentsBuilder.
                fromCurrentRequestUri().
                path("/{id}").
                buildAndExpand(productDTO.getId()).
                toUri();

        return ResponseEntity.created(location).body(productDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> updateOne(@PathVariable Long id, @RequestBody ProductDTO dto) {
        ProductDTO productDTO =  productService.updateOne(id, dto);
        return ResponseEntity.ok().body(productDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        productService.deleteOne(id);
        return ResponseEntity.noContent().build();
    }

}
