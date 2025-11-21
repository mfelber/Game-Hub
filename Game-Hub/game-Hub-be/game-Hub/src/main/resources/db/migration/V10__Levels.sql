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
  xp_required BIGINT NOT NULL,
  level_color VARCHAR(255)
);

INSERT INTO game_hub.level (levelNumber, xp_required, level_color)
VALUES
  (0, 0, '#B0B0B0'),
  (1, 50, '#B0B0B0'),
  (2, 150, '#B0B0B0'),
  (3, 300, '#B0B0B0'),
  (4, 500, '#B0B0B0'),
  (5, 750, '#B0B0B0'),
  (6, 1050, '#B0B0B0'),
  (7, 1450, '#B0B0B0'),
  (8, 1950, '#B0B0B0'),
  (9, 2550, '#B0B0B0'),
  (10, 3250, '#B0B0B0'),
  (11, 4050, '#4CAF50'),
  (12, 4950, '#4CAF50'),
  (13, 5950, '#4CAF50'),
  (14, 7050, '#4CAF50'),
  (15, 8250, '#4CAF50'),
  (16, 9550, '#4CAF50'),
  (17, 10950, '#4CAF50'),
  (18, 12450, '#4CAF50'),
  (19, 14050, '#4CAF50'),
  (20, 15750, '#4CAF50'),
  (21, 17550, '#2196F3'),
  (22, 19450, '#2196F3'),
  (23, 21450, '#2196F3'),
  (24, 23550, '#2196F3'),
  (25, 25750, '#2196F3'),
  (26, 28050, '#2196F3'),
  (27, 30450, '#2196F3'),
  (28, 32950, '#2196F3'),
  (29, 35550, '#2196F3'),
  (30, 38250, '#2196F3'),
  (31, 41050, '#9C27B0'),
  (32, 43950, '#9C27B0'),
  (33, 46950, '#9C27B0'),
  (34, 50050, '#9C27B0'),
  (35, 53250, '#9C27B0'),
  (36, 56550, '#9C27B0'),
  (37, 59950, '#9C27B0'),
  (38, 63450, '#9C27B0'),
  (39, 67050, '#9C27B0'),
  (40, 70750, '#9C27B0'),
  (41, 74550, '#FFD700'),
  (42, 78450, '#FFD700'),
  (43, 82450, '#FFD700'),
  (44, 86550, '#FFD700'),
  (45, 90750, '#FFD700'),
  (46, 95050, '#FFD700'),
  (47, 99450, '#FFD700'),
  (48, 103950, '#FFD700'),
  (49, 108550, '#FFD700'),
  (50, 113250, '#FFD700');


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