package edu.uob.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Players extends GameEntity{

    Integer health;

    HashMap<String,Artefacts> inventory;

    public Players(String name) {
        super(name);
        this.inventory = new HashMap<>();
        this.health = 3;
    }

    public String getInventoryKeys(){
        return this.inventory.keySet().toString();
    }

    public boolean artifactInIvn(String item){
        return this.inventory.containsKey(item);
    }

    public HashMap<String,Artefacts> getInv(){
        return this.inventory;
    }

    public void addToInventory(Artefacts artifact){
        this.inventory.put(artifact.getName(),artifact);
    }

    public Artefacts removeFromInventory(String artifact){
        Artefacts tempArtifact = this.inventory.get(artifact);
        this.inventory.remove(artifact);
        return tempArtifact;
    }

    public void addHealth(){
        if(this.health < 3 ){
            this.health++;
        }
    }

    public void reduceHealth(){
        this.health--;
    }

    public boolean died(){
        if(this.health < 1){
            this.health = 3; //reset health to 3 - done
            return true;
        }
        return false;
    }

    public String getHealth(){
        return this.health.toString();
    }

}
