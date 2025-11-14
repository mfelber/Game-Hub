CREATE TABLE IF NOT EXISTS game_hub.card_color
(
  id         BIGSERIAL PRIMARY KEY,
  color_name VARCHAR(255),
  color_code VARCHAR(255)
);

INSERT INTO game_hub.card_color (color_name, color_code)
VALUES ('Default', '#0000004D'),
       ('Midnight Blue', '#0F172AB3'),
       ('Deep Teal', '#164E6399'),
       ('Royal Purple', '#312E8199'),
       ('Slate Gray', '#1E293B99'),
       ('Emerald Dark', '#064E3B99'),
       ('Crimson Glow', '#7F1D1D99');

ALTER TABLE game_hub.user
  ADD COLUMN card_color_id BIGINT;

ALTER TABLE game_hub.user
  ADD CONSTRAINT fk_user_card_color
    FOREIGN KEY (card_color_id) REFERENCES game_hub.card_color (id);
