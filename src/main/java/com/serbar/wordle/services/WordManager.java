package com.serbar.wordle.services;

import com.serbar.wordle.exceptions.HashNotExistException;
import com.serbar.wordle.exceptions.WordNotExistException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WordManager {
    private final static Logger logger = LoggerFactory.getLogger(WordManager.class);

    private final WordLoader wordLoader;
    private final Random random = new Random();

    private final HashMap<String, String> hashToWord = new HashMap<>();

    private Set<String> wordSet = new HashSet<>();
    private List<String> wordList = new ArrayList<>();

    public volatile Optional<String> wordOfDay = Optional.empty();
    public AtomicInteger inc = new AtomicInteger(0);

    public WordManager(WordLoader wordLoader) {
        this.wordLoader = wordLoader;
    }

    public void checkWord(String word) throws WordNotExistException {
        if (!this.wordSet.contains(word.toLowerCase())) {
            throw new WordNotExistException(word);
        }
    }

    public String getRandomWord() {
        return wordList.get(random.nextInt(wordList.size()));
    }

    public String getWordOfDay() {
        if (wordOfDay.isEmpty()) {
            setWordOfDay();
        }
        return wordOfDay.get();
    }

    public String changeWordOfDay(Optional<String> wordO) throws WordNotExistException {
        if (wordO.isPresent()) {
            checkWord(wordO.get());
            wordOfDay = wordO;
            return wordO.get();
        } else {
            inc.incrementAndGet();
            String word = wordO.orElse(generateWordOfDay());
            wordOfDay = Optional.of(word);
            return word;
        }
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void fillLetters() throws IOException {
        logger.info("Start to load word");
        wordSet = wordLoader.loadWords();
        wordList = wordSet.stream().toList();
        logger.info("Load {} words", wordList.size());
    }

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void setWordOfDay() {
        inc.set(0);
        wordOfDay = Optional.of(generateWordOfDay());
    }

    private String generateWordOfDay() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        int index = now.getDayOfYear() * now.getYear();
        return wordList.get(index % wordList.size() + inc.get());
    }

    public String generateRandomHash() {
        String randomWord = getRandomWord();
        String hash = RandomStringUtils.random(15, true, true);
        hashToWord.put(hash, randomWord);
        return hash;
    }

    public String getWordByHash(String expectedWordHash) throws HashNotExistException {
        return Optional.ofNullable(hashToWord.get(expectedWordHash))
                .orElseThrow(() -> new HashNotExistException(expectedWordHash));
    }
}
