package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.model.Comment;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CommentDtoMapper implements Converter<Comment, CommentDto> {

    @Override
    public CommentDto convert(Comment source) {
        return new CommentDto(source.getId(), source.getText(), source.getBook().getId());
    }
}
