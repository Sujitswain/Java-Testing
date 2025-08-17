package com.sujit.Testing.service;

import com.sujit.Testing.Exception.ResourceNotFoundException;
import com.sujit.Testing.entity.Product;
import com.sujit.Testing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product update(Long id, Product productDetails) {
        Product existing = getById(id);
        existing.setName(productDetails.getName());
        existing.setPrice(productDetails.getPrice());
        existing.setQuantity(productDetails.getQuantity());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Product existing = getById(id);
        repository.delete(existing);
    }
}
