package com.galaxy13.hw;

import com.galaxy13.hw.service.TestRunnerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");

        var runner = context.getBean(TestRunnerService.class);
        runner.run();
    }
}