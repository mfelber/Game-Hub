CREATE TABLE IF NOT EXISTS game_hub.user_cart
(
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT    NOT NULL UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_user_cart_user
    FOREIGN KEY (user_id)
      REFERENCES game_hub."user" (id)
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS game_hub.cart_item
(
  id       BIGSERIAL PRIMARY KEY,
  cart_id  BIGINT    NOT NULL,
  game_id  BIGINT    NOT NULL,
  added_at TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_cart_item_cart
    FOREIGN KEY (cart_id)
      REFERENCES game_hub.user_cart (id)
      ON DELETE CASCADE,

  CONSTRAINT fk_cart_item_game
    FOREIGN KEY (game_id)
      REFERENCES game_hub.game (id)
      ON DELETE CASCADE,

  CONSTRAINT unique_cart_game
    UNIQUE (cart_id, game_id)
);