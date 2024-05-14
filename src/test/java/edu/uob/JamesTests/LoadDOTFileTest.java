package edu.uob.JamesTests;

import edu.uob.entities.Locations;
import edu.uob.gameMap.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LoadDOTFileTest {

    private LoadDOTFile location;

    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        this.location = new LoadDOTFile(entitiesFile);
    }

    @Test
    void testLocation(){
        HashMap<String, Locations> testLocations =  this.location.getLocations();

        //test location
        assertEquals("cabin",testLocations.get("cabin").getName());

        //test Artifacts
        assertEquals("axe",testLocations.get("cabin").getArtefacts().get("axe").getName());
        assertEquals("A razor sharp axe",testLocations.get("cabin").getArtefacts().get("axe").getDescription());

        //test Furnature
        assertEquals("trapdoor",testLocations.get("cabin").getFurniture().get("trapdoor").getName());
        assertEquals("Wooden trapdoor",testLocations.get("cabin").getFurniture().get("trapdoor").getDescription());

        //test Characters
        assertEquals("elf",testLocations.get("cellar").getCharacters().get("elf").getName());
        assertEquals("Angry Elf",testLocations.get("cellar").getCharacters().get("elf").getDescription());
    }

    @Test
    void testPaths(){
        assertEquals(true, this.location.getLocations().get("cabin").getPathTo("forest"));
        assertEquals(false, this.location.getLocations().get("cabin").getPathTo("cellar"));

        assertEquals(true, this.location.getLocations().get("forest").getPathTo("cabin"));
        assertEquals(false, this.location.getLocations().get("forest").getPathTo("cellar"));

    }

    @AfterEach
    void tearDown() {
    }



}