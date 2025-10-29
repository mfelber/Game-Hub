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

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

  @Value("${application.file.upload.image-cover-path}")
  private String fileUploadPath;

  @Override
  public String saveFile(@NotNull final MultipartFile sourceFile) {
    return uploadFile(sourceFile);
  }

  private String uploadFile(final @NotNull MultipartFile sourceFile) {
    final String uploadPath = fileUploadPath;
    File targetFolder = new File(uploadPath);
    if (!targetFolder.exists()) {
      boolean folderCreated = targetFolder.mkdirs();
      if (!folderCreated) {
        log.warn("Could not create folder: " + targetFolder.getAbsolutePath());
        return null;
      }
    }

    final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
    String targetFilePath = uploadPath + separator + currentTimeMillis() + "." + fileExtension;
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

//   TODO create method for uploading userprofile picture with dedicated folder user/profilepicture...

}
