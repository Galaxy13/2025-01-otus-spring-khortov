package com.galaxy13.hw.dao.dto;

import com.galaxy13.hw.domain.Answer;
import com.opencsv.bean.AbstractCsvConverter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class AnswerCsvConverter extends AbstractCsvConverter {
    @Override
    public Object convertToRead(String s) throws CsvDataTypeMismatchException {
        var value = s.split("%");
        if (value.length != 2) {
            throw new CsvDataTypeMismatchException("Answer parsing exception");
        }
        return new Answer(value[0], Boolean.parseBoolean(value[1]));
    }
}
