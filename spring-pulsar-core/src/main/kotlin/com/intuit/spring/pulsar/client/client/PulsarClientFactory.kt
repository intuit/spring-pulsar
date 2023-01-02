package com.intuit.spring.pulsar.client.client

import com.intuit.spring.pulsar.client.config.PulsarClientConfig
import com.intuit.spring.pulsar.client.constant.AuthConstants
import com.intuit.spring.pulsar.client.utils.parseDuration
import mu.KotlinLogging
import org.apache.pulsar.client.api.AuthenticationFactory
import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.impl.auth.AuthenticationBasic
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy

/**
 * Client factory class. Builds client objects
 * with the config that is provided. Keeps the client objects
 * in cache and provides accessor to get these cached
 * client objects
 */
@Component
class PulsarClientFactory(
    clientConfig: PulsarClientConfig
) : IPulsarClientFactory {

    /**
     * Create client object from the client configuration. If client cannot be created with
     * the given config, Pulsar will throw a RuntimeException
     */
    private val logger = KotlinLogging.logger {}
    private var pulsarClientBuilder: ClientBuilder = PulsarClient.builder()
    private val pulsarClient: PulsarClient = buildClient(clientConfig)

    /**
     * Close all resources before the bean gets destroyed.
     * Calls PulsarClient.close() to close all available
     * Consumers and Producers. Also shuts down the client
     * connection.
     */
    @PreDestroy
    fun close() {
        try {
            pulsarClient.close()
        } catch (exception: Exception) {
            logger.error { "Exception caught while closing client: ${exception.stackTraceToString()}" }
        }
    }

    /**
     * Returns client object
     */
    override fun getClient(): PulsarClient {
        return pulsarClient
    }

    /**
     * Populates ClientBuilder with client related configs
     */
    private fun withClientConfig(clientConfig: PulsarClientConfig) {
        pulsarClientBuilder.serviceUrl(clientConfig.serviceUrl)
        clientConfig.statsInterval.let {
            pulsarClientBuilder.statsInterval(
                parseDuration(clientConfig.statsInterval).toMillis(),
                TimeUnit.MILLISECONDS
            )
        }
        withTls(clientConfig.tls)
        withThreadPool(clientConfig.threadPool)
        withNetwork(clientConfig.network)
        withRequest(clientConfig.request)
        withAuth(clientConfig.auth)
    }

    /**
     * Populates ClientBuilder with network related configs
     */
    private fun withNetwork(network: PulsarClientConfig.PulsarNetworkConfig) {
        network.connectionTimeout.let {
            pulsarClientBuilder.connectionTimeout(
                parseDuration(network.connectionTimeout).toSeconds().toInt(),
                TimeUnit.SECONDS
            )
        }
        network.keepAliveInterval.let {
            pulsarClientBuilder.keepAliveInterval(
                parseDuration(network.keepAliveInterval).toSeconds().toInt(),
                TimeUnit.SECONDS
            )
        }
        network.operationTimeout.let {
            pulsarClientBuilder.operationTimeout(
                parseDuration(network.operationTimeout).toSeconds().toInt(),
                TimeUnit.SECONDS
            )
        }
        network.useTcpNoDelay.let { pulsarClientBuilder.enableTcpNoDelay(network.useTcpNoDelay) }
        network.defaultBackoffInterval.let {
            pulsarClientBuilder.startingBackoffInterval(
                parseDuration(network.defaultBackoffInterval).toMillis(),
                TimeUnit.MILLISECONDS
            )
        }
        network.maxBackoffInterval.let {
            pulsarClientBuilder.maxBackoffInterval(
                parseDuration(network.maxBackoffInterval).toMillis(),
                TimeUnit.MILLISECONDS
            )
        }
    }

    /**
     * Populates ClientBuilder with request related configs
     */
    private fun withRequest(request: PulsarClientConfig.PulsarRequestConfig) {
        request.maxLookupRequest
            .let { pulsarClientBuilder.maxLookupRequests(request.maxLookupRequest) }
        request.concurrentLookupRequest
            .let { pulsarClientBuilder.maxConcurrentLookupRequests(request.concurrentLookupRequest) }
        request.maxNumberOfRejectedRequestPerConnection
            .let {
                pulsarClientBuilder.maxNumberOfRejectedRequestPerConnection(
                    request.maxNumberOfRejectedRequestPerConnection
                )
            }
    }

    /**
     * Populates ClientBuilder with thread pool related configs
     */
    private fun withThreadPool(threadPool: PulsarClientConfig.PulsarThreadPoolConfig) {
        threadPool.numIoThreads.let { pulsarClientBuilder.ioThreads(threadPool.numIoThreads) }
        threadPool.numListenerThreads
            .let { pulsarClientBuilder.listenerThreads(threadPool.numListenerThreads) }
    }

    /**
     * Populates ClientBuilder with tls related configs
     */
    private fun withTls(tls: PulsarClientConfig.PulsarTlsConfig) {
        tls.useTls.let { pulsarClientBuilder.enableTls(tls.useTls) }
        tls.tlsAllowInsecureConnection
            .let { pulsarClientBuilder.allowTlsInsecureConnection(tls.tlsAllowInsecureConnection) }
        tls.tlsHostnameVerificationEnable
            .let { pulsarClientBuilder.enableTlsHostnameVerification(tls.tlsHostnameVerificationEnable) }
        tls.tlsTrustCertsFilePath
            .let { pulsarClientBuilder.tlsTrustCertsFilePath(tls.tlsTrustCertsFilePath) }
    }

    /**
     * Populates ClientBuilder with authentication related configs
     */
    private fun withAuth(auth: PulsarClientConfig.PulsarAuthConfig) {
        val authMap = mutableMapOf<String, String>()
        authMap[AuthConstants.userId] = auth.userName
        authMap[AuthConstants.password] = auth.password

        pulsarClientBuilder.authentication(
            AuthenticationFactory.create(
                AuthenticationBasic::class.java.name,
                authMap
            )
        )
    }

    /**
     * Populates the pulsar client builder using client config that is provided.
     * Then uses pulsar client builder to build pulsar client.
     */
    private fun buildClient(clientConfig: PulsarClientConfig): PulsarClient {
        logger.info { "Creating client with config $clientConfig" }
        withClientConfig(clientConfig)
        val pulsarClient =  pulsarClientBuilder.build()
        logger.info { "Client created successfully $pulsarClient" }
        return pulsarClient
    }
}


