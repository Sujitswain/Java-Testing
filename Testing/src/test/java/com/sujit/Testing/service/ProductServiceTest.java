package com.sujit.Testing.service;

import com.sujit.Testing.Exception.ResourceNotFoundException;
import com.sujit.Testing.entity.Product;
import com.sujit.Testing.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    @DisplayName("Test for getAllProduct")
    void testGetAllProducts_WhenProductsExist_ThenReturnProductList() {
        // Arrange
        List<Product> products = List.of(new Product(1L, "Laptop", 1000.0, 5));
        Mockito.when(repository.findAll()).thenReturn(products);

        // Act
        List<Product> result = service.getAll();

        //Assert
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test for getByProductId")
    void testGetById_WhenProductIdPassed_ReturnTheProduct() {
        // Arrange
        Product product = new Product(1L, "Phone", 500.0, 10);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Product result = service.getById(1L);

        // Assert
        assertEquals("Phone", result.getName());
    }

    @Test
    @DisplayName("Test for createProduct")
    void testCreateProduct_WhenNewProductIsSaved_ThenReturnTheSameProductInResponse() {
        Product product = new Product(null, "Tablet", 300.0, 3);
        Product savedProduct = new Product(1L, "Tablet", 300.0, 3);

        when(repository.save(product)).thenReturn(savedProduct);

        Product result = service.create(product);

        assertNotNull(result.getId());
        assertEquals("Tablet", result.getName());
    }

    @Test
    @DisplayName("Test for updateProduct")
    void TestUpdateProduct_WhenProductDetailsIsUpdated_ThenReturnUpdatedProduct() {
        Product existing = new Product(1L, "TV", 200.0, 2);
        Product update = new Product(null, "Smart TV", 400.0, 4);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenReturn(existing);

        Product result = service.update(1L, update);

        assertEquals("Smart TV", result.getName());
        assertEquals(400.0, result.getPrice());
    }

    @Test
    @DisplayName("Test for deleteProduct")
    void testDeleteProduct_WhenProductIdIsGivenForDeletion_ThenDeleteProduct() {
        Product product = new Product(1L, "Camera", 150.0, 1);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        verify(repository, times(1)).delete(product);
    }

    @Test
    @DisplayName("getById should throw ResourceNotFoundException when product does not exist")
    void testGetById_WhenProductNotFound_ThrowsException() {
        Long id = 9L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getById(id));

        assertEquals("Product not found with id " + id, ex.getMessage());
    }

    @Test
    @DisplayName("delete should throw ResourceNotFoundException when product does not exist")
    void testDelete_WhenProductNotFound_ThrowsException() {
        Long id = 123L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));

        verify(repository, never()).delete(any());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, 999L})
    @DisplayName("getById should throw exception for invalid product IDs")
    void testGetById_InvalidIds_ThrowsException(Long invalidId) {
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(invalidId));
    }

    @ParameterizedTest
    @CsvSource({
            "Laptop, 1000.0, 5",
            "Tablet, 500.0, 2",
            "Phone, 800.0, 10"
    })
    @DisplayName("create should return product with correct details")
    void testCreateProduct_MultipleCases(String name, Double price, Integer quantity) {
        Product product = new Product(null, name, price, quantity);
        Product saved = new Product(1L, name, price, quantity);

        when(repository.save(product)).thenReturn(saved);

        Product result = service.create(product);

        assertEquals(name, result.getName());
        assertEquals(price, result.getPrice());
        assertEquals(quantity, result.getQuantity());
        assertNotNull(result.getId());
    }

}