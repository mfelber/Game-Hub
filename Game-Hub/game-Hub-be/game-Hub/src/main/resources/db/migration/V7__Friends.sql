CREATE TABLE game_hub.user_friends(
  user_id BIGINT NOT NULL,
  friend_id BIGINT NOT NULL,
  PRIMARY KEY (user_id,friend_id),
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
    REFERENCES game_hub."user"(id)
    ON DELETE CASCADE ,
  CONSTRAINT fk_friend
    FOREIGN KEY (friend_id)
      REFERENCES game_hub."user"(id)
      ON DELETE CASCADE
);

CREATE TABLE game_hub.friend_request(
  id BIGSERIAL PRIMARY KEY,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  CONSTRAINT fk_sender
    FOREIGN KEY (sender_id)
      REFERENCES game_hub."user"(id)
      ON DELETE CASCADE,
  CONSTRAINT fk_receiver
    FOREIGN KEY (receiver_id)
      REFERENCES game_hub."user"(id)
      ON DELETE CASCADE,
  CONSTRAINT unique_request UNIQUE(sender_id,receiver_id)
)