package com.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class UploadImageUtils {

    public String setNameImage(String fileName, Long id) {

        Long firstName = 0L;

        firstName = id + 1;

        String name = String.valueOf(firstName);
        name = name.concat(fileName);

        return name;
    }

    public boolean save(MultipartFile file, String fileName, Path root) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(root.toAbsolutePath());
        stringBuilder.append(fileName);

        Path pathFileIcon = Paths.get(stringBuilder.toString());

        try {
            if (!root.toFile().exists()) {
                Files.createDirectory(root);
            }

            if (Files.notExists(pathFileIcon)) {
                Files.copy(file.getInputStream(), root.resolve(fileName));
            } else {
                Files.deleteIfExists(pathFileIcon);
                Files.copy(file.getInputStream(), root.resolve(fileName));
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(Path root, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(root.toAbsolutePath());
        stringBuilder.append("/");
        stringBuilder.append(fileName);

        Path pathFileIcon = Paths.get(stringBuilder.toString());

        try {
            if (Files.deleteIfExists(pathFileIcon)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
}
