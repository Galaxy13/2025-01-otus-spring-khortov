package com.galaxy13.hw.shell;

import com.galaxy13.hw.model.ActDocument;
import com.galaxy13.hw.model.BidItem;
import com.galaxy13.hw.service.WorkProcessGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Command(description = "Integration command")
@Slf4j
@RequiredArgsConstructor
public class IntegrationCommands {

    private final static List<String> ITEMS = List.of("tv", "smartphone", "fridge", "headphones", "camera", "sensor panel");

    private final WorkProcessGateway itemCheckService;

    @Command(command = "start_one", alias = "st", description = "Perform check of items")
    public String startJob(@Option(arity = CommandRegistration.OptionArity.ONE_OR_MORE) List<String> items) {
        log.info("Starting work process");
        ActDocument document = itemCheckService.process(items.stream().map(BidItem::new).toList());
        return "Initiator received document %s %s".formatted(document.getTitle(), document.getId());
    }

    @Command(command = "start_multiple", alias = "sm", description = "Start multiple jobs")
    public String startMultiple() {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < 5; i++) {
                Collection<BidItem> items = generateBidItems();
                log.info("Items accepted to check: {}", items);
                executor.submit(() -> itemCheckService.process(items));
            }
        }
        return "All jobs finished";
    }

    private static Collection<BidItem> generateBidItems() {
        List<BidItem> items = new ArrayList<>();
        for (int i = 0; i < RandomUtils.secure().randomInt(1, 10); ++i) {
            items.add(generateOrderItem());
        }
        return items;
    }

    private static BidItem generateOrderItem() {
        return new BidItem(ITEMS.get(RandomUtils.secure().randomInt(0, ITEMS.size())));
    }
}
