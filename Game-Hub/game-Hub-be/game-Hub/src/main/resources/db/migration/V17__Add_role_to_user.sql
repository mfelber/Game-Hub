DROP TABLE game_hub.user_roles;

ALTER TABLE game_hub."user"
  ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'USER';