package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.LfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType implements LfgOption {

  SHOOTER("Shooter", "First-person and third-person shooters."),
  CO_OP("Co-op", "Playing together against the game."),
  RPG("RPG", "Characters, levelling and gear progression."),
  SURVIVAL("Survival", "Gathering resources and staying alive."),
  SANDBOX("Sandbox", "Open-ended games where you build and create freely."),
  MOBA("MOBA", "Team-based arena battles like League of Legends or Dota 2."),
  COZY("Cozy", "Relaxing games with no pressure, like farming or decorating."),
  STRATEGY("Strategy", "Real-time or turn-based strategy: building, managing and outsmarting."),
  HORROR("Horror", "Scary games best played with friends."),
  RACING("Racing", "Cars, bikes and everything that goes fast."),
  SPORTS("Sports", "Football, basketball and other sports games."),
  SIMULATION("Simulation", "Realistic or management simulations."),
  ROGUELIKE("Roguelike", "Runs that start over after you die."),
  STORY_DRIVEN("Story-driven", "Games focused on narrative and characters."),
  TACTICAL("Tactical", "Planning moves and positions, often turn-based."),
  MMO_RPG("MMORPG", "Massive online worlds with lots of players."),
  FIGHTING("Fighting", "One-on-one fighting games."),
  PARTY("Party", "Light, quick games for groups."),
  PUZZLE("Puzzle", "Brain teasers and logic games."),
  PLATFORMER("Platformer", "Jumping and running through levels.");

  private final String label;
  private final String description;
}
