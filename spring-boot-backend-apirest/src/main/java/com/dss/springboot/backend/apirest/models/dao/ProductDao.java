package com.dss.springboot.backend.apirest.models.dao;

import com.dss.springboot.backend.apirest.models.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductDao extends CrudRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String text);
}
