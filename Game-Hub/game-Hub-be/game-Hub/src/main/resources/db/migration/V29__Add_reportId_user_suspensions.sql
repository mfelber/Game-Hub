ALTER TABLE game_hub.user_suspensions
  ADD COLUMN report_id BIGINT;

ALTER TABLE game_hub.user_suspensions
  ADD CONSTRAINT fk_user_suspensions
    FOREIGN KEY (report_id)
      REFERENCES game_hub.report (id);