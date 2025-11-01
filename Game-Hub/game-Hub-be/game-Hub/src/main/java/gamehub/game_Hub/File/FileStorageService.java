package gamehub.game_Hub.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

  String saveFile(MultipartFile file);

  String saveUserImages(MultipartFile file, Long connectedUser);

}
