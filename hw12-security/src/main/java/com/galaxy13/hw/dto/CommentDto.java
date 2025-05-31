package com.galaxy13.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CommentDto) obj;
        return this.id == that.id &&
                Objects.equals(this.text, that.text) &&
                this.bookId == that.bookId &&
                this.isEditAllowed == that.isEditAllowed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, bookId, isEditAllowed);
    }

    @Override
    public String toString() {
        return "CommentDto[" +
                "id=" + id + ", " +
                "text=" + text + ", " +
                "bookId=" + bookId + ", " +
                "isEditAllowed=" + isEditAllowed + ']';
    }

}
