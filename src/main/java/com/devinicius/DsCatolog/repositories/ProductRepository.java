package com.devinicius.DsCatolog.repositories;

import com.devinicius.DsCatolog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
