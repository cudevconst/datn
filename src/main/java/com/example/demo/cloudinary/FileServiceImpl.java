package com.example.demo.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileServiceImpl {


    String saveFile(MultipartFile file, String nameImage);

    byte[] downloadFile(String filename);


    String deleteFile(String filename);


    List<String> listAllFiles();
}