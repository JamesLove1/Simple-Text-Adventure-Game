package edu.uob.JamesTests;

import edu.uob.actions.LoadActions;
import edu.uob.commands.CommandInterpreter;
import edu.uob.entities.Players;
import edu.uob.gameMap.GameMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CommandInterpreterTest {

  CommandInterpreter command;

  @BeforeEach
  void setUp() {
    File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
    GameMap gameMap = new GameMap(entitiesFile);

    File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
    LoadActions gameActions = new LoadActions(actionsFile);

    this.command = new CommandInterpreter(gameMap,gameActions);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testActions(){
    assertEquals(
            "added item: axe to inventory",
            this.command.intep("simon: get axe")
    );

    assertEquals(
            "Moved player too - forest : A dark forest",
            this.command.intep("simon: goto forest")
    );

    assertEquals(
            "You cut down the tree with the axe",
            this.command.intep("simon: chop tree with axe")
    );

    assertEquals(
            "forest - A dark forest\n" +
                    "log - A heavy wooden log\n" +
                    "key - Brass key\n" +
                    "path to: cabin - A log cabin in the woods\n",
            this.command.intep("simon: look")
    );

    String responce = this.command.intep("simon: look");
    assertTrue(responce.contains("cabin"));
    assertTrue(responce.contains("Brass key"));
    assertTrue(responce.contains("log"));
    assertTrue(responce.contains("forest"));
    assertTrue(responce.contains("A dark forest"));

    assertEquals(
            "added item: key to inventory",
            this.command.intep("simon: get key")
    );

    assertEquals(
            "Error - path not present",
            this.command.intep("simon: goto forest")
    );

    assertEquals(
            "Moved player too - cabin : A log cabin in the woods",
                    this.command.intep("simon: goto cabin")
    );

    assertEquals(
            "You unlock the trapdoor and see steps leading down into a cellar",
            this.command.intep("simon: open trapdoor with key")
    );

    assertEquals(
            "[axe]",
           this.command.intep("simon: inv")
    );

    assertEquals(
            "Moved player too - cellar : A dusty cellar",
            this.command.intep("simon: goto cellar")
    );

    //test health
    assertEquals(
            "cellar - A dusty cellar\n" +
                    "elf - Angry Elf\n" +
                    "path to: cabin - A log cabin in the woods\n",
            this.command.intep("simon: look")
    );

    assertEquals(
            "3",
            this.command.intep("simon: health")
    );

    assertEquals(
            "You attack the elf, but he fights back and you lose some health",
            this.command.intep("simon: fight the elf")
    );

    assertEquals(
            "2",
            this.command.intep("simon: health")
    );

    assertEquals(
            "You attack the elf, but he fights back and you lose some health",
            this.command.intep("simon: hit the elf")
    );

    assertEquals(
            "1",
            this.command.intep("simon: health")
    );

    assertEquals(
            "you died and lost all of your items, you must return to the start of the game",
            this.command.intep("simon: attack the elf")
    );

    //check for starting location
    assertEquals(
            "cabin - A log cabin in the woods\n" +
                    "potion - Magic potion\n" +
                    "trapdoor - Wooden trapdoor\n" +
                    "path to: forest - A dark forest\n" +
                    "path to: cellar - A dusty cellar\n",
            this.command.intep("simon: look")
    );

    //check for full health
    assertEquals(
            "3",
            this.command.intep("simon: health")
    );

    //check items were drops in location player died
    assertEquals(
            "Moved player too - cellar : A dusty cellar",
            this.command.intep("simon: goto cellar")
    );

    assertEquals(
            "cellar - A dusty cellar\n" +
                    "axe - A razor sharp axe\n" +
                    "elf - Angry Elf\n" +
                    "path to: cabin - A log cabin in the woods\n",
            this.command.intep("simon: look")
    );


  }

}