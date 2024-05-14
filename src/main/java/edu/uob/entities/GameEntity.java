package edu.uob.entities;

public abstract class GameEntity {
    private String name;
    private String description;

    public GameEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public GameEntity(String name) {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }
}
