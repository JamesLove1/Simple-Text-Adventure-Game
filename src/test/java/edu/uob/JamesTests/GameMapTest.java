package edu.uob.JamesTests;

import edu.uob.gameMap.GameMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

  GameMap gameMap;

  @BeforeEach
  void setUp() {
    File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
    gameMap = new GameMap(entitiesFile);

  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testCheckPlayerExsits() {
    assertEquals(false,gameMap.checkPlayerExsits("James"));
    assertEquals(true,gameMap.checkPlayerExsits("James"));
  }


}