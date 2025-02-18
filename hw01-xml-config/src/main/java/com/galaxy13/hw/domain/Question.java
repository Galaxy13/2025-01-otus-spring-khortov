package com.galaxy13.hw.domain;

import java.util.List;

public record Question(String text, List<Answer> answers) {
}
