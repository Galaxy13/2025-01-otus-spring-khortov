package com.galaxy13.hw.service;

import org.springframework.stereotype.Service;

@Service
public class ItemIdProviderImpl implements ItemIdProvider {

    private long counter = 0L;

    @Override
    public Long retrieveItemId() {
        return ++counter;
    }
}
