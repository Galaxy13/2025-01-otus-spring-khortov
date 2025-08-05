package com.galaxy13.hw.service;

import com.galaxy13.hw.model.CheckedItem;
import com.galaxy13.hw.model.RegisteredItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemCheckServiceImpl implements ItemCheckService {


    @Override
    public CheckedItem check(RegisteredItem item) {
        log.info("Checking item: {} in progress...", item.name());
        delay();
        log.info("Checking item: {} completed...", item.name());
        return new CheckedItem(item.name(), item.id());
    }

    private void delay(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
