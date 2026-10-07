CREATE TABLE IF NOT EXISTS game_hub.news
(
  news_id    BIGSERIAL PRIMARY KEY,
  version    VARCHAR   NOT NULL,
  title      VARCHAR   NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS game_hub.news_section
(
  news_section_id BIGSERIAL PRIMARY KEY,
  news_type       VARCHAR NOT NULL,
  news_id         BIGINT  NOT NULL,

  CONSTRAINT fk_news_section_news
    FOREIGN KEY (news_id)
    REFERENCES game_hub.news (news_id)
);

CREATE TABLE IF NOT EXISTS game_hub.news_item
(
  news_item_id    BIGSERIAL PRIMARY KEY,
  description     VARCHAR NOT NULL,
  news_section_id BIGINT  NOT NULL,

  CONSTRAINT fk_news_item_news_section
    FOREIGN KEY (news_section_id)
    REFERENCES game_hub.news_section(news_section_id)
);