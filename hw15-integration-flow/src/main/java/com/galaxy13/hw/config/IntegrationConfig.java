package com.galaxy13.hw.config;

import com.galaxy13.hw.service.ItemCheckService;
import com.galaxy13.hw.service.ItemFinalizeService;
import com.galaxy13.hw.service.ItemRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel inputChannel() {
        return MessageChannels.queue(5).getObject();
    }

    @Bean
    public PublishSubscribeChannel registrationChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    @Bean
    public MessageChannel checkedItemsChannel() {
        return MessageChannels.queue(20).getObject();
    }

    @Bean
    public PublishSubscribeChannel documentChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(1000).maxMessagesPerPoll(5);
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean
    public IntegrationFlow bidItemFlow(ItemRegistrationService itemRegistrationService) {
        return IntegrationFlow.from(inputChannel())
                .enrichHeaders(h -> h.headerFunction("correlationId", m -> m.getHeaders().getId()))
                .split()
                .handle(itemRegistrationService, "register")
                .channel("registrationChannel")
                .get();
    }

    @Bean
    public IntegrationFlow itemCheckFlow(ItemCheckService itemCheckService) {
        return IntegrationFlow.from("registrationChannel")
                .channel(MessageChannels.executor(taskExecutor()))
                .handle(itemCheckService, "check")
                .channel("checkedItemsChannel")
                .get();
    }

    @Bean
    public IntegrationFlow checkedItemsWorkFlow(ItemFinalizeService itemFinalizeService) {
        return IntegrationFlow.from("checkedItemsChannel")
                .aggregate(a -> a.correlationStrategy(m -> m.getHeaders().get("correlationId")))
                .handle(itemFinalizeService, "finalizeWork")
                .channel("documentChannel")
                .get();
    }
}
