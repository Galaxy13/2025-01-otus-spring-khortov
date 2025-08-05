package com.galaxy13.hw.service;

import com.galaxy13.hw.model.BidItem;
import com.galaxy13.hw.model.RegisteredItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRegistrationServiceImpl implements ItemRegistrationService {

    private final ItemIdProvider itemIdProvider;

    @Override
    public RegisteredItem register(BidItem item) {
        log.info("Registering item {}", item.itemName());
        delay();
        RegisteredItem regItem = new RegisteredItem(item.itemName(), itemIdProvider.retrieveItemId());
        log.info("Registering item {} completed. Set new id: {}", regItem.name(), regItem.id());
        return regItem;
    }

    private void delay() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
