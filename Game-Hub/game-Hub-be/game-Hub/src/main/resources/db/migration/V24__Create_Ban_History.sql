CREATE TABLE IF NOT EXISTS game_hub.ban_history (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT  NOT NULL,
  reason_id BIGINT  NOT NULL,
  custom_msg TEXT,
  banned_at TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_ban_history_user
    FOREIGN KEY (user_id)
    REFERENCES game_hub.user (id),

  CONSTRAINT fk_ban_history_reason
  FOREIGN KEY (reason_id)
  REFERENCES game_hub.report_reason (id)
)
