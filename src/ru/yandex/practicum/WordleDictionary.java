package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {
    private final List<String> words;
    private final Set<String> wordSet;
    private final Map<String, Set<Character>> wordLettersCache;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
        this.wordSet = new HashSet<>(words);
        this.wordLettersCache = new HashMap<>();

        for (String word : words) {
            wordLettersCache.put(word, toCharSet(word));
        }
    }

    public boolean contains(String word) {
        return wordSet.contains(word);
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public Set<Character> getLettersSet(String word) {
        return wordLettersCache.getOrDefault(word, toCharSet(word));
    }

    private Set<Character> toCharSet(String word) {
        Set<Character> chars = new HashSet<>();
        for (char c : word.toCharArray()) {
            chars.add(c);
        }
        return chars;
    }

    public String analyzeGuess(String guess, String answer) {
        if (guess == null || answer == null || guess.length() != answer.length()) {
            throw new IllegalArgumentException("Слова должны быть одинаковой длины");
        }

        StringBuilder result = new StringBuilder();
        Map<Character, Integer> answerCounts = new HashMap<>();
        for (char c : answer.toCharArray()) {
            answerCounts.merge(c, 1, Integer::sum);
        }

        char[] guessChars = guess.toCharArray();
        char[] answerChars = answer.toCharArray();
        boolean[] used = new boolean[answer.length()];

        // Точные совпадения
        for (int i = 0; i < guessChars.length; i++) {
            if (guessChars[i] == answerChars[i]) {
                result.append('+');
                used[i] = true;
                answerCounts.merge(answerChars[i], -1, Integer::sum);
            } else {
                result.append(' ');
            }
        }

        // Буквы не на своих местах
        for (int i = 0; i < guessChars.length; i++) {
            if (result.charAt(i) == ' ') {
                char c = guessChars[i];
                if (answerCounts.getOrDefault(c, 0) > 0 && containsLetter(answer, c, used)) {
                    result.setCharAt(i, '^');
                    answerCounts.merge(c, -1, Integer::sum);
                } else {
                    result.setCharAt(i, '-');
                }
            }
        }

        return result.toString();
    }

    private boolean containsLetter(String word, char letter, boolean[] used) {
        for (int i = 0; i < word.length(); i++) {
            if (!used[i] && word.charAt(i) == letter) {
                return true;
            }
        }
        return false;
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public int size() {
        return words.size();
    }
}
