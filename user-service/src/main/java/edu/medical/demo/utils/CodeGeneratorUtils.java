package edu.medical.demo.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Утилитный класс для генерации кода для регистрации и действий над пользователями.
 */
@UtilityClass
public class CodeGeneratorUtils {
    /**
     * Метод для генерации случайного 6-значного числового кода.
     *
     * @return шестизначный код из уникальных цифр.
     */
    public static String generateSixDigitCode() {
        final Random random = new Random();
        return IntStream.generate(() -> random.nextInt(10))
                .distinct()
                .limit(6)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
