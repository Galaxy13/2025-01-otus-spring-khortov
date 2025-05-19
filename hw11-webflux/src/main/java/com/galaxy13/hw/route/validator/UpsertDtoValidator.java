package com.galaxy13.hw.route.validator;

import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
public class UpsertDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AuthorUpsertDto.class.equals(clazz) || GenreUpsertDto.class.equals(clazz) ||
                BookUpsertDto.class.equals(clazz) || CommentUpsertDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Field[] dtoFields = target.getClass().getDeclaredFields();
        for (Field dtoField : dtoFields) {
            Annotation[] annotations = dtoField.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                switch (annotation) {
                    case NotNull ignored -> ValidationUtils.rejectIfEmpty(errors,
                            dtoField.getName(),
                            "validation.%s.required".formatted(dtoField.getName()));
                    case NotBlank ignored -> ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                            dtoField.getName(), "validation.%s.requiredNotBlank".formatted(dtoField.getName()));
                    case NotEmpty ignored -> ValidationUtils.rejectIfEmptyOrWhitespace(
                            errors, dtoField.getName(), "validation.%s.requiredNotEmpty".formatted(dtoField.getName()));
                    default -> {
                    }
                }
            }
        }
    }
}
