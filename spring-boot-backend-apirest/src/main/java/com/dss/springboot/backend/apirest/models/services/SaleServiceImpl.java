package com.dss.springboot.backend.apirest.models.services;

import com.dss.springboot.backend.apirest.models.dao.SaleDao;
import com.dss.springboot.backend.apirest.models.entity.Sale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleDao saleDao;

    public SaleServiceImpl(SaleDao saleDao) {
        this.saleDao = saleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Sale findById(Long id) {
        return saleDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Sale save(Sale sale) {
        return saleDao.save(sale);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        saleDao.deleteById(id);
    }
}
