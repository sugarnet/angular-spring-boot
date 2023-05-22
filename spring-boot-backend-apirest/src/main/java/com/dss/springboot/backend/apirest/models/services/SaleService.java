package com.dss.springboot.backend.apirest.models.services;

import com.dss.springboot.backend.apirest.models.entity.Sale;

public interface SaleService {
    Sale findById(Long id);
    Sale save(Sale sale);
    void delete(Long id);
}
