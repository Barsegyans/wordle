package com.serbar.wordle.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class WordManager {
    private final static Logger logger = LoggerFactory.getLogger(WordManager.class);

    private final WordLoader wordLoader;
    private final Random random = new Random();

    private Set<String> wordSet = new HashSet<>();
    private List<String> wordList = new ArrayList<>();

    public WordManager(WordLoader wordLoader) {
        this.wordLoader = wordLoader;
    }

    public boolean checkWord(String word) {
        return this.wordSet.contains(word);
    }

    public String getRandomWord() {
        return wordList.get(random.nextInt(wordList.size()));
    }

    public String getWordOfDay() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        int index = now.getDayOfYear() * now.getYear();
        return wordList.get(index % wordList.size());
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void fillLetters() throws IOException {
        logger.info("Start to load word");
        wordSet = wordLoader.loadWords();
        wordList = wordSet.stream().toList();
        logger.info("Load {} words", wordList.size());
    }
}
