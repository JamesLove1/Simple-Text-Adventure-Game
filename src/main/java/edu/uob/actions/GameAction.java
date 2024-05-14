package edu.uob.actions;

import edu.uob.entities.GameEntity;

import java.util.HashMap;
import java.util.HashSet;

public class GameAction {

    HashSet<String> siblingTriggers;

    HashSet<String> subjects;

    HashSet<String> consumed;

    HashSet<String> produced;

    String narration;

    public GameAction(){
        this.siblingTriggers = new HashSet<>();
        this.subjects = new HashSet<>();
        this.consumed = new HashSet<>();
        this.produced = new HashSet<>();
        this.narration = "";
    }

    public GameAction(HashSet<String> subjects,
                      HashSet<String> consumed,
                      HashSet<String> produced,
                      HashSet<String> siblingTriggers,
                      String narration ){
        this.siblingTriggers = siblingTriggers;
        this.subjects = subjects;
        this.consumed = consumed;
        this.produced = produced;
        this.narration = narration;
    }

    public void addSubjects(String entity){
        this.subjects.add(entity);
    }

    public HashSet<String> getSubject(){ return this.subjects;}

    public boolean checkSubject(String value){ return this.subjects.contains(value);}

    public void addConsumed(String entity){
        this.consumed.add(entity);
    }

    public HashSet<String> getConsumed(){ return this.consumed;}

    public boolean checkConsumed(String value){ return this.consumed.contains(value);}

    public void addProduced(String entity){
        this.produced.add(entity);
    }

    public HashSet<String> getProduced(){ return this.produced;}

    public boolean checkProduced(String value){ return this.produced.contains(value);}

    public void addNarration(String entity){
        this.narration = entity ;
    }

    public String getNarration(){ return this.narration ;}

    public void addSiblingTriggers(String trigger){
        this.siblingTriggers.add(trigger);
    }

    public HashSet<String> getSiblingTriggers() {
        return siblingTriggers;
    }
}
