package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(Arrays.asList(
                "герой", "город", "гонец", "гость", "грозы"
        ));
    }

    @Test
    void testContains() {
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("город"));
        assertFalse(dictionary.contains("абвгд"));
        assertFalse(dictionary.contains("несуществующее"));
    }

    @Test
    void testGetRandomWord() {
        String word = dictionary.getRandomWord();
        assertNotNull(word);
        assertTrue(dictionary.contains(word));
    }

    @Test
    void testAnalyzeGuessCorrect() {
        String result = dictionary.analyzeGuess("герой", "герой");
        assertEquals("+++++", result);
    }

    @Test
    void testAnalyzeGuessPartial() {
        String result = dictionary.analyzeGuess("гонец", "герой");
        assertEquals("+^-^-", result);
    }

    @Test
    void testAnalyzeGuessAllWrong() {
        String result = dictionary.analyzeGuess("шумяц", "герой");
        assertEquals("-----", result);
    }

    @Test
    void testGetLettersSet() {
        Set<Character> letters = dictionary.getLettersSet("герой");
        assertTrue(letters.contains('г'));
        assertTrue(letters.contains('е'));
        assertTrue(letters.contains('р'));
        assertTrue(letters.contains('о'));
        assertTrue(letters.contains('й'));
        assertEquals(5, letters.size());
    }

    @Test
    void testSize() {
        assertEquals(5, dictionary.size());
    }
}
