package com.galaxy13.hw.service;

import com.galaxy13.hw.model.ActDocument;
import com.galaxy13.hw.model.CheckedItem;

import java.util.List;

public interface ItemFinalizeService {
    ActDocument finalizeWork(List<CheckedItem> items);
}
