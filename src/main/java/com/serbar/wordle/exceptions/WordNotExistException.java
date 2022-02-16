package com.serbar.wordle.exceptions;

public class WordNotExistException extends Exception {
    public WordNotExistException(String word) {
        super("Word " + word + " is not exist");
    }
}
