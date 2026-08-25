ALTER TABLE game_hub.ban_history
  ADD COLUMN IF NOT EXISTS report_id BIGINT;

ALTER TABLE game_hub.ban_history
  ADD CONSTRAINT fk_report_ban_history
    FOREIGN KEY (report_id)
      REFERENCES game_hub.report (id);