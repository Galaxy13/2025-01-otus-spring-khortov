package com.galaxy13.hw.service;

import com.galaxy13.hw.model.ActDocument;
import com.galaxy13.hw.model.BidItem;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.Collection;

@MessagingGateway
public interface WorkProcessGateway {

    @Gateway(requestChannel = "inputChannel", replyChannel = "documentChannel")
    ActDocument process(Collection<BidItem> items);
}
