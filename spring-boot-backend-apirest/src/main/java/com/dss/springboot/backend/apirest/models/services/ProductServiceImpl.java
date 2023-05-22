package com.dss.springboot.backend.apirest.models.services;

import com.dss.springboot.backend.apirest.models.dao.ProductDao;
import com.dss.springboot.backend.apirest.models.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByName(String text) {
        return productDao.findByNameContainingIgnoreCase(text);
    }
}
