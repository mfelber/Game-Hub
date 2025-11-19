CREATE TABLE IF NOT EXISTS game_hub.report_status
(
  id     BIGSERIAL PRIMARY KEY,
  status VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS game_hub.report_reason
(
  id          BIGSERIAL PRIMARY KEY,
  reason      VARCHAR(2000),
  description VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS game_hub.report
(
  id               BIGSERIAL PRIMARY KEY,
  reporter_id      BIGINT NOT NULL,
  reported_user_id BIGINT NOT NULL,
  reason_id        BIGINT NOT NULL,
  message          VARCHAR(2000),
  status_id        BIGINT NOT NULL,
  created_at       TIMESTAMP DEFAULT now(),
  CONSTRAINT fk_reporter
    FOREIGN KEY (reporter_id)
      REFERENCES game_hub."user" (id)
      ON DELETE CASCADE,
  CONSTRAINT fk_reported_user
    FOREIGN KEY (reported_user_id)
      REFERENCES game_hub."user" (id)
      ON DELETE CASCADE,
  CONSTRAINT fk_reason_id
    FOREIGN KEY (reason_id)
      REFERENCES game_hub.report_reason (id)
      ON DELETE CASCADE,
  CONSTRAINT fk_report_status
    FOREIGN KEY (status_id)
      REFERENCES game_hub.report_status (id)
      ON DELETE CASCADE
);

