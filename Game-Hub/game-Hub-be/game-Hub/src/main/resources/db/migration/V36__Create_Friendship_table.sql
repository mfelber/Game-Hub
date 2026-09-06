CREATE TABLE IF NOT EXISTS game_hub.friendship
(
  id         BIGSERIAL PRIMARY KEY,

  user_id    BIGINT    NOT NULL,
  friend_id  BIGINT    NOT NULL,

  created_at TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_friendship_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub.user (id),

  CONSTRAINT fk_friendship_friend
    FOREIGN KEY (friend_id)
      REFERENCES game_hub.user (id),

  CONSTRAINT uk_friendship_user_friend
    UNIQUE (user_id, friend_id),

  CONSTRAINT chk_friendship_not_self
    CHECK (user_id <> friend_id)
);

INSERT INTO game_hub.friendship (
  user_id,
  friend_id,
  created_at
)
SELECT
  user_id,
  friend_id,
  CURRENT_TIMESTAMP
FROM game_hub.user_friends;

DROP TABLE IF EXISTS game_hub.user_friends
