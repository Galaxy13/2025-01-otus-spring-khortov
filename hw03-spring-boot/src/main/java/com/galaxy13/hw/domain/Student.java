package com.galaxy13.hw.domain;

public record Student(String firstName, String lastName) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
