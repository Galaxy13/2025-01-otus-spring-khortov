package com.galaxy13.hw;

import com.galaxy13.hw.model.ActDocument;
import com.galaxy13.hw.model.BidItem;
import com.galaxy13.hw.model.CheckedItem;
import com.galaxy13.hw.model.RegisteredItem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestDataProvider {
    private static final List<String> testData = List.of("TestItem1", "TestItem2", "TestItem3",
            "TestItem4");

    public static List<BidItem> getBidItems(){
        return testData.stream().map(BidItem::new).collect(Collectors.toList());
    }

    public static List<RegisteredItem> getRegisteredItems() {
        return IntStream.range(0, testData.size())
                .mapToObj(index -> new RegisteredItem(testData.get(index), (long) (index + 1))).toList();
    }

    public static List<CheckedItem> getCheckedItems() {
        return getRegisteredItems().stream().map(item -> new CheckedItem(item.name(), item.id())).toList();
    }

    public static ActDocument getActDocument() {
        return new ActDocument("Act for 4 items", getCheckedItems());
    }
}
