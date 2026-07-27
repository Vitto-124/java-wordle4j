package ru.yandex.practicum.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordNormalizerTest {
    private WordNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new WordNormalizer();
    }

    @Test
    void testNormalizeToLowercase() {
        assertEquals("герой", normalizer.normalize("ГЕРОЙ"));
        assertEquals("герой", normalizer.normalize("Герой"));
        assertEquals("герой", normalizer.normalize("гЕРоЙ"));
    }

    @Test
    void testNormalizeTrim() {
        assertEquals("герой", normalizer.normalize("  герой  "));
        assertEquals("герой", normalizer.normalize("\tгерой\n"));
    }

    @Test
    void testNormalizeReplaceE() {
        assertEquals("елка", normalizer.normalize("ёлка"));
        assertEquals("ежик", normalizer.normalize("ёжик"));
        assertEquals("береза", normalizer.normalize("берёза"));
    }

    @Test
    void testNormalizeEmpty() {
        assertEquals("", normalizer.normalize(""));
        assertEquals("", normalizer.normalize(null));
        assertEquals("", normalizer.normalize("   "));
    }

    @Test
    void testIsRussianWord() {
        assertTrue(normalizer.isRussianWord("герой"));
        assertTrue(normalizer.isRussianWord("ёлка"));
        assertTrue(normalizer.isRussianWord("ГОРОД"));
        assertFalse(normalizer.isRussianWord("hello"));
        assertFalse(normalizer.isRussianWord("12345"));
        assertFalse(normalizer.isRussianWord("герой123"));
        assertFalse(normalizer.isRussianWord(""));
        assertFalse(normalizer.isRussianWord(null));
    }

    @Test
    void testIsValidWord() {
        assertTrue(normalizer.isValidWord("герой"));
        assertTrue(normalizer.isValidWord("город"));
        assertTrue(normalizer.isValidWord("гонец"));
        assertTrue(normalizer.isValidWord("гонка"));
        assertTrue(normalizer.isValidWord("седла"));
        assertTrue(normalizer.isValidWord("зерна"));

        assertFalse(normalizer.isValidWord("ёлка"));
        assertFalse(normalizer.isValidWord("дом"));
        assertFalse(normalizer.isValidWord("компьютер"));
        assertFalse(normalizer.isValidWord("hello"));
        assertFalse(normalizer.isValidWord("12345"));
        assertFalse(normalizer.isValidWord(""));
        assertFalse(normalizer.isValidWord(null));
    }
}
