package gamehub.game_Hub.File;

import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.Module.Game;

public interface FileStorageService {

  String saveGameCoverImage(MultipartFile file, Long gameId);

  String saveUserImages(MultipartFile file, Long connectedUser);

}
