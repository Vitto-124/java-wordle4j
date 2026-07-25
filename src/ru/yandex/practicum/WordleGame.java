package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameException;
import ru.yandex.practicum.exceptions.WordNotFoundException;
import ru.yandex.practicum.utils.WordNormalizer;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;

    private final String answer;
    private final WordleDictionary dictionary;
    private final WordNormalizer normalizer;
    private final PrintWriter log;
    private final List<String> guesses;
    private final List<String> hints;
    private int steps;
    private boolean gameOver;
    private boolean won;
    private final Set<Character> absentLetters;
    private final Set<Character> requiredLetters;
    private final Map<Integer, Character> exactPositions;
    private final Map<Integer, Set<Character>> excludedPositions;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) throws GameException {
        if (dictionary == null || dictionary.size() == 0) {
            throw new GameException("Словарь не может быть пустым");
        }

        this.dictionary = dictionary;
        this.log = log;
        this.normalizer = new WordNormalizer();
        this.answer = dictionary.getRandomWord();
        this.guesses = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.steps = MAX_ATTEMPTS;
        this.gameOver = false;
        this.won = false;
        this.absentLetters = new HashSet<>();
        this.requiredLetters = new HashSet<>();
        this.exactPositions = new HashMap<>();
        this.excludedPositions = new HashMap<>();

        log.println("Игра создана. Ответ: " + answer);
    }

    public String makeGuess(String input) throws GameException {
        if (gameOver) {
            throw new GameException("Игра уже завершена");
        }

        if (input == null || input.trim().isEmpty()) {
            throw new WordNotFoundException("Вы не ввели слово. Для подсказки просто нажмите Enter.");
        }

        String word = normalizer.normalize(input);

        if (!isValidGuess(word)) {
            throw new WordNotFoundException(
                    "Слово \"" + input + "\" не подходит.\n" +
                            "Оно должно состоять из " + WORD_LENGTH + " русских букв и быть в словаре."
            );
        }

        steps--;
        guesses.add(word);

        if (word.equals(answer)) {
            gameOver = true;
            won = true;
            log.println("Победа! Осталось попыток: " + steps);
            return "+++++";
        }

        String analysis = dictionary.analyzeGuess(word, answer);
        updateState(word, analysis);

        if (steps <= 0) {
            gameOver = true;
            log.println("Поражение. Ответ: " + answer);
            return "К сожалению, попытки закончились. Загаданное слово: " + answer;
        }

        return analysis;
    }

    private void updateState(String word, String analysis) {
        char[] chars = word.toCharArray();
        char[] status = analysis.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (status[i]) {
                case '+':
                    exactPositions.put(i, c);
                    requiredLetters.add(c);
                    break;
                case '^':
                    requiredLetters.add(c);
                    excludedPositions.computeIfAbsent(i, k -> new HashSet<>()).add(c);
                    break;
                case '-':
                    if (!requiredLetters.contains(c)) {
                        absentLetters.add(c);
                    }
                    break;
            }
        }
    }

    public String getHint() {
        if (gameOver) {
            return null;
        }

        List<String> candidates = new ArrayList<>(dictionary.getWords());

        candidates.removeIf(word -> {
            for (char c : absentLetters) {
                if (word.indexOf(c) >= 0) return true;
            }
            return false;
        });

        candidates.removeIf(word -> {
            for (Map.Entry<Integer, Character> entry : exactPositions.entrySet()) {
                if (word.charAt(entry.getKey()) != entry.getValue()) return true;
            }
            return false;
        });

        candidates.removeIf(word -> {
            for (Map.Entry<Integer, Set<Character>> entry : excludedPositions.entrySet()) {
                if (entry.getValue().contains(word.charAt(entry.getKey()))) return true;
            }
            return false;
        });

        candidates.removeIf(word -> {
            Set<Character> letters = dictionary.getLettersSet(word);
            return !letters.containsAll(requiredLetters);
        });

        candidates.removeAll(guesses);
        candidates.removeAll(hints);

        if (candidates.isEmpty()) {
            return null;
        }

        String hint = candidates.get(new Random().nextInt(candidates.size()));
        hints.add(hint);
        return hint;
    }

    private boolean isValidGuess(String word) {
        if (word == null || word.length() != WORD_LENGTH) {
            return false;
        }
        if (!normalizer.isRussianWord(word)) {
            return false;
        }
        return dictionary.contains(word);
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }

    public List<String> getGuesses() {
        return new ArrayList<>(guesses);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWon() {
        return won;
    }

    public Set<Character> getAbsentLetters() {
        return new HashSet<>(absentLetters);
    }

    public Set<Character> getRequiredLetters() {
        return new HashSet<>(requiredLetters);
    }

    public Map<Integer, Character> getExactPositions() {
        return new HashMap<>(exactPositions);
    }
}