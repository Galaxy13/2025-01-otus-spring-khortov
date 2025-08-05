package com.galaxy13.hw.service;

import com.galaxy13.hw.model.ActDocument;
import com.galaxy13.hw.model.CheckedItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemFinalizeServiceImpl implements ItemFinalizeService {
    @Override
    public ActDocument finalizeWork(List<CheckedItem> items) {
        log.info("Creating final document from items...");
        delay();
        ActDocument document = new ActDocument("Act for %s items".formatted(items.size()), items);
        log.info("Finished creating final document: {}", document.getTitle());
        return document;
    }

    private void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
