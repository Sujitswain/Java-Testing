package com.sujit.Testing.controller;

import com.sujit.Testing.entity.Product;
import com.sujit.Testing.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // GET all products
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.getAll()); // 200 OK
    }

    // GET product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id)); // 200 OK
    }

    // CREATE product
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        return new ResponseEntity<>(service.create(product), HttpStatus.CREATED); // 201 CREATED
    }

    // UPDATE product
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(service.update(id, product)); // 200 OK
    }

    // DELETE product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204 NO CONTENT
    }
}
