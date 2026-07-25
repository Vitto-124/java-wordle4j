package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameException;
import ru.yandex.practicum.exceptions.WordNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private WordleGame game;
    private WordleDictionary dictionary;
    private PrintWriter log;
    private StringWriter logWriter;

    @BeforeEach
    void setUp() throws GameException {
        // ТОЛЬКО слова из 5 букв!
        dictionary = new WordleDictionary(Arrays.asList(
                "герой", "город", "гонец", "гость", "грозы",
                "гроза", "грязь", "гусар", "густо", "гуща",
                "дамба", "дверь", "дело", "день", "дети",
                "диван", "добро", "дождь", "домен", "доска"
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
        assertThrows(WordNotFoundException.class, () -> game.makeGuess("неверное"));
    }

    @Test
    void testShortWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> game.makeGuess("дом"));
    }

    @Test
    void testLongWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> game.makeGuess("компьютер"));
    }

    @Test
    void testEnglishLettersThrowsException() {
        assertThrows(WordNotFoundException.class, () -> game.makeGuess("hello"));
    }

    @Test
    void testWordNotInDictionaryThrowsException() {
        assertThrows(WordNotFoundException.class, () -> game.makeGuess("абвгд"));
    }

    @Test
    void testEmptyWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> game.makeGuess(""));
    }

    @Test
    void testNullWordThrowsException() {
        assertThrows(WordNotFoundException.class, () -> game.makeGuess(null));
    }

    @Test
    void testHintGeneration() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertEquals(5, hint.length());  // ← теперь точно 5 букв
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void testHintNotGeneratedAfterGameOver() throws GameException {
        String answer = game.getAnswer();
        game.makeGuess(answer);

        assertTrue(game.isGameOver());
        assertTrue(game.isWon());

        String hint = game.getHint();
        assertNull(hint);
    }

    @Test
    void testCannotMakeMoveAfterGameOver() throws GameException {
        String answer = game.getAnswer();
        game.makeGuess(answer);

        assertTrue(game.isGameOver());
        assertTrue(game.isWon());

        // Используем слово из 5 букв, которое есть в словаре
        assertThrows(GameException.class, () -> game.makeGuess("город"));
    }

    @Test
    void testStepsDecreaseAfterGuess() throws GameException {
        assertEquals(6, game.getSteps());

        String answer = game.getAnswer();
        String wrongWord = dictionary.getWords().stream()
                .filter(w -> !w.equals(answer))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Нет подходящего слова"));

        game.makeGuess(wrongWord);

        assertEquals(5, game.getSteps());
        assertFalse(game.isGameOver());
        assertEquals(1, game.getGuesses().size());
    }

    @Test
    void testWinWithCorrectGuess() throws GameException {
        String answer = game.getAnswer();
        String result = game.makeGuess(answer);

        assertTrue(game.isGameOver());
        assertTrue(game.isWon());
        assertEquals("+++++", result);
        assertEquals(5, game.getSteps());
    }

    @Test
    void testLoseAfterMaxAttempts() throws GameException {
        String answer = game.getAnswer();

        // Находим слово из 5 букв, которое НЕ является ответом
        String wrongWord = dictionary.getWords().stream()
                .filter(w -> !w.equals(answer))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Нет подходящего слова"));

        // Делаем 6 ходов с одним и тем же словом
        for (int i = 0; i < 6; i++) {
            if (!game.isGameOver()) {
                game.makeGuess(wrongWord);
            }
        }

        assertTrue(game.isGameOver());
        assertFalse(game.isWon());
        assertEquals(0, game.getSteps());
        assertEquals(6, game.getGuesses().size());
    }

    @Test
    void testLogCreated() {
        assertNotNull(log);
        String logContent = logWriter.toString();
        assertTrue(logContent.contains("Игра создана"));
        assertTrue(logContent.contains(game.getAnswer()));
    }

    @Test
    void testGetGuessesReturnsCopy() {
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
