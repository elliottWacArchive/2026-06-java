package com.myrestaurant.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// stockage img
@Service
public class FileStorageService {

  private final Path uploadsDir;

  public FileStorageService(@Value("${app.uploads-dir}") String uploadsDir)
    throws IOException {
    this.uploadsDir = Path.of(uploadsDir);
    Files.createDirectories(this.uploadsDir);
  }

  public String store(MultipartFile file) {
    String original = file.getOriginalFilename();
    String ext =
      original != null && original.contains(".")
        ? original.substring(original.lastIndexOf('.'))
        : "";
    String filename = UUID.randomUUID() + ext;
    try {
      file.transferTo(uploadsDir.resolve(filename).toAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return filename;
  }
}
