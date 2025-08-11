package com.galaxy13.hw.model;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ActDocument {
    private final String title;

    private final UUID id;

    private final List<CheckedItem> items;

    public ActDocument(String title, List<CheckedItem> items) {
        this.title = title;
        this.id = UUID.randomUUID();
        this.items = items;
    }
}
