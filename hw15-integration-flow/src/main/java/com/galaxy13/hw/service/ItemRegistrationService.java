package com.galaxy13.hw.service;

import com.galaxy13.hw.model.BidItem;
import com.galaxy13.hw.model.RegisteredItem;

public interface ItemRegistrationService {
    RegisteredItem register(BidItem item);
}
