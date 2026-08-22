CREATE TABLE IF NOT EXISTS game_hub.user_suspensions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  reason_id BIGINT NOT NULL,
  custom_msg TEXT,
  created_at TIMESTAMP DEFAULT now(),
  expires_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_user_suspensions_user
    FOREIGN KEY (user_id)
    REFERENCES game_hub."user" (id),

  CONSTRAINT fk_user_suspensions_reason
    FOREIGN KEY (reason_id)
    REFERENCES game_hub.community_guidelines (id)

)