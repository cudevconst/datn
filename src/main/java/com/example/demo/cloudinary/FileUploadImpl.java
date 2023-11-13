package com.example.demo.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface FileUploadImpl {
    String uploadFile(MultipartFile multipartFile) throws IOException;
    Map<Object, Object> deleteFile(String publicId) throws IOException;
}

