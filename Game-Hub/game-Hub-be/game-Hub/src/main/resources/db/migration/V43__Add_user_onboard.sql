ALTER TABLE game_hub."user"
  ADD COLUMN region            VARCHAR,
  ADD COLUMN main_platform     VARCHAR,
  ADD COLUMN user_is_onboarded BOOLEAN DEFAULT FALSE,
  ADD COLUMN microphone_usage  VARCHAR;
