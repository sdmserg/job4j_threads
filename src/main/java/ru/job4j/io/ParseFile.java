package ru.job4j.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(final File file) {
        this.file = file;
    }

    public String getContent(Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        int data;
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8)
        )) {
            while ((data = input.read()) != -1) {
                char character = (char) data;
                     if (filter.test(character)) {
                        output.append(character);
                    }
                }
            }
        return output.toString();
    }
}