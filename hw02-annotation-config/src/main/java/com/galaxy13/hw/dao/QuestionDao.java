package com.galaxy13.hw.dao;

import com.galaxy13.hw.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
