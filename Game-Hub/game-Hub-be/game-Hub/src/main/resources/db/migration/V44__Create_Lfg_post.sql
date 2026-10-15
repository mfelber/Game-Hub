CREATE TABLE game_hub.lfg_post
(
  id          BIGSERIAL PRIMARY KEY,
  author_id   BIGINT      NOT NULL,
  game_id     BIGINT      NOT NULL,
  looking_for VARCHAR(30),
  play_style  VARCHAR(30),
  commitment  VARCHAR(30),
  voice_chat  VARCHAR(30),
  personality VARCHAR(30),
  description VARCHAR(300),
  flexible    BOOLEAN     NOT NULL DEFAULT FALSE,
  lfg_status  VARCHAR(20) NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL,
  expires_at  TIMESTAMPTZ NOT NULL,

  CONSTRAINT fk_lfg_post_author
    FOREIGN KEY (author_id)
      REFERENCES game_hub."user" (id),

  CONSTRAINT fk_lfg_post_game
    FOREIGN KEY (game_id)
      REFERENCES game_hub.game (id)
);

CREATE INDEX idx_lfg_post_game ON game_hub.lfg_post (game_id);
CREATE INDEX idx_lfg_post_author ON game_hub.lfg_post (author_id);
CREATE INDEX idx_lfg_post_status_exp ON game_hub.lfg_post (lfg_status, expires_at);

CREATE TABLE game_hub.lfg_post_platforms
(
  lfg_post_id BIGINT      NOT NULL,
  platform    VARCHAR(30) NOT NULL,
  PRIMARY KEY (lfg_post_id, platform),
  CONSTRAINT fk_lfg_post_platforms_post FOREIGN KEY (lfg_post_id)
    REFERENCES game_hub.lfg_post (id) ON DELETE CASCADE
);

CREATE TABLE game_hub.lfg_post_days
(
  lfg_post_id BIGINT      NOT NULL,
  day         VARCHAR(20) NOT NULL,
  PRIMARY KEY (lfg_post_id, day),
  CONSTRAINT fk_lfg_post_day_post FOREIGN KEY (lfg_post_id)
    REFERENCES game_hub.lfg_post (id) ON DELETE CASCADE
);

CREATE TABLE game_hub.lfg_post_times_of_day
(
  lfg_post_id BIGINT      NOT NULL,
  time_of_day VARCHAR(20) NOT NULL,
  PRIMARY KEY (lfg_post_id, time_of_day),
  CONSTRAINT fk_lfg_post_times_of_day_post FOREIGN KEY (lfg_post_id)
    REFERENCES game_hub.lfg_post (id) ON DELETE CASCADE
);

CREATE TABLE game_hub.lfg_post_open_to_regions
(
  lfg_post_id BIGINT      NOT NULL,
  region      VARCHAR(30) NOT NULL,
  PRIMARY KEY (lfg_post_id, region),
  CONSTRAINT fk_lfg_post_open_to_regions_post FOREIGN KEY (lfg_post_id)
    REFERENCES game_hub.lfg_post (id) ON DELETE CASCADE
);

CREATE TABLE game_hub.lfg_post_languages
(
  lfg_post_id BIGINT      NOT NULL,
  language    VARCHAR(10) NOT NULL,
  PRIMARY KEY (lfg_post_id, language),
  CONSTRAINT fk_lfg_post_languages_post FOREIGN KEY (lfg_post_id)
    REFERENCES game_hub.lfg_post (id) ON DELETE CASCADE
);

CREATE TABLE game_hub.lfg_post_game_types
(
  lfg_post_id BIGINT      NOT NULL,
  game_type  VARCHAR(30) NOT NULL,
  PRIMARY KEY (lfg_post_id, game_type),
  CONSTRAINT fk_lfg_post_game_types_post FOREIGN KEY (lfg_post_id)
    REFERENCES game_hub.lfg_post (id) ON DELETE CASCADE
)