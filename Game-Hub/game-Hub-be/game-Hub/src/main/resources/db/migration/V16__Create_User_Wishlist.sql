CREATE TABLE IF NOT EXISTS game_hub.wishlist (
  user_id BIGINT NOT NULL,
  game_id BIGINT NOT NULL,
  added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_user_wishlist
  PRIMARY KEY (user_id, game_id),

  CONSTRAINT fk_user_wishlist_user
  FOREIGN KEY (user_id)
  REFERENCES game_hub."user"(id),

  CONSTRAINT fk_user_wishlist_game
  FOREIGN KEY (game_id)
  REFERENCES game_hub.game(id)
  );