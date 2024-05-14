package edu.uob.JamesTests;

import edu.uob.actions.GameAction;
import edu.uob.actions.LoadActions;
import edu.uob.gameMap.LoadDOTFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class LoadActionsTest {

    private LoadActions actions;

    @BeforeEach
    void setUp() {
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        this.actions = new LoadActions(actionsFile);
    }

    @Test
    void loadXML() {

        HashMap<String, HashSet<GameAction>> actionsHashMap = this.actions.getActions();

        assertTrue(actionsHashMap.get("open").iterator().next().checkSubject("trapdoor"));

        assertTrue(actionsHashMap.get("open").iterator().next().checkConsumed("key"));

        assertTrue(actionsHashMap.get("open").iterator().next().checkProduced("cellar"));

        assertEquals("You unlock the trapdoor and see steps leading down into a cellar",
                actionsHashMap.get("open").iterator().next().getNarration());

    }

    @AfterEach
    void tearDown() {
    }
}