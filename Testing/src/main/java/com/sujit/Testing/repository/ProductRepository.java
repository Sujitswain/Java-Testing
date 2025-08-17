package com.sujit.Testing.repository;

import com.sujit.Testing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
