package com.dss.springboot.backend.apirest.controllers;

import com.dss.springboot.backend.apirest.models.entity.Sale;
import com.dss.springboot.backend.apirest.models.services.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api/v1")
public class SalesRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesRestController.class);

    private final SaleService saleService;

    public SalesRestController(SaleService saleService) {
        this.saleService = saleService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/sales/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {

        Sale sale;
        Map<String, Object> response = new HashMap<>();

        try {
            sale = saleService.findById(id);
        } catch (DataAccessException dae) {
            response.put("message", "Error al realizar la consulta en Base de Datos");
            response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (Objects.isNull(sale)) {
            response.put("message", "La Factura " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/sales/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {

            saleService.delete(id);
            response.put("message", "La factura ha sido eliminada con éxito!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException dae) {
            response.put("message", "Error al realizar la eliminación de la Factura");
            response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/sales")
    @ResponseStatus(HttpStatus.CREATED)
    public Sale create(@RequestBody Sale sale) {
        return saleService.save(sale);
    }
}
