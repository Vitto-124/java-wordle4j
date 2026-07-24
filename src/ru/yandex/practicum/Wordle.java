package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(String[] args) {
        try (PrintWriter log = createLog();
             Scanner scanner = new Scanner(System.in)) {

            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.loadDictionary(DICTIONARY_FILE);

            WordleGame game = new WordleGame(dictionary, log);

            System.out.println("Добро пожаловать в Wordle!");
            System.out.println("Угадайте слово из 5 букв. У вас 6 попыток.");
            System.out.println("Для подсказки нажмите Enter без ввода.");
            System.out.println();

            while (!game.isGameOver()) {
                System.out.println("Осталось попыток: " + game.getSteps());
                System.out.print("Введите слово: ");
                String input = scanner.nextLine();

                if (input.trim().isEmpty()) {
                    String hint = game.getHint();
                    if (hint != null) {
                        System.out.println("Подсказка: " + hint);
                    } else {
                        System.out.println("Нет подходящих подсказок.");
                    }
                    continue;
                }

                try {
                    String result = game.makeGuess(input);
                    System.out.println(result);

                    if (!game.isGameOver() && result.length() == 5) {
                        System.out.println("+ = правильная позиция, ^ = есть в слове, - = нет");
                    }
                } catch (GameException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            if (game.isWon()) {
                System.out.println("Поздравляем! Вы угадали слово!");
            } else {
                System.out.println("Загаданное слово: " + game.getAnswer());
            }

        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static PrintWriter createLog() throws IOException {
        return new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(LOG_FILE, true),
                        StandardCharsets.UTF_8
                ),
                true
        );
    }
}
