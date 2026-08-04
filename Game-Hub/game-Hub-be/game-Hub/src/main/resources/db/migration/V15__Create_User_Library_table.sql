CREATE TABLE IF NOT EXISTS game_hub.user_library (
  user_id BIGINT NOT NULL,
  game_id BIGINT NOT NULL,
  installed BOOLEAN DEFAULT FALSE,
  playtime_minutes INTEGER DEFAULT 0,
  favorite BOOLEAN DEFAULT FALSE,
  last_played TIMESTAMP,
  "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_user_library
    PRIMARY KEY (user_id, game_id),

  CONSTRAINT fk_user_library_user
    FOREIGN KEY (user_id)
    REFERENCES game_hub."user"(id),

  CONSTRAINT fk_user_library_game
  FOREIGN KEY (game_id)
  REFERENCES game_hub.game(id)
);