package edu.uob.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Locations extends GameEntity{

    private HashMap<String, Artefacts> artefacts;

    private HashMap<String, Furniture> furniture;

    private HashMap<String, Characters> characters;

    private HashMap<String, Players> players;

    private HashSet<String> pathsTo;

    public Locations(String name, String description) {
        super(name, description);
        this.artefacts = new HashMap<>();
        this.furniture = new HashMap<>();
        this.characters = new HashMap<>();
        this.players = new HashMap<>();
        this.pathsTo = new HashSet<>();
    }

    public void addArtefacts(String key, Artefacts artefacts){
        this.artefacts.put(key, artefacts);
    }

    public HashMap<String, Artefacts> getArtefacts(){
        return this.artefacts;
    }

    public Artefacts getArtefact(String artifact){
        return this.artefacts.get(artifact);
    }

    public void removeArtefact(String artifact){
        this.artefacts.remove(artifact);
    }

    public void addFurniture(String key, Furniture furniture){
        this.furniture.put(key, furniture);
    }

    public HashMap<String, Furniture> getFurniture(){
        return this.furniture;
    }

    public void removeFurnature(String furnature){
        this.furniture.remove(furnature);
    }

    public void addCharacters(String key, Characters characters){
        this.characters.put(key, characters);
    }

    public HashMap<String, Characters> getCharacters(){
        return this.characters;
    }

    public void addPlayer(String key, Players player){
        this.players.put(key, player);
    }

    public void removePlayer(String player){ this.players.remove(player);}

    public Players getPlayer(String player){
        return this.players.get(player);
    }

    public HashMap<String, Players> getPlayers(){
        return this.players;
    }

    public void addPathsTo(String location){
        this.pathsTo.add(location);
    }

    public Boolean getPathTo(String path){
        return this.pathsTo.contains(path);
    }

    public HashSet<String> getPathTos(){
        return this.pathsTo;
    }

    public void removePath(String path){ this.pathsTo.remove(path);}

    public void setAddHealth(String player){
        this.players.get(player).addHealth();
    }

    public void setReduceHealth(String player){
        this.players.get(player).reduceHealth();
    }

    public Boolean getPlayerDied(String player){

        if(this.players.get(player).died()){

            ArrayList<String> keys = new ArrayList<>(this.players.get(player).getInv().keySet());
            for(String key : keys){
                Artefacts tempArtifact = this.players.get(player).removeFromInventory(key);
                this.artefacts.put(tempArtifact.getName(),tempArtifact);
            }

            return true;

        }

        return false;
    }
}
