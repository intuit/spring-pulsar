package com.intuit.spring.pulsar.sample02

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["com.intuit.spring.pulsar.sample02", "com.intuit.spring.pulsar.client"])
class SampleApp

fun main() {
    SpringApplication.run(SampleApp::class.java)
}