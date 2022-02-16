package com.serbar.wordle.controllers;

import com.serbar.wordle.services.WordManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {

    private final WordManager wordManager;

    public AdminController(WordManager wordManager) {
        this.wordManager = wordManager;
    }

    @GetMapping("/word_of_day")
    public String getWordOfDay() {
        return wordManager.getWordOfDay();
    }
}