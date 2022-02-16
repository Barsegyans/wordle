package com.serbar.wordle.exceptions;

public class HashNotExistException extends Exception {
    public HashNotExistException(String word) {
        super("Hash " + word + " is not exist");
    }
}
