package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.exceptions.DictionaryLoadException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {
    private WordleDictionaryLoader loader;

    @BeforeEach
    void setUp() {
        loader = new WordleDictionaryLoader();
    }

    @Test
    void testLoadValidDictionary(@TempDir Path tempDir) throws IOException, DictionaryLoadException {
        Path dictionaryFile = tempDir.resolve("words.txt");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dictionaryFile.toFile()), StandardCharsets.UTF_8))) {
            writer.write("герой\n");
            writer.write("город\n");
            writer.write("гонец\n");
            writer.write("гость\n");
            writer.write("грозы\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictionaryFile.toString());

        assertNotNull(dictionary);
        assertEquals(5, dictionary.size());
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("город"));
        assertTrue(dictionary.contains("гонец"));
        assertTrue(dictionary.contains("гость"));
        assertTrue(dictionary.contains("грозы"));
    }

    @Test
    void testLoadDictionaryWithInvalidWords(@TempDir Path tempDir) throws IOException, DictionaryLoadException {
        Path dictionaryFile = tempDir.resolve("words.txt");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dictionaryFile.toFile()), StandardCharsets.UTF_8))) {
            writer.write("герой\n");
            writer.write("абвгд\n");
            writer.write("12345\n");
            writer.write("hello\n");
            writer.write("слишкомдлинное\n");
            writer.write("дом\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictionaryFile.toString());

        assertEquals(2, dictionary.size());
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("абвгд"));
        assertFalse(dictionary.contains("hello"));
        assertFalse(dictionary.contains("12345"));
    }

    @Test
    void testLoadDictionaryWithUpperCase(@TempDir Path tempDir) throws IOException, DictionaryLoadException {
        Path dictionaryFile = tempDir.resolve("words.txt");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dictionaryFile.toFile()), StandardCharsets.UTF_8))) {
            writer.write("ГЕРОЙ\n");
            writer.write("ГОРОД\n");
            writer.write("Гонец\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictionaryFile.toString());

        assertEquals(3, dictionary.size());
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("город"));
        assertTrue(dictionary.contains("гонец"));
        assertFalse(dictionary.contains("ГЕРОЙ"));
    }

    @Test
    void testLoadDictionaryWithE(@TempDir Path tempDir) throws IOException, DictionaryLoadException {
        Path dictionaryFile = tempDir.resolve("words.txt");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dictionaryFile.toFile()), StandardCharsets.UTF_8))) {
            writer.write("ёжик\n");
            writer.write("берёза\n");
            writer.write("озеро\n");
            writer.write("клён\n");

            writer.write("мелёда\n");
        }
    }

    @Test
    void testLoadEmptyFileThrowsException(@TempDir Path tempDir) throws IOException {
        Path emptyFile = tempDir.resolve("empty.txt");
        emptyFile.toFile().createNewFile();

        assertThrows(DictionaryLoadException.class,
                () -> loader.loadDictionary(emptyFile.toString()));
    }

    @Test
    void testLoadNonExistentFileThrowsException() {
        assertThrows(DictionaryLoadException.class,
                () -> loader.loadDictionary("nonexistent_file.txt"));
    }

    @Test
    void testLoadFileWithOnlyInvalidWordsThrowsException(@TempDir Path tempDir) throws IOException {
        Path dictionaryFile = tempDir.resolve("invalid.txt");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dictionaryFile.toFile()), StandardCharsets.UTF_8))) {
            writer.write("12345\n");
            writer.write("hello\n");
            writer.write("дом\n");
        }

        assertThrows(DictionaryLoadException.class,
                () -> loader.loadDictionary(dictionaryFile.toString()));
    }

    @Test
    void testLoadDictionaryWithDuplicates(@TempDir Path tempDir) throws IOException, DictionaryLoadException {
        Path dictionaryFile = tempDir.resolve("words.txt");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dictionaryFile.toFile()), StandardCharsets.UTF_8))) {
            writer.write("герой\n");
            writer.write("герой\n");
            writer.write("город\n");
            writer.write("город\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictionaryFile.toString());

        assertEquals(4, dictionary.size());
    }
}
