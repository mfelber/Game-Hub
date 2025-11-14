package gamehub.game_Hub.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

  String saveGameCoverImage(MultipartFile file, Long gameId);

  String saveUserImages(MultipartFile file, Long connectedUser);

}
