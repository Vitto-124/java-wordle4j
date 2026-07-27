package ru.yandex.practicum.utils;

public class WordNormalizer {

    public String normalize(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.toLowerCase()
                .replace('ё', 'е')
                .trim();
    }

    public boolean isRussianWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return word.matches("[а-яА-ЯёЁ]+");
    }

    public boolean isValidWord(String word) {
        if (word == null) {
            return false;
        }
        String normalized = normalize(word);
        return normalized.length() == 5 && isRussianWord(normalized);
    }
}