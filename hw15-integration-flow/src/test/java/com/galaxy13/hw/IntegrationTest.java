package com.galaxy13.hw;

import com.galaxy13.hw.service.WorkProcessGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.MessageChannel;

@SpringIntegrationTest
@SpringBootTest
public class IntegrationTest {

    @Autowired
    WorkProcessGateway gateway;

    @Autowired
    @Qualifier("inputChannel")
    MessageChannel inputChannel;
}
