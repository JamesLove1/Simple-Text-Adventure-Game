package edu.uob.commands;

import edu.uob.actions.GameAction;
import edu.uob.actions.LoadActions;
import edu.uob.entities.Players;
import edu.uob.gameMap.GameMap;

import java.io.File;
import java.util.*;

public class CommandInterpreter {

  private HashMap<String, HashSet<GameAction>> actions;

  private GameMap gameMap;

  private HashSet<String> basicCommands;

  private GameAction action;

  public CommandInterpreter(GameMap gameMap, LoadActions gameActions){

    this.actions = gameActions.getActions();

    this.gameMap = gameMap;

    this.basicCommands = new HashSet<>();
    this.basicCommands.add("inventory");
    this.basicCommands.add("inv");
    this.basicCommands.add("get");
    this.basicCommands.add("drop");
    this.basicCommands.add("goto");
    this.basicCommands.add("look");
    this.basicCommands.add("health");

  }

  public String intep(String command){

    Tokeniser tokens = new Tokeniser(command);
    if(!tokens.getValidCommand()){return "Error - invalid command";}
    if(!tokens.getValidPlayerName()){ return "Error - invalid player name";}

    this.gameMap.checkPlayerExsits(tokens.getPlayer());

    if(isAmbiguousCommand(tokens)){return "Error - command is to ambiguous";}

    for(String token : tokens.getTokens()){

      if(this.basicCommands.contains(token)){
        BasicCommandsInterp basicCommand = new BasicCommandsInterp(this.gameMap);
        return basicCommand.interp(tokens.getPlayer(), token, tokens.getTokens());
      }

      if(this.actions.containsKey(token)){

        if(containsSubjects(tokens.getPlayer(),token, tokens.getTokens())){

          if(extraneousEntities(tokens.getTokens())){
            return "Error - extrenous Entites present within command";
          }

          //check that i can conssume and produce entitys - create hashset of of all valid entity (paths current location, artifactes etc )
          if(checkConsumedProduce(tokens.getPlayer())){
            return "Error - item cant be produced consumed";
          }

          if(removeConsumables(tokens.getPlayer(),this.action.getConsumed().size())){
            return "Error - consumables not removed";
          }

          // check if player has died
          String hasPlayerDied = this.gameMap.hasPlayerDied(tokens.getPlayer());
          if (!hasPlayerDied.isEmpty()) {
            return hasPlayerDied;
          }

          produceEntitiy(tokens.getPlayer());

          return this.action.getNarration();
        }

      }

    }

    return "Error - no command exsits";
  }

  private Boolean checkConsumedProduce(String player){

    HashSet<String> ableProduced = this.gameMap.getCheckProduced(player,true);
    HashSet<String> ableConsumed = this.gameMap.getCheckProduced(player,false);

    for( String produce : this.action.getProduced()){
      if(!ableProduced.contains(produce)){
        return true;
      }
    }

    for( String consumed : this.action.getConsumed()){
      if(!ableConsumed.contains(consumed)){
        return true;
      }
    }

    return false;
  }

  private boolean extraneousEntities(ArrayList<String> tokenList){

    for (String token : tokenList){
      Boolean subjectPresent = this.action.getSubject().contains(token);
      Boolean matchingGameEntity = this.gameMap.getEntityNames().contains(token);
      if(!subjectPresent && matchingGameEntity){
        return true;
      }
    }
    return false;
  }

  private void produceEntitiy(String player){
    for ( String produced : this.action.getProduced()){

      if(!this.gameMap.storeRoomToMap(player, produced)){}

    }
  }

  private boolean removeConsumables(String player, int numConsumables){
    // remove consumables are all consumed e.g. if an action has two consumables it must find and consume them
    // check to see if they are in other map positions
    int numRumovedConsumables = 0;
    for ( String consumed : this.action.getConsumed()){
      if(this.gameMap.removeConsumables(player,consumed)){
        numRumovedConsumables++;
      }
    }

    if(numRumovedConsumables != numConsumables){
      return true;
    }
    return false;
  }

  private Boolean containsSubjects(String player, String trigger, ArrayList<String> tokens){

    for( GameAction gameAction : this.actions.get(trigger)){

      int count = 0;
      int commandContainSubject = 0;

      for ( String subject : gameAction.getSubject()){

        if(tokens.contains(subject)){
          commandContainSubject++;
        }

        if(this.gameMap.getValidSubject(player, subject)){
          count++;
        }

      }

      if(gameAction.getSubject().size() == count && commandContainSubject > 0){
        this.action = gameAction;
        return true;
      }

    }
    return false;
  }

  private Boolean isAmbiguousCommand(Tokeniser tokens){
    Integer numTriggerTokens = 0;

    HashSet<String> basicCommandComeAcross = new HashSet<>();
    HashSet<String> siblingTriggerWords = new HashSet<>();

    // dont count if sibling triggers or same basic command twice

    for(String token : tokens.getTokens()) {

      if (this.basicCommands.contains(token) && !basicCommandComeAcross.contains(token)) {
        basicCommandComeAcross.add(token);
        numTriggerTokens++;
      }

      if (this.actions.containsKey(token) && !siblingTriggerWords.contains(token)) {

        // contains subjects then add sibling trigger words to hashset of sibbling trigger words
        if(containsSubjects(tokens.getPlayer(),token, tokens.getTokens())){
          for(String siblingTrigger :this.action.getSiblingTriggers()){
            siblingTriggerWords.add(siblingTrigger);
          }
        }

        numTriggerTokens++;
      }
    }
    if(numTriggerTokens > 1){
      return true;
    }
    return false;
  }

}
// move lumber jack