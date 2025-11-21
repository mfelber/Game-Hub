CREATE TABLE IF NOT EXISTS game_hub.age_rating
(
  id               BIGSERIAL PRIMARY KEY,
  age_rating       VARCHAR(255),
  age_rating_color VARCHAR(255)
);

ALTER TABLE game_hub.game
  ADD COLUMN age_rating BIGINT;

ALTER TABLE game_hub.game
  ADD CONSTRAINT fk_age_rating
    FOREIGN KEY (age_rating) REFERENCES game_hub.age_rating (id);

INSERT INTO game_hub.age_rating (age_rating, age_rating_color)
VALUES ('PEGI 3', '#A4C500CC'),
       ('PEGI 7', '#A4C500CC'),
       ('PEGI 12', '#F69F00CC'),
       ('PEGI 16', '#F69F00CC'),
       ('PEGI 18', '#E20217CC');



UPDATE game_hub.game
SET age_rating = CASE id
                   WHEN 1 THEN 4
                   WHEN 2 THEN 3
                   WHEN 3 THEN 2
                   WHEN 4 THEN 3
                   WHEN 5 THEN 4
                   WHEN 6 THEN 1
                   WHEN 7 THEN 3
                   WHEN 8 THEN 3
                   WHEN 9 THEN 4
                   WHEN 10 THEN 5
  END
WHERE game.id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
