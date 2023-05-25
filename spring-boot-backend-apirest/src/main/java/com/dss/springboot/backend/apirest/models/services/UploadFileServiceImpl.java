package com.dss.springboot.backend.apirest.models.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileServiceImpl.class);
    private static final String UPLOADS_FOLDER = "spring-boot-backend-apirest/uploads";

    @Override
    public Resource upload(String fileName) throws MalformedURLException {
        Path path = getPath(fileName);
        LOGGER.info(path.toString());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            LOGGER.error("No se pudo cargar la imagen " + fileName);
            path = Paths.get("src/main/resources/static/images").resolve("not-user-icon.png").toAbsolutePath();
            resource = new UrlResource(path.toUri());
        }

        return resource;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString().concat("_").concat(file.getOriginalFilename().replace(" ", ""));
        Path path = getPath(fileName);
        LOGGER.info(path.toString());

        Files.copy(file.getInputStream(), path);

        return fileName;
    }

    @Override
    public boolean delete(String fileName) {
        if (Objects.nonNull(fileName) && fileName.length() > 0) {
            Path lastPathPhoto = Paths.get("uploads").resolve(fileName).toAbsolutePath();
            File lastPhotoFile = lastPathPhoto.toFile();

            if (lastPhotoFile.exists() && lastPhotoFile.canRead()) {
                lastPhotoFile.delete();
                return true;
            }
        }
        return false;
    }

    @Override
    public Path getPath(String fileName) {
        return Paths.get(UPLOADS_FOLDER).resolve(fileName).toAbsolutePath();
    }
}
