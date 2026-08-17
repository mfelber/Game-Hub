ALTER TABLE game_hub.system_requirements
  DROP COLUMN os;

ALTER TABLE game_hub.system_requirements
  DROP COLUMN ram;

ALTER TABLE game_hub.system_requirements
  DROP COLUMN storage;

ALTER TABLE game_hub.system_requirements
  ADD COLUMN ram INTEGER;

ALTER TABLE game_hub.system_requirements
  ADD COLUMN storage INTEGER;

ALTER TABLE game_hub.system_requirements
  ADD COLUMN game_unit_size VARCHAR(10);

UPDATE game_hub.system_requirements
SET ram     = 8,
    storage = 50,
    game_unit_size = 'GB'
WHERE system_requirements.id = 1;

UPDATE game_hub.system_requirements
SET ram     = 4,
    storage = 13,
    game_unit_size = 'GB'
WHERE system_requirements.id = 2;

UPDATE game_hub.system_requirements
SET ram     = 8,
    storage = 20,
    game_unit_size = 'GB'
WHERE system_requirements.id = 3;

UPDATE game_hub.system_requirements
SET ram     = 8,
    storage = 9,
    game_unit_size = 'GB'
WHERE system_requirements.id = 4;

UPDATE game_hub.system_requirements
SET ram     = 16,
    storage = 70,
    game_unit_size = 'GB'
WHERE system_requirements.id = 5;

UPDATE game_hub.system_requirements
SET ram     = 2,
    storage = 500,
    game_unit_size = 'MB'
WHERE system_requirements.id = 6;

UPDATE game_hub.system_requirements
SET ram     = 8,
    storage = 18,
    game_unit_size = 'GB'
WHERE system_requirements.id = 7;

UPDATE game_hub.system_requirements
SET ram     = 8,
    storage = 10,
    game_unit_size = 'GB'
WHERE system_requirements.id = 8;

UPDATE game_hub.system_requirements
SET ram     = 8,
    storage = 25,
    game_unit_size = 'GB'
WHERE system_requirements.id = 9;

UPDATE game_hub.system_requirements
SET ram     = 8,
    storage = 25,
    game_unit_size = 'GB'
WHERE system_requirements.id = 10;