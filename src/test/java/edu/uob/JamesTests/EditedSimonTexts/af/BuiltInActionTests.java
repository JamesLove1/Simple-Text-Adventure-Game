package edu.uob.JamesTests.EditedSimonTexts.af;

import edu.uob.GameServer;
//import edu.uob.command.CommandHandler;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BuiltInActionTests {
    private GameServer server;

    @BeforeEach
    void setup() {
        // Create a GameServer instance
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }
    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will time out if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000000000), () -> server.handleCommand(command),
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    void testGettingDroppingArtefacts() {
        String response;

        // player has nothing in inventory at start
        sendCommandToServer("Justin: look");
        response = sendCommandToServer("Justin: inv").toLowerCase();
        assertTrue(response.contains("empty"));

        // pick up available item
        sendCommandToServer("Justin: get potion");
        response = sendCommandToServer("Justin: inv").toLowerCase();
        assertTrue(response.contains("potion"));
        assertTrue(sendCommandToServer("Justin: get trapdoor").toLowerCase().contains("artifact not presnet"));

        // player drops item, now it's in the current location
        sendCommandToServer("Justin: drop potion");
        response = sendCommandToServer("Justin: look").toLowerCase();
        assertTrue(response.contains("potion"));

        // player picks up two items, has both
        sendCommandToServer("Justin: get potion");
        sendCommandToServer("Justin: goto forest");
        sendCommandToServer("Justin: get key");
        response = sendCommandToServer("Justin: inv").toLowerCase();
        assertTrue(response.contains("potion") && response.contains("key"));

        // player drops one item, has one and the other is in the current location
        sendCommandToServer("Justin: drop potion");
        response = sendCommandToServer("Justin: inv").toLowerCase();
        assertTrue(response.contains("key"));
        assertFalse(response.contains("potion"));

        // test picking up and dropping items that don't exist
        response = sendCommandToServer("Justin: get fake_item").toLowerCase();
        assertTrue(response.contains("error"));
        response = sendCommandToServer("Justin: drop fake_item").toLowerCase();
        assertTrue(response.contains("error"));
    }

    @Test
    void testInvalidCommands() {
        // Invalid look tests
//        assertTrue(sendCommandToServer("Justin: look look").toLowerCase().contains("error"));
        assertTrue(sendCommandToServer("Justin: look for axe").toLowerCase().contains("extrenuos entities"));

        // Invalid inventory tests
        assertTrue(sendCommandToServer("Justin: inv inventory").toLowerCase().contains("command is to ambiguous"));
        assertTrue(sendCommandToServer("Justin: is the axe in my inventory?").toLowerCase().contains("extrenuos entities"));

        // Invalid get tests
//        assertTrue(sendCommandToServer("Justin: get the potion, get it now!").toLowerCase().contains("error"));
        assertTrue(sendCommandToServer("Justin: get the trapdoor").toLowerCase().contains("artifact not presnet"));
        assertTrue(sendCommandToServer("Justin: get cue").toLowerCase().contains("artifact not presnet"));
        assertTrue(sendCommandToServer("Justin: get axe and potion").toLowerCase().contains("extrenuos entities"));

        // Invalid drop tests
        assertTrue(sendCommandToServer("Justin: drop trapdoor").toLowerCase().contains("artifact not present in player inventory"));
        assertTrue(sendCommandToServer("Justin: drop axe").toLowerCase().contains("artifact not present in player inventory"));
        sendCommandToServer("Justin: get axe");
        sendCommandToServer("Justin: get potion");
        assertTrue(sendCommandToServer("Justin: drop potion and axe").toLowerCase().contains("extrenuos entities"));

        // Invalid goto tests
        assertTrue(sendCommandToServer("Justin: goto next location").toLowerCase().contains("path not present"));
        assertTrue(sendCommandToServer("Justin: goto axe").toLowerCase().contains("path not present"));
        assertTrue(sendCommandToServer("Justin: trapdoor goto").toLowerCase().contains("path not present"));
        sendCommandToServer("Justin: goto forest");
        sendCommandToServer("Justin: get key");
        sendCommandToServer("Justin: goto cabin");
        sendCommandToServer("Justin: unlock trapdoor");
        assertTrue(sendCommandToServer("Justin: goto forest or cellar").toLowerCase().contains("extrenuos entities"));
    }
}
