package ru.job4j.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SaveFile {
    private final File file;

    public SaveFile(File file) {
        this.file = file;
    }

    public void saveContent(String content) throws IOException {
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
            output.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }
}