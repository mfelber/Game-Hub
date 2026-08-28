ALTER TABLE game_hub.report
  ADD COLUMN IF NOT EXISTS closed_at TIMESTAMP;

ALTER TABLE game_hub.report
  ADD COLUMN IF NOT EXISTS closed_by_report BIGINT NULL;

ALTER TABLE game_hub.report
  ADD CONSTRAINT fk_report_closed_by_report
    FOREIGN KEY (closed_by_report)
      REFERENCES game_hub.report (id)