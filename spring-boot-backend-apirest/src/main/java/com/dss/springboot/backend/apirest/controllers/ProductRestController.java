package com.dss.springboot.backend.apirest.controllers;

import com.dss.springboot.backend.apirest.models.entity.Product;
import com.dss.springboot.backend.apirest.models.services.ProductService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/products/search/{text}")
    public ResponseEntity<?> findProductByName(@PathVariable String text) {

        List<Product> products;
        Map<String, Object> response = new HashMap<>();

        try {
            products = productService.findProductsByName(text);
        } catch (DataAccessException dae) {
            response.put("message", "Error al realizar la consulta en Base de Datos");
            response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (Objects.isNull(products) || products.isEmpty()) {
            products = new ArrayList<>();
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
