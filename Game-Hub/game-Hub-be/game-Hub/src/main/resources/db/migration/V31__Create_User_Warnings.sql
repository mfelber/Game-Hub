CREATE TABLE IF NOT EXISTS game_hub.user_warnings(
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  report_id BIGINT NOT NULL,
  admin_msg TEXT,
  created_at TIMESTAMP DEFAULT now(),

  CONSTRAINT fk_user_user_warnings
    FOREIGN KEY (user_id)
    REFERENCES game_hub."user"(id),

  CONSTRAINT fk_report_user_warnings
    FOREIGN KEY (report_id)
    REFERENCES game_hub.report(id)
)