package edu.uob.JamesTests;

import edu.uob.actions.LoadActions;
import edu.uob.commands.BasicCommandsInterp;
import edu.uob.commands.CommandInterpreter;
import edu.uob.commands.Tokeniser;
import edu.uob.gameMap.GameMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BasicCommandsInterpTest {

    BasicCommandsInterp basicCommand;

    GameMap gameMap;

    Tokeniser t;

    @BeforeEach
    void setUp() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        gameMap = new GameMap(entitiesFile);
        gameMap.checkPlayerExsits("simon:");
        this.basicCommand = new BasicCommandsInterp(gameMap);
    }

    ArrayList<String> tokeniserCall(String command){
        t = new Tokeniser(command);
        return t.getTokens();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testLook() {

        String command = "simon: look  ";
        assertEquals("cabin - A log cabin in the woods\n" +
                "potion - Magic potion\n" +
                "axe - A razor sharp axe\n" +
                "trapdoor - Wooden trapdoor\n" +
                "path to: forest - A dark forest\n",
                this.basicCommand.interp("simon:", "look", tokeniserCall(command))
        );

        assertEquals("cabin - A log cabin in the woods\n" +
                "potion - Magic potion\n" +
                "axe - A razor sharp axe\n" +
                "trapdoor - Wooden trapdoor\n" +
                "path to: forest - A dark forest\n",
                this.basicCommand.interp("simon:", "LOOK", tokeniserCall(command))
        );
    }

    @Test
    void testInventory() {
        String command = "simon: inv  ";
        assertEquals("Inventory is empty", this.basicCommand.interp("simon:", "inv", tokeniserCall(command)));
        command = "simon: INV  ";
        assertEquals("Inventory is empty", this.basicCommand.interp("simon:", "INV", tokeniserCall(command)));
        command = "simon: inventory  ";
        assertEquals("Inventory is empty", this.basicCommand.interp("simon:", "inventory", tokeniserCall(command)));
        command = "simon: INVENTORY  ";
        assertEquals("Inventory is empty", this.basicCommand.interp("simon:", "INVENTORY", tokeniserCall(command)));
    }

    @Test
    void testGet() {

        String result = "added item: axe to inventory";
        String command = "simon: get axe";
        assertEquals(result, this.basicCommand.interp("simon:", "get", tokeniserCall(command)));
        assertEquals("[axe]", this.gameMap.getPlayerInventory("simon:"));
        assertEquals(
                "cabin - A log cabin in the woods\n" +
                "potion - Magic potion\n" +
                "trapdoor - Wooden trapdoor\n" +
                "path to: forest - A dark forest\n",
                this.gameMap.getLook("simon:")
        );

        setUp();
        command = "simon: get AXE";
        assertEquals("added item: axe to inventory", this.basicCommand.interp("simon:", "GET", tokeniserCall(command)));
        assertEquals("[axe]",this.gameMap.getPlayerInventory("simon:"));
        assertEquals(
                "cabin - A log cabin in the woods\n" +
                        "potion - Magic potion\n" +
                        "trapdoor - Wooden trapdoor\n" +
                        "path to: forest - A dark forest\n",
                this.gameMap.getLook("simon:")
        );
    }

    @Test
    void testDrop() {
        String command = "simon: get axe";
        this.basicCommand.interp("simon:", "get", tokeniserCall(command));
        command = "simon:drop axe";
        assertEquals(
                "Droped axe from inventory into cabin",
                this.basicCommand.interp("simon:","drop", tokeniserCall(command))
        );

        setUp();
        command = "simon:get axe";
        this.basicCommand.interp("simon:","get", tokeniserCall(command));
        command = "simon:DROP AXE";
        assertEquals("Droped axe from inventory into cabin",
                this.basicCommand.interp("simon:","DROP", tokeniserCall(command))
        );

        command = "simon:DROP AXE";
        assertEquals("Error - artifact not present in player inventory",
                this.basicCommand.interp("simon:","DROP", tokeniserCall(command))
        );

    }

    @Test
    void testGoTo() {

        String command = "simon: goto forest";
        assertEquals("Moved player too - forest : A dark forest",
                this.basicCommand.interp("simon:","goto",tokeniserCall(command))
        );

        setUp();
        command = "simon: GOTO FOREST";
        assertEquals("Moved player too - forest : A dark forest",
                this.basicCommand.interp("simon:","GOTO",tokeniserCall(command))
        );

        command = "simon:goto cellar";
        assertEquals("Error - path not present",
                this.basicCommand.interp("simon:","goto",tokeniserCall(command))
        );
    }
}