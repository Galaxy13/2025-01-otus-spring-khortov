package com.galaxy13.hw.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class CommentDto {
    private final String id;

    private final String text;

    private final String bookId;
}
