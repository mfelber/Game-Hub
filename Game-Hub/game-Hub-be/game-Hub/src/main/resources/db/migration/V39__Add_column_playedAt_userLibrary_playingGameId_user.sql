ALTER TABLE game_hub.user_library
  ADD COLUMN IF NOT EXISTS played_at TIMESTAMP;

ALTER TABLE game_hub."user"
  ADD COLUMN IF NOT EXISTS currently_playing_game_id BIGINT,
  ADD CONSTRAINT fk_user_currently_playing_game
    FOREIGN KEY (currently_playing_game_id)
      REFERENCES game_hub.game (id);