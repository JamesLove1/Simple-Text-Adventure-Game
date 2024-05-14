package edu.uob.commands;

import edu.uob.gameMap.GameMap;

import java.util.ArrayList;

public class BasicCommandsInterp {

    private GameMap gameMap;

    public BasicCommandsInterp(GameMap gameMap){
        this.gameMap = gameMap;
    }

    public String interp(String player, String command, ArrayList<String> tokens){
        command = command.toLowerCase();
        switch (command) {
            case "inventory", "inv" -> {
                int maxEntities  = 0;
                if(checkExtrenousCommands(tokens, maxEntities)){ return "Error - extrenuos entities";}
                return inventory(player);}
            case "get" -> {
                int maxEntities  = 1;
                if(checkExtrenousCommands(tokens, maxEntities)){ return "Error - extrenuos entities";}
                int numArtifacts = numArtifacts(player, tokens);
                for(String potentialArtifact : tokens) {
                    if(numArtifacts == 1 && this.gameMap.artifactExsits(player,potentialArtifact)){
                        return get(potentialArtifact ,player);
                    }
                }
                return "Error - artifact not presnet"; }
            case "drop" -> {
                int maxEntities  = 1;
                if(checkExtrenousCommands(tokens, maxEntities)){ return "Error - extrenuos entities";}
                int numArtifacts = numArtifactsInInv(player, tokens);
                for(String potentialArtifact : tokens) {
                    if(numArtifacts == 1 && this.gameMap.artifactInPlayerInv(player,potentialArtifact)){
                        return drop(potentialArtifact, player);
                    }
                }
                return "Error - artifact not present in player inventory";}
            case "goto" -> {
                int maxEntities  = 1;
                if(checkExtrenousCommands(tokens, maxEntities)){ return "extrenuos entities";}
                int numPaths = numPaths(player, tokens);
                for(String path : tokens) {
                    if(numPaths == 1 && this.gameMap.pathExsits(player, path)){
                        return goto_basicCommand(path, player);
                    }
                }
                return "Error - path not present";}
            case "look" -> {
                int maxEntities  = 0;
                if(checkExtrenousCommands(tokens, maxEntities)){ return "extrenuos entities";}
                int extraPaths = 0;
                for(String entities : tokens) {
                    if(this.gameMap.pathNum(entities)){
                        extraPaths++;
                    }
                }
                if(extraPaths > 0){
                    return "Error - to many extenouse entities";
                }
                return look(player);
            }
            case "health" -> {
                int maxEntities  = 0;
                if(checkExtrenousCommands(tokens, maxEntities)){ return "extrenuos entities";}
                return health(player);}
            default -> {   }
        };

        return "";
    }

    private boolean checkExtrenousCommands(ArrayList<String> tokens, Integer maxNumEntities){

        int entityCounter = 0;

        for (String token : tokens){

            if(this.gameMap.getEntityNames().contains(token)){
                entityCounter++;
            }

        }

        if (entityCounter > maxNumEntities){
            return true;
        }
        return false;

    }



    private String health(String player){
        return this.gameMap.playerHealth(player);
    }

    private String look(String player) {
      return this.gameMap.getLook(player);
    }

    private String inventory(String player) {
        return this.gameMap.getPlayerInventory(player);
    }

    private String get(String artifact, String player) {
        return this.gameMap.addArtefactToInv(artifact,player);
    }

    private String drop(String token, String player) {
        return this.gameMap.dropArtifact(token,player);
    }

    private String goto_basicCommand(String token, String player) {
        return this.gameMap.goto_command(player, token);
    }

    private int numArtifacts(String player, ArrayList<String> tokens){
        int numArtifacts = 0;
        for(String potentialArtifact : tokens) {
            if(this.gameMap.artifactExsits(player,potentialArtifact)){
                numArtifacts++;
            }
        }
        return numArtifacts;
    }

    private int numArtifactsInInv(String player, ArrayList<String> tokens){
        int numArtifacts = 0;
        for(String artifact : tokens){
            if(this.gameMap.artifactInPlayerInv(player, artifact)){
                numArtifacts++;
            }
        }
        return numArtifacts;
    }

    private int numPaths(String player, ArrayList<String> tokens){
        int numPaths = 0;
        for(String paths : tokens){
            if(this.gameMap.pathNum(paths)){
                numPaths++;
            }
        }
        return numPaths;
    }


}
