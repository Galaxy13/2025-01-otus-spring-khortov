package com.galaxy13.hw;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
@EnableMongock
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
