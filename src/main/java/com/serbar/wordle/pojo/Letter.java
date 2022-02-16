package com.serbar.wordle.pojo;

import java.util.List;
import java.util.Set;

public record Letter(Set<Letter> nextLetters) {
}
