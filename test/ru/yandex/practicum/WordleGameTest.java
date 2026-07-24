package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameException;
import ru.yandex.practicum.exceptions.WordNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private WordleGame game;
    private WordleDictionary dictionary;
    private PrintWriter log;
    private StringWriter logWriter;

    @BeforeEach
    void setUp() throws GameException {
        dictionary = new WordleDictionary(Arrays.asList(
                "герой", "город", "гонец", "гость", "грозы"
        ));
        logWriter = new StringWriter();
        log = new PrintWriter(logWriter);
        game = new WordleGame(dictionary, log);
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getAnswer());
        assertEquals(6, game.getSteps());
        assertEquals(6, game.getMaxAttempts());
        assertFalse(game.isGameOver());
        assertFalse(game.isWon());
        assertTrue(game.getGuesses().isEmpty());
        assertTrue(game.getAbsentLetters().isEmpty());
        assertTrue(game.getRequiredLetters().isEmpty());
        assertTrue(game.getExactPositions().isEmpty());
    }

    @Test
    void testInvalidWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess("неверное");
        });
    }

    @Test
    void testShortWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess("дом");
        });
    }

    @Test
    void testLongWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess("компьютер");
        });
    }

    @Test
    void testEnglishLettersThrowsException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess("hello");
        });
    }

    @Test
    void testWordNotInDictionaryThrowsException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess("абвгд");
        });
    }

    @Test
    void testEmptyWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess("");
        });
    }

    @Test
    void testNullWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess(null);
        });
    }

    @Test
    void testHintGeneration() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertEquals(5, hint.length());
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void testHintNotGeneratedAfterGameOver() throws GameException {
        assertNotNull(game.getHint());
    }

    @Test
    void testCannotMakeMoveAfterGameOver() throws GameException {
        assertFalse(game.isGameOver());
    }

    @Test
    void testStepsDecreaseAfterGuess() throws GameException {
        int initialSteps = game.getSteps();
        assertEquals(6, initialSteps);

    }

    @Test
    void testLogCreated() {
        assertNotNull(log);
        String logContent = logWriter.toString();
        assertTrue(logContent.contains("Игра создана"));
        assertTrue(logContent.contains(game.getAnswer()));
    }

    @Test
    void testGetGuessesReturnsCopy() throws GameException {
        List<String> guesses1 = game.getGuesses();
        guesses1.add("тест");

        List<String> guesses2 = game.getGuesses();
        assertNotEquals(guesses1.size(), guesses2.size());
        assertTrue(guesses2.isEmpty());
    }

    @Test
    void testGetAbsentLettersReturnsCopy() {
        Set<Character> absent1 = game.getAbsentLetters();
        absent1.add('x');

        Set<Character> absent2 = game.getAbsentLetters();
        assertNotEquals(absent1, absent2);
        assertTrue(absent2.isEmpty());
    }

    @Test
    void testGetRequiredLettersReturnsCopy() {
        Set<Character> required1 = game.getRequiredLetters();
        required1.add('x');

        Set<Character> required2 = game.getRequiredLetters();
        assertNotEquals(required1, required2);
        assertTrue(required2.isEmpty());
    }

    @Test
    void testGetExactPositionsReturnsCopy() {
        Map<Integer, Character> positions1 = game.getExactPositions();
        positions1.put(0, 'x');

        Map<Integer, Character> positions2 = game.getExactPositions();
        assertNotEquals(positions1, positions2);
        assertTrue(positions2.isEmpty());
    }
}
