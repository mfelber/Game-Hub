INSERT INTO game_hub.badge_category (category_name, category_name_code)
VALUES ('Game Collector', 'game_collector');

INSERT INTO game_hub.badge (name, description, icon_path, badge_category_id)
VALUES ('GAME_COLLECTOR_LEVEL_1', 'Earned by owning 5 games', '/assets/badges/game_collector/bronze.svg', 1),
       ('GAME_COLLECTOR_LEVEL_2', 'Earned by owning more than 15 games', '/assets/badges/game_collector/silver.svg', 1),
       ('GAME_COLLECTOR_LEVEL_3', 'Earned by owning more than 30 games', '/assets/badges/game_collector/gold.svg', 1),
       ('GAME_COLLECTOR_LEVEL_4', 'Earned by owning more than 50 games', '/assets/badges/game_collector/elite.svg', 1),
       ('GAME_COLLECTOR_LEVEL_5', 'Earned by owning more than 100 games', '/assets/badges/game_collector/emerald.svg',
        1);


INSERT INTO game_hub.genre (name)
VALUES ('Horror'),
       ('RPG'),
       ('Co-op'),
       ('Survival'),
       ('Drama'),
       ('FPS'),
       ('Adventure');


INSERT INTO game_hub.language (name)
VALUES ('English'),
       ('Slovak'),
       ('Czech'),
       ('German'),
       ('French'),
       ('Spanish'),
       ('Japanese'),
       ('Korean'),
       ('Chinese (Simplified)'),
       ('Russian');

INSERT INTO game_hub.platform (name)
VALUES ('Windows'),
       ('macOS'),
       ('Linux');

INSERT INTO game_hub.subtitles (name)
VALUES ('English'),
       ('Slovak'),
       ('Czech'),
       ('German'),
       ('French'),
       ('Spanish'),
       ('Japanese'),
       ('Korean'),
       ('Chinese (Simplified)'),
       ('Russian');


INSERT INTO game_hub.system_requirements (os, cpu, ram, gpu, storage)
VALUES ('Windows 10', 'AMD FX-6300', '8 GB RAM', 'NVIDIA GTX 970', '50 GB'),
       ('Windows 7', 'AMD Phenom X3 8750', '4 GB RAM', 'ATI 2600',
        '13 GB'),
       ('Windows 10', 'AMD Ryzen 3', '8 GB RAM', 'NVIDIA GTX 1050 Ti', '20 GB'),
       ('Windows 10', 'Intel Core i5', '8 GB RAM', 'Intel HD 4000', '9 GB'),
       ('Windows 10', 'Intel i7-6700', '16 GB RAM', 'RX 5700 XT', '70 GB'),
       ('Windows 10', 'Intel Core 2 Duo 2.0GHz', '2 GB RAM', 'Intel HD 3000', '500 MB'),
       ('Windows 10', 'Intel i5-4590', '8 GB RAM', 'NVIDIA GTX 970', '18 GB'),
       ('Windows 10', 'Intel i5-8400', '8 GB RAM', 'GTX 1060', '10 GB'),
       ('Windows', 'Intel Core i3-4170', '8 GB RAM', 'GTX 760 or higher', '25 GB'),
       ('Windows 10', 'Intel Core i7-3770', '8 GB RAM', 'GTX 970', '25 GB');


INSERT INTO game_hub.game (title, description, price, release_year, game_cover_image, publisher, developer,
                           system_req_id)
VALUES ('The Witcher 3: Wild Hunt',
        'An epic open-world RPG following Geralt of Rivia on his monster-hunting adventures.', 39.99, '2015-05-19',
        NULL, 'CD Projekt', 'CD Projekt RED', 1),
       ('Left 4 Dead 2', 'Co-op zombie shooter featuring fast-paced survival gameplay and multiple campaigns.', 19.99,
        '2009-11-17', NULL, 'Valve', 'Valve', 2),
       ('Subnautica', 'Underwater survival adventure where players explore an alien ocean world.', 29.99, '2018-01-23',
        NULL, 'Unknown Worlds', 'Unknown Worlds', 3),
       ('Hollow Knight', 'Hand-drawn action adventure through a ruined insect kingdom.', 14.99, '2017-02-24', NULL,
        'Team Cherry', 'Team Cherry', 4),
       ('Cyberpunk 2077', 'Futuristic open-world RPG set in Night City.', 59.99, '2020-12-10', NULL, 'CD Projekt',
        'CD Projekt RED', 5),
       ('Stardew Valley', 'Farming and life simulation game where you restore and manage your grandfather’s farm.',
        14.99, '2016-02-26', NULL, 'ConcernedApe', 'ConcernedApe', 6),
       ('Phasmophobia', 'Co-op psychological horror game where players hunt ghosts using investigation tools.', 13.99,
        '2020-09-18', NULL, 'Kinetic Games', 'Kinetic Games', 7),
       ('No Man’s Sky', 'Procedurally generated universe full of planets to explore and survive.', 49.99, '2016-08-12',
        NULL, 'Hello Games', 'Hello Games', 8),
       ('Dead by Daylight',
        'Multiplayer horror game where one player takes the role of a killer and others try to survive.', 19.99,
        '2016-06-14', NULL, 'Behaviour Interactive', 'Behaviour Interactive', 9),
       ('Dark Souls III', 'Challenging action RPG set in a dark fantasy world full of bosses and secrets.', 59.99,
        '2016-04-12', NULL, 'Bandai Namco', 'FromSoftware', 10);

INSERT INTO game_hub.game_genre (game_id, genre_id)
VALUES (1, 2),
       (1, 5),
       (2, 3),
       (2, 4),
       (3, 4),
       (3, 2),
       (4, 6),
       (4, 2),
       (5, 2),
       (5, 5),
       (5, 6),
       (6, 6),
       (6, 5),
       (7, 1),
       (7, 4),
       (8, 6),
       (8, 4),
       (9, 1),
       (9, 3),
       (10, 2),
       (10, 6),
       (10, 1);

INSERT INTO game_hub.game_language (game_id, language_id)
VALUES (1, 1),
       (1, 5),
       (1, 7),
       (2, 1),
       (2, 3),
       (3, 1),
       (3, 2),
       (4, 1),
       (4, 6),
       (5, 1),
       (5, 2),
       (5, 7),
       (6, 1),
       (6, 4),
       (7, 1),
       (7, 2),
       (8, 1),
       (8, 2),
       (8, 7),
       (9, 1),
       (9, 2),
       (9, 4),
       (10, 1),
       (10, 2),
       (10, 5);

INSERT INTO game_hub.game_platform (game_id, platform_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 3),
       (3, 1),
       (3, 2),
       (4, 1),
       (4, 2),
       (4, 3),
       (5, 1),
       (5, 2),
       (6, 1),
       (6, 2),
       (6, 3),
       (7, 1),
       (8, 1),
       (8, 2),
       (9, 1),
       (9, 3),
       (10, 1),
       (10, 2);

INSERT INTO game_hub.game_subtitles (game_id, subtitles_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 1),
       (3, 2),
       (4, 1),
       (4, 3),
       (5, 1),
       (5, 2),
       (5, 3),
       (6, 1),
       (6, 3),
       (7, 1),
       (7, 2),
       (8, 1),
       (8, 2),
       (8, 3),
       (9, 1),
       (9, 2),
       (10, 1),
       (10, 3);