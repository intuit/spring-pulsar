package com.intuit.spring.pulsar.client.config

import com.intuit.spring.pulsar.client.TestData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PulsarClientConfigTest {

    @Test
    fun `validate auth config creation with all properties set`() {
        val auth:PulsarBasicAuthConfig = TestData.getAuthConfig() as PulsarBasicAuthConfig
        assertEquals(TestData.userName, auth.userName)
        assertEquals(TestData.password, auth.password)

        auth.password = "pass"
        auth.userName = "user"

        assertEquals("user", auth.userName)
        assertEquals("pass", auth.password)
    }

    @Test
    fun `validate request config creation with all properties set`() {
        val request = TestData.getRequestConfig()
        assertEquals(TestData.maxLookupRequest, request.maxLookupRequest)
        assertEquals(TestData.concurrentLookupRequest, request.concurrentLookupRequest)
        assertEquals(TestData.maxNumberOfRejectedRequestPerConnection, request.maxNumberOfRejectedRequestPerConnection)

        request.maxLookupRequest = 10
        request.concurrentLookupRequest = 10
        request.maxNumberOfRejectedRequestPerConnection = 10

        assertEquals(10, request.maxLookupRequest)
        assertEquals(10, request.concurrentLookupRequest)
        assertEquals(10, request.maxNumberOfRejectedRequestPerConnection)
    }

    @Test
    fun `validate request config creation with no properties set`() {
        val request = PulsarRequestConfig()
        assertEquals(50000, request.maxLookupRequest)
        assertEquals(5000, request.concurrentLookupRequest)
        assertEquals(50, request.maxNumberOfRejectedRequestPerConnection)
    }

    @Test
    fun `validate thread pool config with all properties set`() {
        val threadPool = TestData.getThreadPoolConfig()
        assertEquals(TestData.numIoThreads, threadPool.numIoThreads)
        assertEquals(TestData.numListenerThreads, threadPool.numListenerThreads)

        threadPool.numIoThreads = 10
        threadPool.numListenerThreads = 10

        assertEquals(10, threadPool.numIoThreads)
        assertEquals(10, threadPool.numListenerThreads)
    }

    @Test
    fun `validate thread pool config with no properties set`() {
        val threadPool = PulsarThreadPoolConfig()
        assertEquals(1, threadPool.numIoThreads)
        assertEquals(1, threadPool.numListenerThreads)
    }

    @Test
    fun `validate network config creation with all properties set`() {
        val network = TestData.getNetworkConfig()
        assertEquals(TestData.networkDuration, network.keepAliveInterval)
        assertEquals(TestData.networkDuration, network.connectionTimeout)
        assertEquals(TestData.networkDuration, network.requestTimeout)
        assertEquals(TestData.networkDuration, network.defaultBackoffInterval)
        assertEquals(TestData.networkDuration, network.maxBackoffInterval)
        assertEquals(TestData.networkDuration, network.operationTimeout)
        assertEquals(false, network.useTcpNoDelay)

        network.keepAliveInterval = "1s"
        network.connectionTimeout = "1s"
        network.requestTimeout = "1s"
        network.defaultBackoffInterval = "1s"
        network.maxBackoffInterval = "1s"
        network.operationTimeout = "1s"
        network.useTcpNoDelay = true

        assertEquals("1s", network.keepAliveInterval)
        assertEquals("1s", network.connectionTimeout)
        assertEquals("1s", network.requestTimeout)
        assertEquals("1s", network.defaultBackoffInterval)
        assertEquals("1s", network.maxBackoffInterval)
        assertEquals("1s", network.operationTimeout)
        assertEquals(true, network.useTcpNoDelay)
    }

    @Test
    fun `validate network config creation with no properties set`() {
        val network = PulsarNetworkConfig()
        assertEquals("30s", network.keepAliveInterval)
        assertEquals("10000ms", network.connectionTimeout)
        assertEquals("60000ms", network.requestTimeout)
        assertEquals("100ms", network.defaultBackoffInterval)
        assertEquals("30s", network.maxBackoffInterval)
        assertEquals("30000ms", network.operationTimeout)
        assertEquals(true, network.useTcpNoDelay)
    }

    @Test
    fun `validate tls config creation with all properties set`() {
        val tls = TestData.getTlsConfig()
        assertFalse(tls.useTls)
        assertFalse(tls.tlsAllowInsecureConnection)
        assertFalse(tls.tlsHostnameVerificationEnable)
        assertEquals(TestData.tlsTrustCertsFilePath, tls.tlsTrustCertsFilePath)

        tls.useTls = true
        tls.tlsAllowInsecureConnection = true
        tls.tlsHostnameVerificationEnable = false
        tls.tlsTrustCertsFilePath = "file_path"

        assertTrue(tls.useTls)
        assertTrue(tls.tlsAllowInsecureConnection)
        assertFalse(tls.tlsHostnameVerificationEnable)
        assertEquals("file_path", tls.tlsTrustCertsFilePath)
    }

    @Test
    fun `validate tls config creation with no properties set`() {
        val tls = PulsarTlsConfig()
        assertEquals(false, tls.useTls)
        assertEquals(false, tls.tlsAllowInsecureConnection)
        assertEquals(true, tls.tlsHostnameVerificationEnable)
        assertEquals("", tls.tlsTrustCertsFilePath)
    }

    @Test
    fun `validate client config creation with all properties set`() {
        val client = PulsarClientConfig(
            serviceUrl = TestData.serviceUrl,
            statsInterval = "100ms",
            tls = PulsarTlsConfig(),
            request = PulsarRequestConfig(),
            network = PulsarNetworkConfig(),
            threadPool = PulsarThreadPoolConfig(),
            auth = PulsarBasicAuthConfig(TestData.userName, TestData.password)
        )
        assertEquals(TestData.serviceUrl, client.serviceUrl)
        assertEquals("100ms", client.statsInterval)
        assertNotNull(client.tls)
        assertNotNull(client.request)
        assertNotNull(client.network)
        assertNotNull(client.threadPool)
        assertNotNull(client.auth)

        client.serviceUrl = "service_url"
        client.statsInterval = "10ms"
        client.tls = PulsarTlsConfig()
        client.request = PulsarRequestConfig()
        client.auth = PulsarBasicAuthConfig()
        client.network = PulsarNetworkConfig()
        client.threadPool = PulsarThreadPoolConfig()

        assertEquals("service_url", client.serviceUrl)
        assertEquals("10ms", client.statsInterval)
        assertNotNull(client.tls)
        assertNotNull(client.request)
        assertNotNull(client.network)
        assertNotNull(client.threadPool)
        assertNotNull(client.auth)
    }

    @Test
    fun `validate client config creation with mandatory property sets`() {
        val client = TestData.getNominalClientConfig()
        assertEquals("http://service/url/", client.serviceUrl)
        assertEquals("60s", client.statsInterval)
        assertNotNull(client.tls)
        assertNotNull(client.request)
        assertNotNull(client.network)
        assertNotNull(client.threadPool)
        assertNotNull(client.auth)
    }
}
