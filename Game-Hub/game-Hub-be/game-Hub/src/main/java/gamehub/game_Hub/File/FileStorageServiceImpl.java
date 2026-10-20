package gamehub.game_Hub.File;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

  @Value("${application.file.upload.image-cover-path}")
  private String fileUploadPath;

  private static final String SEPARATOR = "/";

  @Override
  public String saveGameCoverImage(@NonNull final MultipartFile sourceFile, Long gameId) {
    final String fileUploadSubPath = "games" + SEPARATOR + gameId;
    return uploadFile(sourceFile, fileUploadSubPath);
  }

  @Override
  public String saveUserImages(@NonNull final MultipartFile sourceFile, @NonNull final Long userId) {
    final String fileUploadSubPath = "users" + SEPARATOR + userId;
    return uploadUserImages(sourceFile, fileUploadSubPath);
  }

  private String uploadUserImages(final @NonNull MultipartFile sourceFile, @NonNull final String fileUploadSubPath) {
    String finalfileUploadPath = fileUploadPath + SEPARATOR + fileUploadSubPath;

    File targetFolder = new File(finalfileUploadPath);
    if (!targetFolder.exists()) {
      boolean folderCreated = targetFolder.mkdirs();
      if (!folderCreated) {
        log.warn("Could not create folder " + targetFolder.getAbsolutePath());
        return null;
      }
    }

    final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
    String targetFilePath = finalfileUploadPath + SEPARATOR + currentTimeMillis() + "." + fileExtension;
    Path targetPath = Paths.get(targetFilePath);

    try {
      Files.write(targetPath, sourceFile.getBytes());
      log.info("Saved file: " + targetFilePath);
      return targetFilePath;
    } catch (IOException e) {
      log.error("File was not saved" , e);
    }

    return null;
  }

  private String uploadFile(final @NonNull MultipartFile sourceFile, final String fileUploadSubPath) {
    final String finalUploadPath = fileUploadPath + SEPARATOR + fileUploadSubPath;
    File targetFolder = new File(finalUploadPath);
    if (!targetFolder.exists()) {
      boolean folderCreated = targetFolder.mkdirs();
      if (!folderCreated) {
        log.warn("Could not create folder: " + targetFolder.getAbsolutePath());
        return null;
      }
    }

    final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
    String targetFilePath = finalUploadPath + SEPARATOR + sourceFile.getOriginalFilename();
    Path targetPath = Paths.get(targetFilePath);
    try {
      Files.write(targetPath, sourceFile.getBytes());
      log.info("Saved file: " + targetFilePath);
      return targetFilePath;
    } catch (IOException e) {
      log.error("File was not saved" , e);
    }
    return null;
  }

  private String getFileExtension(final String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      return null;
    }

    int lastIndex = fileName.lastIndexOf(".");
    if (lastIndex == -1){
      return null;
    }

    return fileName.substring(lastIndex + 1).toLowerCase();
  }

}
