package edu.uob.JamesTests;

import edu.uob.GameServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class GameServerAdvanceTesting {

    private GameServer server;

    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        return server.handleCommand(command);
    }

    @Test
    void testInventory(){
        assertEquals("Inventory is empty",sendCommandToServer("Sion: inventory"));
        assertEquals("Inventory is empty",sendCommandToServer("Sion: inventory!"));

        assertEquals("Inventory is empty",sendCommandToServer("Sion: inventory"));
        assertEquals("Inventory is empty",sendCommandToServer("Sion: inventory!"));

        assertEquals("added item: axe to inventory",sendCommandToServer("Sion: get axe"));
        assertEquals("[axe]",sendCommandToServer("Sion: inv"));
        assertEquals("[axe]",sendCommandToServer("Sion: inv!"));
    }

    @Test
    void testConsumed(){
        String responce;

        // rember to commment out key subject before running this code
        // <entity>key</entity>
        sendCommandToServer("Simon: goto forest");
        sendCommandToServer("Simon: get key");

        sendCommandToServer("Sion: goto cabin");
        responce = sendCommandToServer("Sion: open trapdoor").toLowerCase();
        assertTrue(responce.contains("error"));
    }

    @Test
    void testProduced(){
        String responce;

        // To add gold and remove from dot file
        //gold [description = "A big pot of gold"];
        //<entity>gold</entity>

        sendCommandToServer("Sion: goto forest");
        sendCommandToServer("Sion: get key");
        sendCommandToServer("Sion: goto cabin");
        responce = sendCommandToServer("Sion: open trapdoor").toLowerCase();
        // assertTrue(responce.toLowerCase().contains("error")); -comment back in if following intructions above
        assertTrue(responce.contains("you unlock the door and see steps leading down into a cellar"));

    }

    @Test
    void testLook(){

        // test extrenuos entities
        assertEquals(
                "extrenuos entities",
                sendCommandToServer("Sion: look cabin")
        );

        // test puntuation
        assertEquals(
                "extrenuos entities",
                sendCommandToServer("Sion: look, at the cabin")
        );

        // test actual command
        assertEquals(
                "cabin - A log cabin in the woods\n" +
                        "potion - A bottle of magic potion\n" +
                        "axe - A razor sharp axe\n" +
                        "coin - A silver coin\n" +
                        "trapdoor - A locked wooden trapdoor in the floor\n" +
                        "path to: forest - A deep dark forest\n",
                sendCommandToServer("Sion: look")
        );

        //test decoration
        assertEquals(
                "cabin - A log cabin in the woods\n" +
                        "potion - A bottle of magic potion\n" +
                        "axe - A razor sharp axe\n" +
                        "coin - A silver coin\n" +
                        "trapdoor - A locked wooden trapdoor in the floor\n" +
                        "path to: forest - A deep dark forest\n",
                sendCommandToServer("Sion: look around")
        );
    }

    @Test
    void testHealth(){

        // test ambiguous commands
        assertTrue(sendCommandToServer("Sion: get health").toLowerCase().contains("error"));

        //test decoration
        assertEquals("3",sendCommandToServer("Sion: health leval"));

        // test punctuation
        assertEquals("3",sendCommandToServer("Sion: Show health, levals!"));

        // actual command
        assertEquals("3",sendCommandToServer("Sion: health"));
    }

    @Test
    void testGet(){
        // extrenuos commands
        assertTrue(sendCommandToServer("Sion: Get coin, potion and axe").toLowerCase().contains("error"));

        // test basic command
        assertEquals("added item: potion to inventory",sendCommandToServer("Sion: Get potion"));

        // test decoration
        assertEquals("added item: axe to inventory",sendCommandToServer("Sion: go over and get the axe"));

        // test puntuation
        assertEquals("added item: coin to inventory",sendCommandToServer("Sion: Please, go and get the coin!"));
    }

    @Test
    void testDrop(){
        sendCommandToServer("Sion: Get axe");
        sendCommandToServer("Sion: Get potion");

        // test extrenouse entities
        assertEquals("Error - extrenuos entities",sendCommandToServer("Sion: drop the potion and the axe"));

        // test basic command
        assertEquals("Droped potion from inventory into cabin",sendCommandToServer("Sion: drop potion"));

        // test decoration and puntuation
        assertEquals("Droped axe from inventory into cabin",sendCommandToServer("Sion: Please, drop the axe!"));

    }

    @Test
    void testGoto(){

        // test missing path
        assertEquals("Error - path not present",sendCommandToServer("Sion: goto"));

        // test non exsistant path
        assertEquals("Error - path not present",sendCommandToServer("Sion: goto cabin"));

        // test extrenous entitises
        assertEquals("extrenuos entities",sendCommandToServer("Sion: goto forest with axe"));

        // test basic command
        assertEquals("Moved player too - forest : A deep dark forest", sendCommandToServer("Sion: goto forest"));

        // test puntuation and decoration
        assertEquals(
                "Moved player too - riverbank : A grassy riverbank",
                sendCommandToServer("Sion: Please, can we move the player to the goto the riverbank?")
        );
    }

    @Test
    void testMoveAroundMap() {
        sendCommandToServer("Sion: get axe");
        assertEquals("Moved player too - forest : A deep dark forest", sendCommandToServer("Sion: goto forest"));
        sendCommandToServer("Sion: get key");
        sendCommandToServer("Sion: chop down tree with axe");
        sendCommandToServer("Sion: get log");
        assertEquals("Moved player too - riverbank : A grassy riverbank", sendCommandToServer("Sion: goto riverBank"));
        sendCommandToServer("Sion: create bridge across the river with the log");
        assertEquals("Moved player too - clearing : A clearing in the woods", sendCommandToServer("Sion: goto clearing"));
        assertEquals("Moved player too - riverbank : A grassy riverbank", sendCommandToServer("Sion: goto riverbank"));

        assertEquals("Error - path not present", sendCommandToServer("Sion: goto cabin"));

        assertEquals("Moved player too - forest : A deep dark forest", sendCommandToServer("Sion: goto forest"));
        assertEquals("Moved player too - cabin : A log cabin in the woods", sendCommandToServer("Sion: goto cabin"));
        sendCommandToServer("Sion: open with key");
        assertEquals("Moved player too - cellar : A dusty cellar", sendCommandToServer("Sion: goto cellar"));

    }

    // 3. test actions
    @Test
    void testActions(){
        sendCommandToServer("Sion: get axe");
        assertEquals("Error - no command exsits", sendCommandToServer("Sion: open trapdoor with key"));
        sendCommandToServer("Sion: goto forest");
        sendCommandToServer("Sion: get key");
        String responce = sendCommandToServer("Sion: look");
        assertFalse(responce.contains("key"));
        sendCommandToServer("Sion: goto cabin");

        // test extrenouse entities with text decoration
        assertEquals(
                "Error - extrenous Entites present within command",
                sendCommandToServer("Sion: Please, Open the trapdoor with the key and Axe?")
        );

        // test active action with text decoration
        assertEquals(
                "You unlock the door and see steps leading down into a cellar",
                sendCommandToServer("Sion: Please, Open the trapdoor with the key?")
        );

        String responce1 = sendCommandToServer("Sion: look");
        assertTrue(responce1.contains("path to: cellar"));
    }

    @Test
    void testMultipleTriggerWords(){
        sendCommandToServer("Sion: get axe");
        sendCommandToServer("Sion: goto forest");
        sendCommandToServer("Sion: get key");

        String responce = sendCommandToServer("Sion: look");
        assertTrue(responce.contains("tree"));

        responce = sendCommandToServer("Sion: look look");
        assertTrue(responce.contains("tree"));

        responce = sendCommandToServer("Sion: look look get");
        assertFalse(responce.contains("tree"));

        assertEquals(
          "Error - command is to ambiguous",
          sendCommandToServer("Sion: Please, cut down and chop that tree over there. open")
        );

        // should two sibling triggers cause it not to work - no it should not
        // use one subject and decoration and puntuation
        assertEquals(
          "You cut down the tree with the axe",
          sendCommandToServer("Sion: Please, cut down and chop that tree over there.")
        );

        responce = sendCommandToServer("Sion: look");
        assertTrue(responce.contains("log"));
        assertFalse(responce.contains("tree"));
    }

    @Test
    void testTestActionsAndHealth(){
        sendCommandToServer("Sion: goto forest");
        sendCommandToServer("Sion: get key");
        sendCommandToServer("Sion: goto cabin");
        sendCommandToServer("Sion: get potion");
        sendCommandToServer("Sion: get coin");
        sendCommandToServer("Sion: unlock trapdoor");
        sendCommandToServer("Sion: goto cellar");

        String response = sendCommandToServer("Sion: look around!");
        assertTrue(response.contains("elf"));
        response = sendCommandToServer("Sion: inv");
        assertTrue(response.contains("potion"));
        assertTrue(response.contains("coin"));
        assertFalse(response.contains("key"));

        assertEquals("3",sendCommandToServer("Sion: What is my health?"));
        sendCommandToServer("Sion: hit elf");
        sendCommandToServer("Sion: attack elf");
        assertEquals("1",sendCommandToServer("Sion: What is my health?"));
        assertEquals("You drink the potion and your health improves",sendCommandToServer("Sion: Drink Potion"));
        assertEquals("2",sendCommandToServer("Sion: What is my health?"));
        assertEquals("You pay the elf your silver coin and he produces a shovel",sendCommandToServer("Sion: Pay Elf Coin"));
        sendCommandToServer("Sion: get shovel");
        response = sendCommandToServer("Sion: inv");
        assertTrue(response.contains("shovel"));
    }

    @Test
    void testTwoPlayer(){
        sendCommandToServer("Sion: goto forest");
        sendCommandToServer("Sion: get key");
        sendCommandToServer("Sion: goto cabin");
        sendCommandToServer("Simon: get axe");

        String responce = sendCommandToServer("Simon: look");
        assertTrue(responce.contains("sion"));

        responce = sendCommandToServer("Sion: look");
        assertTrue(responce.contains("simon"));
        assertFalse(responce.contains("axe"));

        sendCommandToServer("Simon: goto forest");
        responce = sendCommandToServer("Simon: look");
        assertFalse(responce.contains("key"));
    }

}
