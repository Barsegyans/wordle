package com.serbar.wordle.controllers;

import com.serbar.wordle.exceptions.WordNotExistException;
import com.serbar.wordle.pojo.WordHashResponse;
import com.serbar.wordle.pojo.WordResponse;
import com.serbar.wordle.services.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.websocket.server.PathParam;
import java.security.InvalidKeyException;

@RestController
@RequestMapping("/api/v1/word/")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/random")
    public WordHashResponse getRandomWord()
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException
    {
        return new WordHashResponse(wordService.generateRandomWordHash());
    }

    @GetMapping("/check")
    public WordResponse check(@RequestParam String word) throws WordNotExistException {
        return new WordResponse(wordService.processWord(word));
    }

    @GetMapping("/{expected_word_hash}/check")
    public WordResponse check(@PathParam("expected_word_hash") String wordHash, @RequestParam String word)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, WordNotExistException
    {
        return new WordResponse(wordService.processHashWord(wordHash, word));
    }
}