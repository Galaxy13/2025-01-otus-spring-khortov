package com.galaxy13.hw;

import com.galaxy13.hw.model.ActDocument;
import com.galaxy13.hw.model.BidItem;
import com.galaxy13.hw.model.CheckedItem;
import com.galaxy13.hw.model.RegisteredItem;
import com.galaxy13.hw.service.ItemIdProvider;
import com.galaxy13.hw.service.WorkProcessGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringIntegrationTest
@SpringBootTest
class ChannelFlowTest {

    @Autowired
    private WorkProcessGateway gateway;

    @Autowired
    private ItemIdProvider itemIdProvider;

    @Autowired
    @Qualifier("registrationChannel")
    private PublishSubscribeChannel registrationChannel;

    @Autowired
    @Qualifier("checkedItemsChannel")
    private MessageChannel checkedItemsChannel;

    @Autowired
    @Qualifier("documentChannel")
    private PublishSubscribeChannel documentChannel;

    private final List<Message<?>> registeredItemMessages = new ArrayList<>();

    private final List<Message<?>> checkedItemMessages = new ArrayList<>();

    private final List<Message<?>> documentMessages = new ArrayList<>();

    @BeforeEach
    public void setup() {
        registeredItemMessages.clear();
        checkedItemMessages.clear();
        documentMessages.clear();
        itemIdProvider.reset();

        registrationChannel.subscribe(registeredItemMessages::add);
        QueueChannel queueCheckedItemsChannel = (QueueChannel) checkedItemsChannel;
        queueCheckedItemsChannel.addInterceptor(new ChannelInterceptor() {
            @Override
            public Message<?> postReceive(Message<?> message, MessageChannel channel) {
                checkedItemMessages.add(message);
                return ChannelInterceptor.super.postReceive(message, channel);
            }
        });

        documentChannel.subscribe(documentMessages::add);
    }

    @Test
    void testItemCheckThroughGateway() {
        ActDocument expectedDocument = TestDataProvider.getActDocument();

        ActDocument document = gateway.process(TestDataProvider.getBidItems());
        assertThat(document).isNotNull();
        assertThat(document).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expectedDocument);
    }

    @Test
    void testWorkFlowsThroughChannels() {
        gateway.process(TestDataProvider.getBidItems());

        List<RegisteredItem> expectedRegisteredItems = TestDataProvider.getRegisteredItems();
        assertThat(registeredItemMessages).hasSize(expectedRegisteredItems.size());
        for (Message<?> registeredMessage : registeredItemMessages) {
            assertThat(registeredMessage.getPayload()).isNotNull();
            RegisteredItem registeredItem = (RegisteredItem) registeredMessage.getPayload();
            assertThat(registeredItem).isIn(expectedRegisteredItems);
        }

        List<CheckedItem> expectedCheckedItems = TestDataProvider.getCheckedItems();
        assertThat(checkedItemMessages).hasSize(expectedCheckedItems.size());
        for (Message<?> checkedItemMessage : checkedItemMessages) {
            assertThat(checkedItemMessage.getPayload()).isNotNull();
            CheckedItem checkedItem = (CheckedItem) checkedItemMessage.getPayload();
            assertThat(checkedItem).isIn(expectedCheckedItems);
        }

        Message<?> documentMessage = documentMessages.getFirst();
        assertThat(documentMessage.getPayload()).isNotNull();
        ActDocument document = (ActDocument) documentMessage.getPayload();
        assertThat(document).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(TestDataProvider.getActDocument());
    }

    @Test
    void testMultipleBids() {
        List<BidItem> bid1 = List.of(new BidItem("item1"),
                new BidItem("item2"), new BidItem("item3"));
        List<BidItem> bid2 = List.of(new BidItem("item4"), new BidItem("item5"));

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<ActDocument> future1 = executor.submit(() -> gateway.process(bid1));
            Future<ActDocument> future2 = executor.submit(() -> gateway.process(bid2));
            ActDocument document1 = future1.get(15, TimeUnit.SECONDS);
            ActDocument document2 = future2.get(15, TimeUnit.SECONDS);

            assertThat(document1).isNotNull();
            assertThat(document2).isNotNull();

            assertThat(document1.getItems()).hasSize(bid1.size());
            assertThat(document2.getItems()).hasSize(bid2.size());

            assertThat(document1.getItems()).extracting(CheckedItem::name)
                    .containsExactlyInAnyOrder(bid1.stream().map(BidItem::itemName).toArray(String[]::new));
            assertThat(document2.getItems()).extracting(CheckedItem::name)
                    .containsExactlyInAnyOrder(bid2.stream().map(BidItem::itemName).toArray(String[]::new));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
