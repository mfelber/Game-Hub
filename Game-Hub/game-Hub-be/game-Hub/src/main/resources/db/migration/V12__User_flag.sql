CREATE TABLE IF NOT EXISTS game_hub.store_flag_type
(
  id          BIGSERIAL PRIMARY KEY,
  flag_name   VARCHAR(255),
  flag_code   VARCHAR(255),
  description VARCHAR(255)
);

INSERT INTO game_hub.store_flag_type (flag_code, flag_name, description)
VALUES ('PEGI_16', 'PEGI 16', 'Hides games rated PEGI 16 in store'),
       ('PEGI_18', 'PEGI 18', 'Hides games rated PEGI 18 in store');

CREATE TABLE IF NOT EXISTS game_hub.community_flag_type
(
  id          BIGSERIAL PRIMARY KEY,
  flag_name   VARCHAR(255),
  flag_code   VARCHAR(255),
  description VARCHAR(255)
);

INSERT INTO game_hub.community_flag_type (flag_code, flag_name, description)
VALUES ('FRIEND_REQUEST', 'Friend Request', 'Who can send you friend request'),
       ('GROUP_INVITES', 'Group Invites', 'Who can invite you to groups'),
       ('PLAY_TOGETHER_INVITES', 'Play Together Invites', 'Who can invite you to game session'),
       ('PROFILE_VISIBILITY', 'Profile Visibility', 'Who can see informations about your profile'),
       ('SEND_MESSAGES', 'Send Messages', 'Who can see informations about your profile');

CREATE TABLE IF NOT EXISTS game_hub.user_store_flag
(
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT  NOT NULL,
  flag_type_id BIGINT  NOT NULL,
  value        BOOLEAN NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  CONSTRAINT fk_flag_type FOREIGN KEY (flag_type_id) REFERENCES game_hub.store_flag_type (id)
);

CREATE TABLE IF NOT EXISTS game_hub.user_community_flag
(
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT  NOT NULL,
  flag_type_id BIGINT  NOT NULL,
  value        VARCHAR NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES game_hub."user" (id),
  CONSTRAINT fk_flag_type FOREIGN KEY (flag_type_id) REFERENCES game_hub.community_flag_type (id)
);

