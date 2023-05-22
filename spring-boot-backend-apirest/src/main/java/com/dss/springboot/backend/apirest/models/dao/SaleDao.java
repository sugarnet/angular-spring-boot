package com.dss.springboot.backend.apirest.models.dao;

import com.dss.springboot.backend.apirest.models.entity.Sale;
import org.springframework.data.repository.CrudRepository;

public interface SaleDao extends CrudRepository<Sale, Long> {
}
