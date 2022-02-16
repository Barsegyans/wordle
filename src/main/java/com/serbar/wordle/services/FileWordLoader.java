package com.serbar.wordle.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileWordLoader implements WordLoader {

    public final String filePath;

    public FileWordLoader(@Value("${word_base_file_path}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Set<String> loadWords() throws IOException {
        return new BufferedReader(new InputStreamReader(new ClassPathResource(filePath).getInputStream()))
                .lines()
                .filter(FileWordLoader::checkSize)
                .filter(FileWordLoader::wordIsCorrect)
                .collect(Collectors.toSet());
    }

    private static boolean checkSize(String s) {
        return s.length() == 5;
    }

    private static boolean wordIsCorrect(String word) {
        return word.matches("[а-яёА-ЯЁ]{5}");
    }
}
