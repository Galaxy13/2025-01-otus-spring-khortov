package com.galaxy13.hw.dto.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class CommentDto {
    private final long id;

    private final String text;

    private final long bookId;
}
