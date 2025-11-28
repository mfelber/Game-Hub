ALTER TABLE game_hub."user"
ADD COLUMN account_type VARCHAR(20);

ALTER TABLE game_hub."user"
ADD COLUMN parent_email VARCHAR(255);