package com.intuit.spring.pulsar.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [
    "com.intuit.spring.pulsar.kotlin.sample01",
    "com.intuit.spring.pulsar.kotlin.sample02",
    "com.intuit.spring.pulsar.client"
])
class SampleApp

fun main(args: Array<String>) {
    runApplication<SampleApp>(*args)
}