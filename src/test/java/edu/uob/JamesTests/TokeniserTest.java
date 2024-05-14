package edu.uob.JamesTests;

import edu.uob.commands.Tokeniser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TokeniserTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void tokens() {
        String command = "test:test1 test2 test3";
        Tokeniser tokeniser = new Tokeniser(command);
        assertEquals("test",tokeniser.getPlayer());
        assertEquals("[test1, test2, test3]",tokeniser.getTokens().toString());
    }

    @Test
    void tokensUppercase() {
        String command = "TEST:TEST1 TEST2 TEST3";
        Tokeniser tokeniser = new Tokeniser(command);
        assertEquals("test",tokeniser.getPlayer());
        assertEquals("[test1, test2, test3]",tokeniser.getTokens().toString());
    }

    @Test
    void tokensPuntuation() {
        String command = "TEST:test, test! test? test?test it's in-fo";
        Tokeniser tokeniser = new Tokeniser(command);
        assertEquals("test",tokeniser.getPlayer());
        assertEquals("[test, test, test, test, test, it's, in-fo]",tokeniser.getTokens().toString());

        command = "TEST:test: test! test? test?test it's in-fo";
        tokeniser = new Tokeniser(command);
        assertEquals("test",tokeniser.getPlayer());
        assertEquals("[test, test, test, test, test, it's, in-fo]",tokeniser.getTokens().toString());
    }

    @Test
    void testValidUsername(){
        String command = "TEST:look";
        Tokeniser tokeniser = new Tokeniser(command);
        assertEquals(true,tokeniser.getValidPlayerName());

        command = "test:look";
        tokeniser = new Tokeniser(command);
        assertEquals(true,tokeniser.getValidPlayerName());

        command = "tes t:look";
        tokeniser = new Tokeniser(command);
        assertEquals(true,tokeniser.getValidPlayerName());

        command = "test's:look";
        tokeniser = new Tokeniser(command);
        assertEquals(true,tokeniser.getValidPlayerName());

        command = "te-st:look";
        tokeniser = new Tokeniser(command);
        assertEquals(true,tokeniser.getValidPlayerName());

        command = "te&st:look";
        tokeniser = new Tokeniser(command);
        assertEquals(false,tokeniser.getValidPlayerName());

        command = "te*st:look";
        tokeniser = new Tokeniser(command);
        assertEquals(false,tokeniser.getValidPlayerName());

        command = "te(st):look";
        tokeniser = new Tokeniser(command);
        assertEquals(false,tokeniser.getValidPlayerName());

        command = "tes!t:look";
        tokeniser = new Tokeniser(command);
        assertEquals(false,tokeniser.getValidPlayerName());
    }
}