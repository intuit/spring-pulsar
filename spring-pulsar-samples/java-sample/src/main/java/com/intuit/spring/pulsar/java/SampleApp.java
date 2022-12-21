package com.intuit.spring.pulsar.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.intuit.spring.pulsar.java.sample01",
        "com.intuit.spring.pulsar.java.sample02",
        "com.intuit.spring.pulsar.client"})
public class SampleApp {
    public static void main(String[] args) {
        SpringApplication.run(SampleApp.class, args);
    }
}
