package com.galaxy13.hw.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
public final class CommentDto {

    private final long id;

    private final String text;

    private final long bookId;

    private boolean isEditAllowed;

    public CommentDto(final long id, final String text, final long bookId) {
        this.id = id;
        this.text = text;
        this.bookId = bookId;
        this.isEditAllowed = false;
    }
}
