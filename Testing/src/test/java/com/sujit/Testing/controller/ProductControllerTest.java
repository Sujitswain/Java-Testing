package com.sujit.Testing.controller;

import com.sujit.Testing.Exception.ResourceNotFoundException;
import com.sujit.Testing.entity.Product;
import com.sujit.Testing.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void testGetAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(new Product(1L, "Laptop", 1000.0, 5)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void testGetById() throws Exception {
        when(service.getById(1L)).thenReturn(new Product(1L, "Phone", 500.0, 10));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"));
    }

    @Test
    void testCreate() throws Exception {
        Product product = new Product(1L, "Tablet", 300.0, 3);
        when(service.create(any(Product.class))).thenReturn(product);

        String requestBody = """
                {
                  "name": "Tablet",
                  "price": 300,
                  "quantity": 3
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tablet"));
    }

    @Test
    void testUpdate() throws Exception {
        Product updated = new Product(1L, "Smart TV", 400.0, 4);
        when(service.update(anyLong(), any(Product.class))).thenReturn(updated);

        String requestBody = """
                {
                  "name": "Smart TV",
                  "price": 400,
                  "quantity": 4
                }
                """;

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smart TV"));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Product not found with id 99"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate_InvalidInput_ShouldReturnBadRequest() throws Exception {
        String invalidRequestBody = """
            {
              "name": "",
              "price": 0,
              "quantity": -1
            }
            """;

        mockMvc.perform(post("/api/products")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }

}