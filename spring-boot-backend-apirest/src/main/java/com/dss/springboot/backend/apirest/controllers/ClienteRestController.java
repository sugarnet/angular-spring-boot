package com.dss.springboot.backend.apirest.controllers;

import com.dss.springboot.backend.apirest.models.entity.Cliente;
import com.dss.springboot.backend.apirest.models.entity.Region;
import com.dss.springboot.backend.apirest.models.services.ClienteService;
import com.dss.springboot.backend.apirest.models.services.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api/v1")
public class ClienteRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClienteRestController.class);

    private final ClienteService clienteService;
    private final UploadFileService uploadFileService;

    public ClienteRestController(ClienteService clienteService, UploadFileService uploadFileService) {
        this.clienteService = clienteService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/clientes/page/{page}")
    public Page<Cliente> index(@PathVariable Integer page) {
        return clienteService.findAll(PageRequest.of(page, 4));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {

        Cliente cliente;
        Map<String, Object> response = new HashMap<>();

        try {
            cliente = clienteService.findById(id);
        } catch (DataAccessException dae) {
            response.put("message", "Error al realizar la consulta en Base de Datos");
            response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (Objects.isNull(cliente)) {
            response.put("message", "El cliente " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/clientes")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

        LOGGER.info("{}", cliente);
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            return getErrorsFieldResponse(result, response);
        }

        Cliente clienteNew;
        try {
            clienteNew = clienteService.save(cliente);
        } catch (DataAccessException dae) {
            response.put("message", "Error al realizar la inserción en Base de Datos");
            response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente ha sido creado con éxito!");
        response.put("cliente", clienteNew);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {

        LOGGER.info("{}", cliente);

        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            return getErrorsFieldResponse(result, response);
        }

        Cliente clienteActual = clienteService.findById(id);
        Cliente clienteUpdated;
        if (Objects.isNull(clienteActual)) {
            response.put("message", "Error al actualizar: El cliente " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());
            clienteActual.setRegion(cliente.getRegion());
            clienteUpdated = clienteService.save(clienteActual);

        } catch (DataAccessException dae) {
            response.put("message", "Error al realizar la actualización en Base de Datos");
            response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente ha sido actualizado con éxito!");
        response.put("cliente", clienteUpdated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {

            Cliente cliente = clienteService.findById(id);
            deletePhoto(cliente);

            clienteService.delete(id);
            response.put("message", "El cliente ha sido eliminado con éxito!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException dae) {
            response.put("message", "Error al realizar la eliminación del Cliente");
            response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile file, @RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();

        Cliente cliente = clienteService.findById(id);

        if (!file.isEmpty()) {

            String fileName = null;
            try {
                fileName = uploadFileService.copy(file);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("message", "Error al subir la foto del cliente ");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            deletePhoto(cliente);

            cliente.setPhoto(fileName);
            clienteService.save(cliente);

            response.put("cliente", cliente);
            response.put("message", "Ha subido correctamente el archivo " + fileName);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/clientes/upload/img/{photoName:.+}")
    public ResponseEntity<Resource> viewPhoto(@PathVariable String photoName) {

        Resource resource;
        try {
            resource = uploadFileService.upload(photoName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photoName + "\"");

        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/clientes/regiones")
    public List<Region> getRegions() {
        return clienteService.findAllRegions();
    }

    private static ResponseEntity<Map<String, Object>> getErrorsFieldResponse(BindingResult result, Map<String, Object> response) {
        List<String> errors = result.getFieldErrors().stream().map(fe -> "El campo '" + fe.getField() + "': " + fe.getDefaultMessage()).collect(Collectors.toList());

        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private void deletePhoto(Cliente cliente) {
        String lastNamePhoto = cliente.getPhoto();

        uploadFileService.delete(lastNamePhoto);
    }
}
