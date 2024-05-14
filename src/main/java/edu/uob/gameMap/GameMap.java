package edu.uob.gameMap;

import edu.uob.entities.*;

import java.io.File;
import java.lang.management.BufferPoolMXBean;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class GameMap {

    private HashSet<String> entityNames;

    private HashSet<String> checkProducedConsume;

    private String startLocation;

    private HashMap<String, Locations> location;
    private HashMap<String, Locations> players;

    public GameMap (File entitiesFile){

        LoadDOTFile dotFile = new LoadDOTFile(entitiesFile);
        this.location = dotFile.getLocations();
        this.startLocation = dotFile.getStartLocation();
        this.entityNames = dotFile.getentityNames();
        this.players = new HashMap<>();

    }

    public HashSet<String> getCheckProduced(String player , Boolean produce){
        HashSet<String> entities = new HashSet<>();
        entities.add("health".toLowerCase());
        // current player Inv
        for(String item :this.players.get(player).getPlayer(player).getInv().keySet()){
            entities.add(item.toLowerCase());
        }

        // current location paths
        if(!produce){
            for(String path :this.players.get(player).getPathTos()){
                entities.add(path.toLowerCase());
            }
        }

        for(String location : this.location.keySet()){

            // locations names not store room
            if(!location.equals("storeroom") && produce ){
                entities.add(location.toLowerCase());
            }

            // characters
            for(String character : this.location.get(location).getCharacters().keySet()){
                entities.add(character.toLowerCase());
            }

            // furnature
            for(String furnature : this.location.get(location).getFurniture().keySet()){
                entities.add(furnature.toLowerCase());
            }

            // artifacts
            for(String artifact : this.location.get(location).getArtefacts().keySet()){
                entities.add(artifact.toLowerCase());
            }

        }

        return entities;
    }


    public HashSet<String> getEntityNames(){
        return this.entityNames;
    }

    public Boolean storeRoomToMap(String player, String entity){

        boolean entityIsCharacter = this.location.get("storeroom").getCharacters().containsKey(entity);
        if(entityIsCharacter){
            Characters tempCharacter = this.location.get("storeroom").getCharacters().get(entity);
            this.location.get("storeroom").getCharacters().remove(entity);
            this.players.get(player).addCharacters(tempCharacter.getName(),tempCharacter);
            return true;
        }

        boolean entityIsArtifact = this.location.get("storeroom").getArtefacts().containsKey(entity);
        if(entityIsArtifact){
            Artefacts tempArtifact = this.location.get("storeroom").getArtefacts().get(entity);
            this.location.get("storeroom").removeArtefact(entity);
            this.players.get(player).addArtefacts(tempArtifact.getName(),tempArtifact);
            return true;
        }

        boolean entityIsFurnature = this.location.get("storeroom").getFurniture().containsKey(entity);
        if(entityIsFurnature){
            Furniture tempFurnature = this.location.get("storeroom").getFurniture().get(entity);
            this.location.get("storeroom").removeFurnature(entity);
            this.players.get(player).addFurniture(tempFurnature.getName(),tempFurnature);
            return true;
        }

        boolean entityIsPath = this.location.containsKey(entity);
        if(entityIsPath && !entity.equals("storeroom")){
            this.players.get(player).addPathsTo(entity);
            return true;
        }

        if(entity.equals("health")){
            this.players.get(player).setAddHealth(player);
            return true;
        }

        checkOtherLocations(player, entity);
        return false;
    }

    private void checkOtherLocations(String player, String entity){

        for( String locations : this.location.keySet()){

            boolean entityIsCharacter = this.location.get(locations).getCharacters().containsKey(entity);
            if(entityIsCharacter){
                Characters tempCharacter = this.location.get(locations).getCharacters().get(entity);
                this.location.get(locations).getCharacters().remove(entity);
                this.players.get(player).addCharacters(tempCharacter.getName(),tempCharacter);
                break;
            }

            boolean entityIsArtifact = this.location.get(locations).getArtefacts().containsKey(entity);
            if(entityIsArtifact){
                Artefacts tempArtifact = this.location.get(locations).getArtefacts().get(entity);
                this.location.get(locations).removeArtefact(entity);
                this.players.get(player).addArtefacts(tempArtifact.getName(),tempArtifact);
                break;
            }

            boolean entityIsFurnature = this.location.get(locations).getFurniture().containsKey(entity);
            if(entityIsFurnature){
                Furniture tempFurnature = this.location.get(locations).getFurniture().get(entity);
                this.location.get(locations).removeFurnature(entity);
                this.players.get(player).addFurniture(tempFurnature.getName(),tempFurnature);
                break;
            }

            boolean entityIsPath = this.location.containsKey(entity);
            if(entityIsPath && !entity.equals(locations)){
                this.players.get(player).addPathsTo(entity);
                break;
            }

        }
    }

    public Boolean removeConsumables(String player, String entity){

        Boolean validLocation = this.players.get(player).getName().equals(entity);
        if(validLocation){
            this.players.get(player).removePath(entity);
            return true;
        }

        Boolean validCharacter = this.players.get(player).getCharacters().containsKey(entity);
        if(validCharacter){
            this.players.get(player).getCharacters().remove(entity);
            return true;
        }

        Boolean validFurnature = this.players.get(player).getFurniture().containsKey(entity);
        if(validFurnature){
            this.players.get(player).getFurniture().remove(entity);
            return true;
        }

        Boolean validArtifact = this.players.get(player).getArtefacts().containsKey(entity);
        if(validArtifact){
            this.players.get(player).getArtefacts().remove(entity);
            return true;
        }

        Boolean checkPlayerInv = this.players.get(player).getPlayer(player).getInv().containsKey(entity);
        if(checkPlayerInv){
            this.players.get(player).getPlayer(player).getInv().remove(entity);
            return true;
        }

        if(entity.equals("health")){
            this.players.get(player).setReduceHealth(player);
            return true;
        }

        if(removeFromOtherLocations(player,entity)){
            return true;
        }


        return false;
    }

    private boolean removeFromOtherLocations(String player, String entity){

        String targetLocation = "storeroom";

        this.location.remove(player);

        for( String location : this.location.keySet()){

            boolean entityIsCharacter = this.location.get(location).getCharacters().containsKey(entity);
            if(entityIsCharacter){
                Characters tempCharacter = this.location.get(location).getCharacters().get(entity);
                this.location.get(location).getCharacters().remove(entity);
                this.location.get(targetLocation).addCharacters(tempCharacter.getName(),tempCharacter);
//                this.players.get(player).addCharacters(tempCharacter.getName(),tempCharacter);
                return true;
            }

            boolean entityIsArtifact = this.location.get(location).getArtefacts().containsKey(entity);
            if(entityIsArtifact){
                Artefacts tempArtifact = this.location.get(location).getArtefacts().get(entity);
                this.location.get(location).removeArtefact(entity);
                this.location.get(targetLocation).addArtefacts(tempArtifact.getName(),tempArtifact);
                return true;
            }

            boolean entityIsFurnature = this.location.get(location).getFurniture().containsKey(entity);
            if(entityIsFurnature){
                Furniture tempFurnature = this.location.get(location).getFurniture().get(entity);
                this.location.get(location).removeFurnature(entity);
                this.location.get(targetLocation).addFurniture(tempFurnature.getName(),tempFurnature);
                return true;
            }


        }

        return false;

    }



    public Boolean getValidSubject(String player, String entity){
        Boolean validLocation = this.players.get(player).getName().equals(entity);
        Boolean validCharacter = this.players.get(player).getCharacters().containsKey(entity);
        Boolean validFurnature = this.players.get(player).getFurniture().containsKey(entity);
        Boolean validArtifact = this.players.get(player).getArtefacts().containsKey(entity);
        Boolean checkPlayerInv = this.players.get(player).getPlayer(player).getInv().containsKey(entity);
        if(validLocation || validCharacter || validFurnature || validArtifact || checkPlayerInv){
            return true;
        }
        return false;
    }

    public boolean pathExsits(String player, String path){
        return this.players.get(player).getPathTo(path);
    }

    public boolean pathNum(String location){
        return this.location.containsKey(location);
    }

    public boolean artifactInPlayerInv(String player, String artifact){
        return this.players.get(player).getPlayer(player).artifactInIvn(artifact);
    }

    public boolean artifactExsits(String player ,String artifact){
        if(this.players.get(player).getArtefact(artifact) == null) {
            return false;
        }
        return this.players.get(player).getArtefact(artifact).getName().equals(artifact);
    }

    public String getLook(String player){

        StringBuilder look = new StringBuilder();
        Locations playerLocation = this.players.get(player);

        look.append(this.players.get(player).getName()+" - "+ this.players.get(player).getDescription()+"\n");

        for(Artefacts value: playerLocation.getArtefacts().values()){
            look.append(value.getName() + " - "+ value.getDescription()+"\n");
        }
        for(Furniture value: playerLocation.getFurniture().values()){
            look.append(value.getName() + " - "+ value.getDescription()+"\n");
        }
        for(Players value: playerLocation.getPlayers().values()){
            if(!Objects.equals(value.getName(), player)){
                look.append(value.getName() + " - "+ value.getDescription()+"\n");
            }
        }
        for(Characters value: playerLocation.getCharacters().values()){
            look.append(value.getName() + " - "+ value.getDescription()+"\n");
        }
        for (String paths : playerLocation.getPathTos()){
            String pathTo = "path to: ";
            look.append(pathTo+this.location.get(paths).getName()+" - "+this.location.get(paths).getDescription()+"\n");
        }
        return look.toString();
    }

    public String getPlayerInventory(String player){
        if(this.players.containsKey(player)){
            String playerLocation = this.players.get(player).getName();
            String output = this.location.get(playerLocation).getPlayer(player).getInventoryKeys();
            if( output == "[]"){
                return "Inventory is empty";
            }
            return output;
        }
        return "Inventory is empty";
    }

    public String addArtefactToInv(String artifact, String player){
//        this.players.get(player)
        artifact = artifact.toLowerCase();
        Artefacts tempArtifact = this.players.get(player).getArtefact(artifact);
        this.players.get(player).removeArtefact(artifact);
        this.players.get(player).getPlayers().get(player).addToInventory(tempArtifact);

        return "added item: "+artifact+" to inventory" ;
    }

    public String dropArtifact(String artifact, String player){
        Artefacts tempArtifact = this.players.get(player).getPlayers().get(player).removeFromInventory(artifact);
        if(tempArtifact == null){
            return "artifact does not exsit within inventory";
        }
        this.players.get(player).addArtefacts(tempArtifact.getName(),tempArtifact);
        return "Droped "+tempArtifact.getName()+" from inventory into "+this.players.get(player).getName();
    }

    public Boolean checkPlayerExsits(String player){
        if(this.players.containsKey(player)){
            return true;
        } else {
            Players tempPlayer = new Players(player);
            this.players.put(player, this.location.get(this.startLocation));
            this.location.get(this.startLocation).addPlayer(player , tempPlayer);
        }
        return false;
    }

    public String goto_command(String player, String location){
        if(this.players.get(player).getPathTo(location)){
           Players tempPlayer = this.players.get(player).getPlayers().get(player);
           this.players.get(player).removePlayer(player);
           this.players.put(player,this.location.get(location));
           this.players.get(player).addPlayer(player, tempPlayer);
            return movePlayer(player);
        }
        return "location does not exsit";
    }

    public String hasPlayerDied(String player){

        Boolean activePlayer = this.players.get(player).getPlayers().containsKey(player);

        if(activePlayer && this.players.get(player).getPlayerDied(player)){
            this.goto_command(player,this.startLocation); //move player - needs to be done within gameMap - Done
            return "you died and lost all of your items, you must return to the start of the game";
        }

        return "";

    }

    private String movePlayer(String player){
        String intro =  "Moved player too - ";
        String location = this.players.get(player).getName();
        String description = this.players.get(player).getDescription();
        return intro + location +" : "+ description;
    }

    public String playerHealth(String player){
        return this.players.get(player).getPlayers().get(player).getHealth();
    }
}
