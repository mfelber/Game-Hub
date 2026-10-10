ALTER TABLE game_hub."user"
  ADD COLUMN main_platform     VARCHAR,
  ADD COLUMN user_is_onboarded BOOLEAN DEFAULT FALSE,
  ADD COLUMN microphone_usage  VARCHAR;


UPDATE game_hub."user"
SET microphone_usage  = 'YES',
    main_platform     = 'PC',
    user_is_onboarded = TRUE;
