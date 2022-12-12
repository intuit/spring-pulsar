package com.intuit.spring.pulsar.client

import com.intuit.spring.pulsar.client.config.PulsarClientConfig

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

    fun getNetworkConfig(): PulsarClientConfig.PulsarNetworkConfig {
        return PulsarClientConfig.PulsarNetworkConfig(
            connectionTimeout = networkDuration,
            keepAliveInterval = networkDuration,
            operationTimeout = networkDuration,
            defaultBackoffInterval = networkDuration,
            maxBackoffInterval = networkDuration,
            requestTimeout = networkDuration,
            useTcpNoDelay = false
        )
    }

    fun getTlsConfig(): PulsarClientConfig.PulsarTlsConfig {
        return PulsarClientConfig.PulsarTlsConfig(
            useTls = useTls,
            tlsAllowInsecureConnection = tlsAllowInsecureConnection,
            tlsHostnameVerificationEnable = tlsHostnameVerificationEnable,
            tlsTrustCertsFilePath = tlsTrustCertsFilePath
        )
    }

    fun getRequestConfig(): PulsarClientConfig.PulsarRequestConfig {
        return PulsarClientConfig.PulsarRequestConfig(
            maxLookupRequest = maxLookupRequest,
            concurrentLookupRequest = concurrentLookupRequest,
            maxNumberOfRejectedRequestPerConnection = maxNumberOfRejectedRequestPerConnection
        )
    }

    fun getThreadPoolConfig(): PulsarClientConfig.PulsarThreadPoolConfig {
        return PulsarClientConfig.PulsarThreadPoolConfig(
            numIoThreads = numIoThreads,
            numListenerThreads = numListenerThreads
        )
    }

    fun getAuthConfig(): PulsarClientConfig.PulsarAuthConfig {
        return PulsarClientConfig.PulsarAuthConfig(
            userName = userName,
            password = password
        )
    }

    fun getNominalClientConfig(): PulsarClientConfig {
        return PulsarClientConfig(serviceUrl, auth = getAuthConfig())
    }
}
