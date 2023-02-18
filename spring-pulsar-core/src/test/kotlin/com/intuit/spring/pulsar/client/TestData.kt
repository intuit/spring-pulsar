package com.intuit.spring.pulsar.client

import com.intuit.spring.pulsar.client.config.PulsarAuthConfig
import com.intuit.spring.pulsar.client.config.PulsarBasicAuthConfig
import com.intuit.spring.pulsar.client.config.PulsarClientConfig
import com.intuit.spring.pulsar.client.config.PulsarNetworkConfig
import com.intuit.spring.pulsar.client.config.PulsarRequestConfig
import com.intuit.spring.pulsar.client.config.PulsarThreadPoolConfig
import com.intuit.spring.pulsar.client.config.PulsarTlsConfig


object TestData {

    const val serviceUrl = "http://service/url/"
    const val statsInterval = 10000
    const val userName = "username"
    const val password = "password"
    const val useTls = false
    const val tlsAllowInsecureConnection = false
    const val tlsHostnameVerificationEnable = false
    const val tlsTrustCertsFilePath = "file/path"
    const val maxLookupRequest = 10
    const val concurrentLookupRequest = 10
    const val maxNumberOfRejectedRequestPerConnection = 10
    const val numIoThreads = 10
    const val numListenerThreads = 10
    const val networkDuration: String = "10000ms"

    fun getNetworkConfig(): PulsarNetworkConfig {
        return PulsarNetworkConfig(
            connectionTimeout = networkDuration,
            keepAliveInterval = networkDuration,
            operationTimeout = networkDuration,
            defaultBackoffInterval = networkDuration,
            maxBackoffInterval = networkDuration,
            requestTimeout = networkDuration,
            useTcpNoDelay = false
        )
    }

    fun getTlsConfig(): PulsarTlsConfig {
        return PulsarTlsConfig(
            useTls = useTls,
            tlsAllowInsecureConnection = tlsAllowInsecureConnection,
            tlsHostnameVerificationEnable = tlsHostnameVerificationEnable,
            tlsTrustCertsFilePath = tlsTrustCertsFilePath
        )
    }

    fun getRequestConfig(): PulsarRequestConfig {
        return PulsarRequestConfig(
            maxLookupRequest = maxLookupRequest,
            concurrentLookupRequest = concurrentLookupRequest,
            maxNumberOfRejectedRequestPerConnection = maxNumberOfRejectedRequestPerConnection
        )
    }

    fun getThreadPoolConfig(): PulsarThreadPoolConfig {
        return PulsarThreadPoolConfig(
            numIoThreads = numIoThreads,
            numListenerThreads = numListenerThreads
        )
    }

    fun getAuthConfig(): PulsarAuthConfig {
        return PulsarBasicAuthConfig(
            userName = userName,
            password = password
        )
    }

    fun getNominalClientConfig(): PulsarClientConfig {
        return PulsarClientConfig(serviceUrl, auth = getAuthConfig())
    }
}
