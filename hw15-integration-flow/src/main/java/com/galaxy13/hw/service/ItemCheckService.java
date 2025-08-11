package com.galaxy13.hw.service;

import com.galaxy13.hw.model.CheckedItem;
import com.galaxy13.hw.model.RegisteredItem;

public interface ItemCheckService {
    CheckedItem check(RegisteredItem item);
}
