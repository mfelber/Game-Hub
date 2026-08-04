package gamehub.game_Hub.Mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.Wishlist;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Response.GenreResponse;
import gamehub.game_Hub.Response.WishlistResponse;

@Service
public class WishlistMapper {

  public WishlistResponse toWishlistResponse(Wishlist wishlist){
    Game game = wishlist.getGame();
    System.out.printf(game.getTitle());

    return WishlistResponse.builder()
        .addedAt(wishlist.getAddedAt())
        .game(new GamePreviewResponse(
            game.getId(), game.getTitle(),
            FileUtils.readCoverFromLocation(game.getGameCoverImage()), game.getPrice(),
            game.getGenres().stream().map(genre -> new GenreResponse(
                genre.getId(), genre.getName())).collect(Collectors.toSet())))
        .build();
  }

}
