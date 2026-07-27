package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.utils.WordNormalizer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private final WordNormalizer normalizer;

    public WordleDictionaryLoader() {
        this.normalizer = new WordNormalizer();
    }

    public WordleDictionary loadDictionary(String filename) throws DictionaryLoadException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String normalized = normalizer.normalize(line);
                if (normalizer.isValidWord(normalized)) {
                    words.add(normalized);
                }
            }

            if (words.isEmpty()) {
                throw new DictionaryLoadException("Словарь не содержит подходящих слов (5 букв)");
            }

            return new WordleDictionary(words);

        } catch (FileNotFoundException e) {
            throw new DictionaryLoadException("Файл словаря не найден: " + filename, e);
        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка при чтении файла словаря", e);
        }
    }
}
