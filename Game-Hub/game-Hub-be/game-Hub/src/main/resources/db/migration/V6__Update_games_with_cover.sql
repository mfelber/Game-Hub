UPDATE game_hub.game
SET game_cover_image = CASE id
    WHEN 1 THEN './uploads\games\1\witcher3.jpg'
    WHEN 2 THEN './uploads\games\2\leftfordead2.jpg'
    WHEN 3 THEN './uploads\games\3\subnautica.jpg'
    WHEN 4 THEN './uploads\games\4\hollowknight.jpg'
    WHEN 5 THEN './uploads\games\5\cyberpunk.jpg'
    WHEN 6 THEN './uploads\games\6\stardewvalley.jpg'
    WHEN 7 THEN './uploads\games\7\Phasmophobia.jpg'
    WHEN 8 THEN './uploads\games\8\nomansky.jpg'
    WHEN 9 THEN './uploads\games\9\dbd.jpg'
    WHEN 10 THEN './uploads\games\10\darksouls3.jpg'
END
WHERE id IN (1,2,3,4,5,6,7,8,9,10)