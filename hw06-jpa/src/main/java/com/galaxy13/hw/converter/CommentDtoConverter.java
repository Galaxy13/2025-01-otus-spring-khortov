package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.CommentDto;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoConverter implements Converter<CommentDto> {
    @Override
    public String convertToString(CommentDto value) {
        return "Comment id: %d, book id: %d, text: %s".formatted(value.getId(), value.getBookId(), value.getText());
    }
}
