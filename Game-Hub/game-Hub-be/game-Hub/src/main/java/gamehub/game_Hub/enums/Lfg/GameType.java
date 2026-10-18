package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.LfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType implements LfgOption {

  SHOOTER("shooter", "First-person and third-person shooters."),
  CO_OP("co-op", "Playing together against the game."),
  RPG("rpg", "Characters, levelling and gear progression."),
  SURVIVAL("survival", "Gathering resources and staying alive."),
  SANDBOX("sandbox", "Open-ended games where you build and create freely."),
  MOBA("moba", "Team-based arena battles like League of Legends or Dota 2."),
  COZY("cozy", "Relaxing games with no pressure, like farming or decorating."),
  STRATEGY("strategy", "Real-time or turn-based strategy: building, managing and outsmarting."),
  HORROR("horror", "Scary games best played with friends."),
  RACING("racing", "Cars, bikes and everything that goes fast."),
  SPORTS("sports", "Football, basketball and other sports games."),
  SIMULATION("simulation", "Realistic or management simulations."),
  ROGUELIKE("roguelike", "Runs that start over after you die."),
  STORY_DRIVEN("story-driven", "Games focused on narrative and characters."),
  TACTICAL("tactical", "Planning moves and positions, often turn-based."),
  MMO_RPG("mmorpg", "Massive online worlds with lots of players."),
  FIGHTING("fighting", "One-on-one fighting games."),
  PARTY("party", "Light, quick games for groups."),
  PUZZLE("puzzle", "Brain teasers and logic games."),
  PLATFORMER("platformer", "Jumping and running through levels.");

  private final String label;
  private final String description;
}
