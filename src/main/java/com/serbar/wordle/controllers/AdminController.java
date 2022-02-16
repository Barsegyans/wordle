package com.serbar.wordle.controllers;

import com.serbar.wordle.exceptions.HashNotExistException;
import com.serbar.wordle.exceptions.WordNotExistException;
import com.serbar.wordle.services.WordManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {

    private final WordManager wordManager;

    public AdminController(WordManager wordManager) {
        this.wordManager = wordManager;
    }

    @GetMapping(value = "/word_of_day", produces = "text/plain;charset=UTF-8")
    public String getWordOfDay() {
        return wordManager.getWordOfDay();
    }

    @GetMapping(value = "/word_by_hash", produces = "text/plain;charset=UTF-8")
    public String getWordOfDay(@RequestParam("hash") String hash) throws HashNotExistException {
        return wordManager.getWordByHash(hash);
    }

    @GetMapping(value = "/change_word_of_day", produces = "text/plain;charset=UTF-8")
    public String changeWordOfDay(@RequestParam Optional<String> word) throws WordNotExistException {
        return wordManager.changeWordOfDay(word);
    }
}