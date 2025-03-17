package com.galaxy13.hw.service;

import com.galaxy13.hw.domain.Student;
import com.galaxy13.hw.domain.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}
