package edu.uob.JamesTests.EditedSimonTexts;

import edu.uob.GameServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * test class for built-in commands
 */
class BuiltInCommandTests {
    /**
     * server object that the tests are run through
     */
    private GameServer server;
    
    @BeforeEach
    void setup() {
        final File entitiesFile = Paths.get("config" + File.separator +
            "extended" +
            "-entities.dot").toAbsolutePath().toFile();
        final File actionsFile = Paths.get("config" + File.separator +
            "extended" +
            "-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }
    
    private String sendCommandToServer(final String command) {
        // Try to send a command to the server - this call will time out if
        // it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> server.handleCommand(command),
            "Server took too long to respond (probably stuck in an infinite loop)");
    }
    
    @Test
    void validInvTest1() {
        final String response1 = sendCommandToServer("Simon: inv");
        assertEquals("Inventory is empty", response1,
            "calling 'inv' with an empty inventory should report back an " +
                "empty inventory");
    }
    
    @Test
    void validInvTest2() {
        sendCommandToServer("Simon: get potion");
        final String response2 = sendCommandToServer("Simon: inv");
        assertEquals("[potion]", response2,
            "calling 'inv' with a potion in inventory should list potion");
    }
    
    @Test
    void validInvTest3() {
        sendCommandToServer("Simon: get potion");
        sendCommandToServer("Simon: get axe");
        final String response3 = sendCommandToServer("Simon: inventory");
        assertEquals("[potion, axe]", response3,
            "calling 'inventory' with a potion and an axe in inventory should" +
                " list potion and axe");
    }
    
    @Test
    void invalidInvTest1() {
        final String response1 = sendCommandToServer("Sion: inv inventory");
        assertEquals("command is to ambiguous", response1,
            "calling 'inv inventory' is invalid because there are two " +
                "built-in command triggers");
    }
    
    @Test
    void invalidInvTest2() {
        final String response1 = sendCommandToServer("Simon: is potion in my " +
            "inventory?");
        assertEquals("extrenuos entities", response1,
            "'is potion in my inventory?' is an invalid command because " +
                "potion is an entity and so invalid decoration for a built-in" +
                " command");
    }
    
    @Test
    void validGetTest1() {
        final String response1 = sendCommandToServer("Simon: get axe");
        assertEquals("added item: axe to inventory", response1,
            "calling 'get axe' should work and report to the user that the " +
                "axe has been gotten");
    }
    
    @Test
    void validGetTest2() {
        sendCommandToServer("Simon: get axe");
        final String response2 = sendCommandToServer("Sion: get coin");
        assertEquals("added item: coin to inventory", response2,
            "calling 'get coin' should work and report to the user that the " +
                "coin has been gotten");
    }
    
    @Test
    void invalidGetTest1() {
        final String response1 = sendCommandToServer("Sion: Get the potion, " +
            "get it " +
            "quickly");
        assertEquals("command is to ambiguous", response1,
            "cannot have two built-in command triggers in a single input");
    }
    
    @Test
    void invalidGetTest2() {
        final String response1 = sendCommandToServer("Simon: get the trapdoor");
        assertEquals("artifact not presnet",
            response1,
            "cannot get a furniture entity");
    }
    
    @Test
    void invalidGetTest3() {
        final String response1 = sendCommandToServer("Sion: get flute");
        assertEquals("artifact not presnet", response1,
            "cannot get an artefact that is in a location other than the " +
                "player's current location");
    }
    
    @Test
    void invalidGetTest4() {
        final String response1 = sendCommandToServer("Simon: get axe and " +
            "potion");
        assertEquals("extrenuos entities",
            response1,
            "cannot call 'get' on more than one artefact");
    }
    
    @Test
    void validDropTest1() {
        sendCommandToServer("Sion: get potion");
        final String response2 = sendCommandToServer("Sion: drop potion");
        assertEquals("Droped potion from inventory into cabin", response2,
            "should be able to drop artefact in player's inventory");
    }
    
    @Test
    void validDropTest2() {
        sendCommandToServer("Sion: get potion");
        sendCommandToServer("Sion: inv");
        sendCommandToServer("Sion: drop potion");
        final String response4 = sendCommandToServer("Sion: inv");
        assertEquals("Inventory is empty", response4,
            "dropping an artefact should remove it from the player's " +
                "inventory");
    }
    
    @Test
    void validDropTest3() {
        sendCommandToServer("Sion: get potion");
        sendCommandToServer("Sion: goto forest");
        sendCommandToServer("Sion: drop potion");
        final String response4 = sendCommandToServer("Sion: look");
        assertEquals("""
            forest-A deep dark forest
            potion-A bottle of magic potion
            key-A rusty old key
            tree-A tall pine tree
            path to:cabin-A log cabin in the woods
            path to:riverbank-A grassy riverbank
            """, response4,
            "dropping an artefact should put it into the location that the " +
                "player is currently in");
    }
    
    @Test
    void invalidDropTest1() {
        final String response1 = sendCommandToServer("Simon: drop trapdoor");
        assertEquals("artifact not present in player inventory",
            response1,
            "cannot drop a furniture entity");
    }
    
    @Test
    void invalidDropTest2() {
        final String response1 = sendCommandToServer("Sion: drop axe");
        assertEquals("artifact not present in player inventory", response1,
            "cannot drop an artefact that isn't in the player's inventory");
    }
    
    @Test
    void invalidDropTest3() {
        sendCommandToServer("Neill: get axe");
        sendCommandToServer("Neill: get potion");
        final String response1 = sendCommandToServer("Neill: drop potion and " +
            "axe");
        assertEquals("extrenuos entities", response1);
    }
    
    @Test
    void validGotoTest1() {
        final String response1 = sendCommandToServer("Alex: goto forest");
        assertEquals("Moved player too - forest : A deep dark forest", response1,
            "going to a valid location should tell the user what is in the " +
                "new location");
    }
    
    @Test
    void invalidGotoTest1() {
        final String response1 = sendCommandToServer("Joe: goto next location");
        assertEquals("path not present", response1,
            "goto requires a location as an argument");
    }
    
    @Test
    void invalidGotoTest2() {
        final String response1 = sendCommandToServer("Sion: goto axe");
        assertEquals("path not present",
            response1,
            "cannot goto an artefact, only locations");
    }
    
    @Test
    void invalidGotoTest3() {
        final String response1 = sendCommandToServer("Sion:  trapdoor goto");
        assertEquals("path not present", response1,
            "cannot goto a furniture, one locations");
    }
    
    @Test
    void invalidGotoTest4() {
        sendCommandToServer("Simon: goto forest");
        sendCommandToServer("Simon: get key");
        sendCommandToServer("Simon: goto cabin");
        sendCommandToServer("Simon: unlock trapdoor");
        final String response1 = sendCommandToServer("Simon: goto forest or cellar");
        assertEquals("extrenuos entities",
            response1,
            "cannot give two arguments to goto command");
    }
    
    @Test
    void validLookTest1() {
        final String response1 = sendCommandToServer("Neill: look around");
        assertEquals("""
            cabin-A log cabin in the woods
            potion-A bottle of magic potion
            axe-A razor sharp axe
            coin-A silver coin
            trapdoor-A locked wooden trapdoor in the floor
            path to:forest-A deep dark forest
            """, response1,
            "look should work with decoration");
    }
    
    @Test
    void invalidLookTest1() {
        final String response1 = sendCommandToServer("Joe: look look");
        assertEquals("command is to ambiguous", response1,
            "cannot provide two built-in command triggers");
    }
    
    @Test
    void invalidLookTest2() {
        final String response1 = sendCommandToServer("Neill: look for axe");
        assertEquals("extrenuos entities",
            response1,
            "cannot provide an entity name as decoration fro a built-in " +
                "command - look takes no arguments");
    }
    
    @Test
    void validHealthTest1() {
        final String response1 = sendCommandToServer("Simon: health");
        assertEquals("3", response1,
            "'health' should provide the user with the player's current " +
                "health");
    }
    
    @Test
    void invalidHealthTest1() {
        final String response1 = sendCommandToServer("Sion: health after potion");
        assertEquals("extrenuos entities", response1,
            "cannot provide an entioty name as decoration for a built-in " +
                "command");
    }
}
