CREATE TABLE game_hub.user_saved_lfg_post
(
  user_id     BIGINT    NOT NULL,
  lfg_post_id BIGINT    NOT NULL,
  saved_at    TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT pk_saved_lfg_post PRIMARY KEY (user_id, lfg_post_id),

  CONSTRAINT fk_user_saved_lfg_post_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id),

  CONSTRAINT fk_user_saved_lfg_post_lfg
    FOREIGN KEY (lfg_post_id)
      REFERENCES game_hub.lfg_post (id)
);