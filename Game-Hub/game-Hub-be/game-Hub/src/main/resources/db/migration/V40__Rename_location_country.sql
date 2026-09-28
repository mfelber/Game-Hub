ALTER TABLE game_hub.user
  RENAME COLUMN location TO country;

UPDATE game_hub.user SET country = 'NOT_SPECIFIED' WHERE country = 'UNKNOWN';