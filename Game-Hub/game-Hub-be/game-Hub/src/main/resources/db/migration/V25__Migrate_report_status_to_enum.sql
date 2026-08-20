ALTER TABLE game_hub.report DROP CONSTRAINT fk_report_status;

ALTER TABLE game_hub.report DROP COLUMN status_id;

DROP TABLE game_hub.report_status;

ALTER TABLE game_hub.report ADD COLUMN report_status VARCHAR(15);