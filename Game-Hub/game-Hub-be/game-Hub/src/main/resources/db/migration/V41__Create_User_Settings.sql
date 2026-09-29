DROP TABLE IF EXISTS game_hub.community_flag_type, game_hub.store_flag_type, game_hub.user_store_flag, game_hub.user_community_flag;

CREATE TABLE IF NOT EXISTS game_hub.pegi_rating
(
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR NOT NULL UNIQUE,
  description VARCHAR NOT NULL
);

INSERT INTO game_hub.pegi_rating (name, description)
VALUES ('PEGI 3', 'Blocks searching for and displaying PEGI 3 games in the store.'),
       ('PEGI 7', 'Blocks searching for and displaying PEGI 7 games in the store.'),
       ('PEGI 12', 'Blocks searching for and displaying PEGI 12 games in the store.'),
       ('PEGI 16', 'Blocks searching for and displaying PEGI 16 games in the store.'),
       ('PEGI 18', 'Blocks searching for and displaying PEGI 18 games in the store.');

CREATE TABLE IF NOT EXISTS game_hub.user_settings
(
  user_id BIGINT PRIMARY KEY,

  CONSTRAINT fk_user_settings_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id)
);

INSERT INTO game_hub.user_settings (user_id)
SELECT u.id
FROM game_hub."user" u
WHERE NOT EXISTS (SELECT 1
                  FROM game_hub.user_settings us
                  WHERE us.user_id = u.id);

CREATE TABLE IF NOT EXISTS game_hub.store_settings
(
  id             BIGSERIAL PRIMARY KEY,
  user_id        BIGINT  NOT NULL,
  pegi_rating_id BIGINT  NOT NULL,
  disabled        BOOLEAN NOT NULL DEFAULT FALSE,

  CONSTRAINT fk_store_settings_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id),

  CONSTRAINT fk_store_settings_pegi_rating
    FOREIGN KEY (pegi_rating_id)
      REFERENCES game_hub.pegi_rating (id),

  CONSTRAINT uq_store_settings_user_pegi
    UNIQUE (user_id, pegi_rating_id)
);

INSERT INTO game_hub.store_settings (user_id, pegi_rating_id, disabled)
SELECT us.user_id, pr.id, FALSE
FROM game_hub.user_settings us
       CROSS JOIN game_hub.pegi_rating pr;

CREATE TABLE IF NOT EXISTS game_hub.community_settings_definition
(
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR NOT NULL UNIQUE,
  description VARCHAR NOT NULL
);

INSERT INTO game_hub.community_settings_definition (name, description)
VALUES ('Friend Requests', 'Who can send you friend requests.'),
       ('Group invites', 'Who can invite you to groups.'),
       ('Group event invites', 'Who can invite you to group events.'),
       ('Play together invites', 'Who can invite you to play together.'),
       ('Send messages', 'Who can send you messages.');

CREATE TABLE IF NOT EXISTS game_hub.privacy_settings_definition
(
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR NOT NULL UNIQUE,
  description VARCHAR NOT NULL
);

INSERT INTO game_hub.privacy_settings_definition(name, description)
VALUES ('Profile visibility', 'Who can view your profile details.'),
       ('Wishlist visibility', 'Who can view your wishlist.'),
       ('Friends list visibility', 'Who can view your friends list.'),
       ('Groups visibility', 'Who can view your groups.'),
       ('Game activity visibility', 'Who can view your recent game activity.'),
       ('Favorite game visibility', 'Who can view your favorite game.');

CREATE TABLE IF NOT EXISTS game_hub.privacy_settings(
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  setting_definition_id BIGINT NOT NULL,
  access VARCHAR NOT NULL,

    CONSTRAINT fk_privacy_settings_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id),

  CONSTRAINT fk_privacy_settings_definition
    FOREIGN KEY (setting_definition_id)
      REFERENCES game_hub.privacy_settings_definition (id),

  CONSTRAINT uq_privacy_settings_user_definition
    UNIQUE (user_id, setting_definition_id)
);

INSERT INTO game_hub.privacy_settings
(user_id, setting_definition_id, access)
SELECT
  u.id,
  psd.id,
  'EVERYONE'
FROM game_hub."user" u
       CROSS JOIN game_hub.privacy_settings_definition psd;

CREATE TABLE IF NOT EXISTS game_hub.community_settings
(
  id                    BIGSERIAL PRIMARY KEY,
  user_id               BIGINT  NOT NULL,
  setting_definition_id BIGINT  NOT NULL,
  access                VARCHAR NOT NULL,

  CONSTRAINT fk_community_settings_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id),

  CONSTRAINT fk_community_settings_definition
    FOREIGN KEY (setting_definition_id)
      REFERENCES game_hub.community_settings_definition (id),

  CONSTRAINT uq_community_settings_user_definition
    UNIQUE (user_id, setting_definition_id)
);

INSERT INTO game_hub.community_settings
  (user_id, setting_definition_id, access)
SELECT us.user_id,
       csd.id,
       CASE
         WHEN csd.name IN ('Send messages', 'Play together') THEN 'FRIENDS'
         ELSE 'EVERYONE'
         END
FROM game_hub.user_settings us
       CROSS JOIN game_hub.community_settings_definition csd;