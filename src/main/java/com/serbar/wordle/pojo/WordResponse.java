package com.serbar.wordle.pojo;

import javax.validation.constraints.Size;
import java.util.List;

public record WordResponse(@Size(min = 5, max = 5) List<LetterResponse> letters) {
}
