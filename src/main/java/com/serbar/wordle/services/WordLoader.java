package com.serbar.wordle.services;

import java.io.IOException;
import java.util.Set;

public interface WordLoader {
    Set<String> loadWords() throws IOException;
}
