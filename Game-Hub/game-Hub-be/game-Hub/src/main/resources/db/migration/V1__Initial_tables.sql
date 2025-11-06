CREATE SCHEMA IF NOT EXISTS game_hub;

CREATE TABLE IF NOT EXISTS game_hub.user
(
  id                   BIGSERIAL PRIMARY KEY,
  first_name           VARCHAR(255),
  last_name            VARCHAR(255),
  username             VARCHAR(255),
  password             VARCHAR(255),
  email                VARCHAR(255) UNIQUE NOT NULL,
  user_profile_picture VARCHAR(255),
  banner               VARCHAR(255),
  is_banned            BOOLEAN                      DEFAULT FALSE,
  created_at           TIMESTAMP           NOT NULL DEFAULT NOW(),
  last_modified_at     TIMESTAMP,
  location             VARCHAR(50),
  status               VARCHAR(50),
  bio                  VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS game_hub.token
(
  id           BIGSERIAL PRIMARY KEY,
  token        VARCHAR(255) NOT NULL,
  created_at   TIMESTAMP,
  expires_at   TIMESTAMP,
  validated_at TIMESTAMP,
  user_id      BIGINT       NOT NULL,
  CONSTRAINT fk_token_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id)
      ON DELETE CASCADE
);

CREATE TABLE game_hub.role
(
  id               BIGSERIAL PRIMARY KEY,
  name             VARCHAR(255) UNIQUE NOT NULL,
  created_at       TIMESTAMP           NOT NULL DEFAULT now(),
  last_modified_at TIMESTAMP
);

CREATE TABLE game_hub.system_requirements
(
  id      BIGSERIAL PRIMARY KEY,
  cpu     VARCHAR(255),
  gpu     VARCHAR(255),
  ram     VARCHAR(255),
  storage VARCHAR(255),
  os      VARCHAR(255)
);

CREATE TABLE game_hub.game
(
  id               BIGSERIAL PRIMARY KEY,
  title            VARCHAR(255),
  description      TEXT,
  system_req_id    BIGINT,
  publisher        VARCHAR(255),
  developer        VARCHAR(255),
  release_year     VARCHAR(50),
  price            DOUBLE PRECISION,
  dlc              BOOLEAN DEFAULT false,
  game_cover_image VARCHAR(255),
  FOREIGN KEY (system_req_id) REFERENCES game_hub.system_requirements (id)
);

CREATE TABLE game_hub.genre
(
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE game_hub.language
(
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255)
);

CREATE TABLE game_hub.platform
(
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255)
);

CREATE TABLE game_hub.subtitles
(
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255)
);


CREATE TABLE game_hub.badge_category
(
  id                 BIGSERIAL PRIMARY KEY,
  category_name      VARCHAR(255) NOT NULL,
  category_name_code VARCHAR(255) NOT NULL
);

CREATE TABLE game_hub.badge
(
  id                BIGSERIAL PRIMARY KEY,
  name              VARCHAR(255) NOT NULL,
  description       TEXT,
  icon_path         VARCHAR(255),
  badge_category_id BIGINT,
  CONSTRAINT fk_badge_category
    FOREIGN KEY (badge_category_id)
      REFERENCES game_hub.badge_category (id)
      ON DELETE SET NULL
);

CREATE TABLE game_hub.password_reset_token
(
  id                   BIGSERIAL PRIMARY KEY,
  token                VARCHAR(255) NOT NULL,
  expiration_date_time TIMESTAMP    NOT NULL,
  user_id              BIGINT       NOT NULL,
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id)
      ON DELETE CASCADE
);

CREATE TABLE game_hub.dlc
(
  id           BIGSERIAL PRIMARY KEY,
  title        VARCHAR(255) NOT NULL,
  description  TEXT,
  base_game_id BIGINT,
  CONSTRAINT fk_base_game
    FOREIGN KEY (base_game_id)
      REFERENCES game_hub.game (id)
      ON DELETE SET NULL
);


-- join tables
CREATE TABLE game_hub.game_genre
(
  game_id  BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  PRIMARY KEY (game_id, genre_id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id),
  FOREIGN KEY (genre_id) REFERENCES game_hub.genre (id)
);

-- Join table game_platform
CREATE TABLE game_hub.game_platform
(
  game_id     BIGINT NOT NULL,
  platform_id BIGINT NOT NULL,
  PRIMARY KEY (game_id, platform_id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id),
  FOREIGN KEY (platform_id) REFERENCES game_hub.platform (id)
);

-- Join table game_subtitles
CREATE TABLE game_hub.game_subtitles
(
  game_id      BIGINT NOT NULL,
  subtitles_id BIGINT NOT NULL,
  PRIMARY KEY (game_id, subtitles_id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id),
  FOREIGN KEY (subtitles_id) REFERENCES game_hub.subtitles (id)
);

-- Join table game_language
CREATE TABLE game_hub.game_language
(
  game_id     BIGINT NOT NULL,
  language_id BIGINT NOT NULL,
  PRIMARY KEY (game_id, language_id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id),
  FOREIGN KEY (language_id) REFERENCES game_hub.language (id)
);


-- Join table: user_roles
CREATE TABLE game_hub.user_roles
(
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  FOREIGN KEY (role_id) REFERENCES game_hub.role (id)
);

-- Join table: user_library
CREATE TABLE game_hub.user_library
(
  user_id BIGINT NOT NULL,
  game_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, game_id),
  FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id)
);

-- Join table: user_wishlist
CREATE TABLE game_hub.user_wishlist
(
  user_id BIGINT NOT NULL,
  game_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, game_id),
  FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id)
);

-- Join table: user_play_recently
CREATE TABLE game_hub.user_play_recently
(
  user_id BIGINT NOT NULL,
  game_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, game_id),
  FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id)
);

-- Join table: user_favorite_game
CREATE TABLE game_hub.user_favorite_game
(
  user_id BIGINT NOT NULL,
  game_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, game_id),
  FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id)
);

-- Join table: user_favorite_genre
CREATE TABLE game_hub.user_favorite_genre
(
  user_id  BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, genre_id),
  FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  FOREIGN KEY (genre_id) REFERENCES game_hub.genre (id)
);

-- Join table: user_recommendation_game
CREATE TABLE game_hub.user_recommendation_game
(
  user_id BIGINT NOT NULL,
  game_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, game_id),
  FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  FOREIGN KEY (game_id) REFERENCES game_hub.game (id)
);

CREATE TABLE game_hub.user_badges
(
  user_id  BIGINT NOT NULL,
  badge_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, badge_id),
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id)
      ON DELETE CASCADE,
  CONSTRAINT fk_badge
    FOREIGN KEY (badge_id)
      REFERENCES game_hub.badge (id)
      ON DELETE CASCADE
);
