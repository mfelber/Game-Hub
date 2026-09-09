ALTER TABLE game_hub.user
  ADD COLUMN IF NOT EXISTS favorite_game_id BIGINT,
  ADD CONSTRAINT fk_user_favorite_game
    FOREIGN KEY (favorite_game_id)
      REFERENCES game_hub.game (id);