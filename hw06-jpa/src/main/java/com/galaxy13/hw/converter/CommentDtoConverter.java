package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.model.Comment;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CommentDtoConverter implements Converter<Comment, CommentDto> {

    @Override
    public CommentDto convert(@NonNull Comment source) {
        return new CommentDto(source.getId(), source.getText(), source.getBook().getId());
    }
}
