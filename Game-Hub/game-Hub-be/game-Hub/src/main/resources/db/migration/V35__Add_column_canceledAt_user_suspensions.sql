ALTER TABLE game_hub.user_suspensions
  ADD COLUMN IF NOT EXISTS canceled_at TIMESTAMP;