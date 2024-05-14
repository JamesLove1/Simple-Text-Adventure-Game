package edu.uob.JamesTests;

import edu.uob.GameServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class GameServerJamesTest {

    private GameServer server;

    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "james-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "james-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        return server.handleCommand(command);
    }

    @Test
    void testMultiPathCall(){
        String request;

        sendCommandToServer("James: goto marina");
        sendCommandToServer("James: get key");
        request = sendCommandToServer("James: look");
        assertTrue(request.contains("gate"));

        sendCommandToServer("James: open gate");
        request = sendCommandToServer("James: open gate");
        request = sendCommandToServer("James: look");
        assertTrue(request.contains("path to: pontoons"));

    }

    @Test
    void gamePlay(){
        String request;

        request = sendCommandToServer("James: look");
        assertTrue(request.contains("motorboatkillswitch"));

        request = sendCommandToServer("James: get MotorBoatKillSwitch");
        assertFalse(request.contains("error"));

        request = sendCommandToServer("James: goto marina");
        assertTrue(request.contains("Moved player too - marina : a old marina "));

        sendCommandToServer("James: get key");
        sendCommandToServer("James: get magicHorn");
        sendCommandToServer("James: get fishingNet");
        request = sendCommandToServer("James: inv");
        assertTrue(request.contains("key"));
        assertTrue(request.contains("magichorn"));
        assertTrue(request.contains("fishingnet"));

        request = sendCommandToServer("James: open gate");
        assertTrue(request.contains("You unlock the door and see the pontoons"));

        sendCommandToServer("James: goto pontoons");
        request = sendCommandToServer("James: look");
        assertTrue(request.contains("boat"));
        assertTrue(request.contains("pontoons"));

        request = sendCommandToServer("James: inv");
        assertTrue(request.contains("motorboatkillswitch"));

        request = sendCommandToServer("James: start boat engin");
        assertTrue(request.contains("boat"));

        sendCommandToServer("James: goto sea");

        request = sendCommandToServer("James: look");
        assertTrue(request.contains("bluesea"));
        assertTrue(request.contains("fish"));

        request = sendCommandToServer("James: blowHorn with magicHorn");
        assertTrue(request.contains("a showl of salmon lay on the surface of the water"));

        sendCommandToServer("James: get tuna");
        request = sendCommandToServer("James: get showlofsalmon ");
        assertTrue(request.contains("added item: showlofsalmon to inventory"));

        request = sendCommandToServer("James: goto pontoons ");
        assertTrue(request.contains("Moved player too - pontoons : a wabble pontoon"));
        sendCommandToServer("James: goto marina");
        sendCommandToServer("James: goto boathouse");

        request = sendCommandToServer("James: sell showlofsalmon");
        assertTrue(request.contains("the old salty sea dog pays you a pounds £££"));
        request = sendCommandToServer("James: look");
        assertTrue(request.contains("coin"));

        request = sendCommandToServer("James: sell tuna");
        assertTrue(request.contains("the old salty sea dog pays you a pounds £££"));
        request = sendCommandToServer("James: look");
        assertTrue(request.contains("coingold"));
    }

}
