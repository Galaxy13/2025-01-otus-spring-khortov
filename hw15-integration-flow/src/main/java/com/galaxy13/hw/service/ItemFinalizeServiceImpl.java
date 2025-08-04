package com.galaxy13.hw.service;

import com.galaxy13.hw.model.ActDocument;
import com.galaxy13.hw.model.CheckedItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemFinalizeServiceImpl implements ItemFinalizeService {
    @Override
    public ActDocument finalizeWork(List<CheckedItem> items) {
        return null;
    }
}
