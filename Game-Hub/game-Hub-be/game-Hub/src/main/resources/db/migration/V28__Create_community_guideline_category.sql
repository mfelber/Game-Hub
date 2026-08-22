ALTER TABLE game_hub.community_guidelines
  ADD COLUMN category_id BIGINT;


CREATE TABLE IF NOT EXISTS game_hub.community_guideline_category
(
  id            BIGSERIAL PRIMARY KEY,
  category_name VARCHAR(50)
);

INSERT INTO game_hub.community_guideline_category (category_name)
VALUES ('Abuse & Harassment'),
       ('Inappropriate Content'),
       ('Spam & Scams'),
       ('Privacy & Identity'),
       ('Rules & Fair Play'),
       ('Other');


ALTER TABLE game_hub.community_guidelines
  ADD CONSTRAINT fk_community_guideline_category
    FOREIGN KEY (category_id)
      REFERENCES game_hub.community_guideline_category (id);

UPDATE game_hub.community_guidelines
SET category_id = 1
WHERE id IN (1,2,3,4);

UPDATE game_hub.community_guidelines
SET category_id = 2
WHERE id IN (5,6);

UPDATE game_hub.community_guidelines
SET category_id = 3
WHERE id IN (7,8,9,10);

UPDATE game_hub.community_guidelines
SET category_id = 4
WHERE id IN (11,12);

UPDATE game_hub.community_guidelines
SET category_id = 5
WHERE id IN (13,14);

UPDATE game_hub.community_guidelines
SET category_id = 6
WHERE id IN (15);


