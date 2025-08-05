package com.galaxy13.hw;

import com.galaxy13.hw.model.BidItem;
import com.galaxy13.hw.model.RegisteredItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@SpringIntegrationTest
@SpringBootTest
public class ChannelFlowTest {

    @Autowired
    @Qualifier("inputChannel")
    MessageChannel inputChannel;

    @Autowired
    @Qualifier("registrationChannel")
    PublishSubscribeChannel registrationChannel;

    @Autowired
    @Qualifier("checkedItemsChannel")
    MessageChannel checkedItemsChannel;

    @Autowired
    @Qualifier("documentChannel")
    PublishSubscribeChannel documentChannel;

    private final BlockingQueue<Message<?>> messages = new LinkedBlockingQueue<>();

    @BeforeEach
    public void setup() {
        registrationChannel.subscribe(messages::add);
    }

    @Test
    public void bidToRegistrationTest() throws InterruptedException {
        List<BidItem> items = TestDataProvider.getBidItems();
        inputChannel.send(new GenericMessage<>(items));

        for (BidItem bidItem : items) {
            Message<?> message = messages.poll(5, TimeUnit.SECONDS);
            assertThat(message).isNotNull();
            assertThat(message.getPayload()).isNotNull();
            assertThat(message.getPayload()).isInstanceOf(RegisteredItem.class);
            RegisteredItem item = (RegisteredItem) message.getPayload();
            assertThat(item).isNotNull();
            assertThat(item).matches(other -> other.name().equals(bidItem.itemName()));
        }
    }
}
