package com.serbar.wordle.services;

import com.serbar.wordle.exceptions.HashNotExistException;
import com.serbar.wordle.exceptions.WordNotExistException;
import com.serbar.wordle.pojo.LetterResponse;
import com.serbar.wordle.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class WordService {
    private final static Logger logger = LoggerFactory.getLogger(WordService.class);

    private final WordManager wordManager;

    public WordService(WordManager wordManager) {
        this.wordManager = wordManager;
    }

    public String generateRandomWordHash() {
        return wordManager.generateRandomHash();
    }

    public List<LetterResponse> processWord(String word) throws WordNotExistException {
        String wordOfDay = wordManager.getWordOfDay();
        return processWord(wordOfDay, word);
    }

    public List<LetterResponse> processHashWord(String expectedWordHash, String word) throws
            WordNotExistException,
            HashNotExistException
    {
        String expectedWord = wordManager.getWordByHash(expectedWordHash);
        return processWord(expectedWord, word);
    }

    private List<LetterResponse> processWord(String expectedWord, String word) throws WordNotExistException {
        wordManager.checkWord(word);

        if (expectedWord.length() != word.length() || word.length() != 5) {
            logger.info("Words length not equal (expected {}, given {})", expectedWord, word);
            throw new RuntimeException("Words length not equal");
        }
        List<LetterResponse> result = new ArrayList<>();
        String expectedWordLc = expectedWord.toLowerCase();
        String wordLc = word.toLowerCase(Locale.ROOT);

        for (int i = 0; i < wordLc.length(); i++) {
            if (wordLc.charAt(i) == expectedWordLc.charAt(i)) {
                result.add(new LetterResponse(wordLc.charAt(i), State.PRESENT_IN_PlACE));
            } else if (expectedWordLc.contains("" + wordLc.charAt(i))) {
                result.add(new LetterResponse(wordLc.charAt(i), State.PRESENT));
            } else {
                result.add(new LetterResponse(wordLc.charAt(i), State.NOT_PRESENT));
            }
        }
        return result;
    }
}
