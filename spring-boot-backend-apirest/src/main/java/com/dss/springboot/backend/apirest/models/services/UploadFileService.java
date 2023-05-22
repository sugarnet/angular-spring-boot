package com.dss.springboot.backend.apirest.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface UploadFileService {
    Resource upload(String fileName) throws MalformedURLException;
    String copy(MultipartFile file) throws IOException;
    boolean delete(String fileName);
    Path getPath(String fileName);
}
