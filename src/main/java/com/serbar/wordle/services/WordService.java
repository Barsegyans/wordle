package com.serbar.wordle.services;

import com.serbar.wordle.exceptions.WordNotExistException;
import com.serbar.wordle.pojo.LetterResponse;
import com.serbar.wordle.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordService {
    private final static Logger logger = LoggerFactory.getLogger(WordService.class);

    private final WordManager wordManager;
    private final String key;
    private final Key aesKey;
    private final Cipher cipher;

    public WordService(WordManager wordManager, @Value("encrypt.key") String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException
    {
        this.wordManager = wordManager;
        this.key = key;
        this.aesKey = new SecretKeySpec(this.key.getBytes(), "AES");
        this.cipher = Cipher.getInstance("AES");
    }

    public String generateRandomWordHash() throws
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException
    {
        String text = wordManager.getRandomWord();

        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return new String(encrypted);
    }

    public List<LetterResponse> processWord(String word) throws WordNotExistException {
        String wordOfDay = wordManager.getWordOfDay();
        return processWord(wordOfDay, word);
    }

    public List<LetterResponse> processHashWord(String expectedWordHash, String word) throws
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            WordNotExistException
    {
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(expectedWordHash.getBytes()));
        return processWord(decrypted, word);
    }

    private List<LetterResponse> processWord(String expectedWord, String word) throws WordNotExistException {
        if (!wordManager.checkWord(word)) {
            throw new WordNotExistException(word);
        }
        if (expectedWord.length() != word.length() || word.length() != 5) {
            logger.info("Words length not equal (expected {}, given {})", expectedWord, word);
            throw new RuntimeException("Words length not equal");
        }
        List<LetterResponse> result = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == expectedWord.charAt(i)) {
                result.add(new LetterResponse(word.charAt(i), State.PRESENT_IN_PlACE));
            } else if (expectedWord.contains("" + word.charAt(i))) {
                result.add(new LetterResponse(word.charAt(i), State.PRESENT));
            } else {
                result.add(new LetterResponse(word.charAt(i), State.NOT_PRESENT));
            }
        }
        return result;
    }
}
