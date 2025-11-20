ALTER TABLE game_hub.badge
  ADD COLUMN IF NOT EXISTS xp_reward BIGINT;

-- update badge table for each badge set xp_reward
UPDATE game_hub.badge
SET xp_reward = CASE id
                  WHEN 1 THEN 25
                  WHEN 2 THEN 75
                  WHEN 3 THEN 150
                  WHEN 4 THEN 300
                  WHEN 5 THEN 600
  END
WHERE id IN (1, 2, 3, 4, 5);

CREATE TABLE game_hub.level
(
  id          BIGSERIAL PRIMARY KEY,
  levelNumber       BIGINT,
  xp_required BIGINT NOT NULL
);


INSERT INTO game_hub.level (levelNumber, xp_required)
VALUES (0, 0),
       (1, 50),
       (2, 150),
       (3, 300),
       (4, 500),
       (5, 750),
       (6, 1050),
       (7, 1450),
       (8, 1950),
       (9, 2550),
       (10, 3250),
       (11, 4050),
       (12, 4950),
       (13, 5950),
       (14, 7050),
       (15, 8250),
       (16, 9550),
       (17, 10950),
       (18, 12450),
       (19, 14050),
       (20, 15750),
       (21, 17550),
       (22, 19450),
       (23, 21450),
       (24, 23550),
       (25, 25750),
       (26, 28050),
       (27, 30450),
       (28, 32950),
       (29, 35550),
       (30, 38250),
       (31, 41050),
       (32, 43950),
       (33, 46950),
       (34, 50050),
       (35, 53250),
       (36, 56550),
       (37, 59950),
       (38, 63450),
       (39, 67050),
       (40, 70750),
       (41, 74550),
       (42, 78450),
       (43, 82450),
       (44, 86550),
       (45, 90750),
       (46, 95050),
       (47, 99450),
       (48, 103950),
       (49, 108550),
       (50, 113250);


ALTER TABLE game_hub."user"
  ADD xp BIGINT,
  ADD level_id BIGINT,
  ADD CONSTRAINT fk_user_level
    FOREIGN KEY (level_id) references game_hub.level(id);

ALTER TABLE game_hub."user"
  ALTER COLUMN level_id SET DEFAULT 1;

ALTER TABLE game_hub."user"
  ALTER COLUMN xp SET DEFAULT 0;

ALTER TABLE game_hub.user_badges
  ADD unlocked TIMESTAMP DEFAULT now();