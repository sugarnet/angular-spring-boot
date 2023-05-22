package com.dss.springboot.backend.apirest.models.services;

import com.dss.springboot.backend.apirest.models.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findProductsByName(String text);
}
