package com.intuit.spring.pulsar.client.client

import com.intuit.spring.pulsar.client.TestData
import com.intuit.spring.pulsar.client.callPrivateFunc
import com.intuit.spring.pulsar.client.config.PulsarClientConfig
import org.apache.pulsar.client.api.Authentication
import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.PulsarClientException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class PulsarClientFactoryTest {

    private lateinit var clientFactory: PulsarClientFactory
    private lateinit var clientConfig: PulsarClientConfig
    private lateinit var pulsarClientBuidler: ClientBuilder
    private lateinit var pulsarClient: PulsarClient
    private lateinit var mockedPulsarClient: MockedStatic<PulsarClient>

    @BeforeEach
    fun init() {
        pulsarClient = mock(PulsarClient::class.java)
        clientConfig = mock(PulsarClientConfig::class.java)
        pulsarClientBuidler = mock(ClientBuilder::class.java)
        mockedPulsarClient = Mockito.mockStatic(PulsarClient::class.java)
        mockedPulsarClient.`when`<ClientBuilder>(PulsarClient::builder).thenReturn(pulsarClientBuidler)
        `when`(pulsarClientBuidler.build()).thenReturn(pulsarClient)
        `when`(clientConfig.auth).thenReturn(TestData.getAuthConfig())
        `when`(clientConfig.serviceUrl).thenReturn("http://service/url")
        `when`(clientConfig.statsInterval).thenReturn("10s")
    }

    @AfterEach
    fun tearDown() {
        mockedPulsarClient.close()
    }

    @Test
    fun `validate withNetwork with all properties set`() {
        val networkConfig = TestData.getNetworkConfig()
        `when`(clientConfig.network).thenReturn(networkConfig)
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).connectionTimeout(anyInt(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).keepAliveInterval(anyInt(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).operationTimeout(anyInt(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).enableTcpNoDelay(anyBoolean())
        verify(pulsarClientBuidler, times(1)).startingBackoffInterval(anyLong(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).maxBackoffInterval(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withNetwork with no properties set`() {
        val networkConfig = PulsarClientConfig.PulsarNetworkConfig()
        `when`(clientConfig.network).thenReturn(networkConfig)
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).connectionTimeout(anyInt(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).keepAliveInterval(anyInt(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).operationTimeout(anyInt(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).enableTcpNoDelay(anyBoolean())
        verify(pulsarClientBuidler, times(1)).startingBackoffInterval(anyLong(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).maxBackoffInterval(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withTls with all properties set`() {
        val tlsConfig = TestData.getTlsConfig()
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(tlsConfig)
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).enableTls(anyBoolean())
        verify(pulsarClientBuidler, times(1)).allowTlsInsecureConnection(anyBoolean())
        verify(pulsarClientBuidler, times(1)).enableTlsHostnameVerification(anyBoolean())
        verify(pulsarClientBuidler, times(1)).tlsTrustCertsFilePath(anyString())
    }

    @Test
    fun `validate withTls with no properties set`() {
        val tlsConfig = PulsarClientConfig.PulsarTlsConfig()
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(tlsConfig)
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).enableTls(anyBoolean())
        verify(pulsarClientBuidler, times(1)).allowTlsInsecureConnection(anyBoolean())
        verify(pulsarClientBuidler, times(1)).enableTlsHostnameVerification(anyBoolean())
        verify(pulsarClientBuidler, times(1)).tlsTrustCertsFilePath(anyString())
    }

    @Test
    fun `validate withRequestConfig with all properties set`() {
        val requestConfig = TestData.getRequestConfig()
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(requestConfig)
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).maxLookupRequests(anyInt())
        verify(pulsarClientBuidler, times(1)).maxConcurrentLookupRequests(anyInt())
        verify(pulsarClientBuidler, times(1)).maxNumberOfRejectedRequestPerConnection(anyInt())
    }

    @Test
    fun `validate withRequestConfig with no properties set`() {
        val requestConfig = PulsarClientConfig.PulsarRequestConfig()
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(requestConfig)
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).maxLookupRequests(anyInt())
        verify(pulsarClientBuidler, times(1)).maxConcurrentLookupRequests(anyInt())
        verify(pulsarClientBuidler, times(1)).maxNumberOfRejectedRequestPerConnection(anyInt())
    }

    @Test
    fun `validate withThreadPool with all properties set`() {
        val threadPoolConfig = TestData.getThreadPoolConfig()
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(threadPoolConfig)
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).ioThreads(anyInt())
        verify(pulsarClientBuidler, times(1)).listenerThreads(anyInt())
    }

    @Test
    fun `validate withThreadPool with NO properties set`() {
        val threadPoolConfig = PulsarClientConfig.PulsarThreadPoolConfig()
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(threadPoolConfig)
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).ioThreads(anyInt())
        verify(pulsarClientBuidler, times(1)).listenerThreads(anyInt())
    }

    @Test
    fun `getClient returns client object`() {
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        val pulsarClient = mockPulsarClient()
        val createdClient = clientFactory.getClient()
        assertNotNull(createdClient)
        assertEquals(pulsarClient, createdClient)
    }

    @Test
    fun `tearDown closes all clients`() {
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        val pulsarClient = mockPulsarClient()
        clientFactory.callPrivateFunc("close")
        verify(pulsarClient, times(1)).close()
    }

    @Test
    fun `tearDown client close failing with exception`() {
        `when`(clientConfig.network).thenReturn(PulsarClientConfig.PulsarNetworkConfig())
        `when`(clientConfig.tls).thenReturn(PulsarClientConfig.PulsarTlsConfig())
        `when`(clientConfig.request).thenReturn(PulsarClientConfig.PulsarRequestConfig())
        `when`(clientConfig.threadPool).thenReturn(PulsarClientConfig.PulsarThreadPoolConfig())
        clientFactory = PulsarClientFactory(clientConfig)
        val pulsarClient = mockPulsarClient()
        `when`(pulsarClient.close()).thenThrow(PulsarClientException::class.java)
        assertThrows<Exception> { clientFactory.callPrivateFunc("close") }
        verify(pulsarClient, times(1)).close()
    }

    @Test
    fun `validate withClientConfig`() {
        val clientConfig = PulsarClientConfig(
            serviceUrl = "serviceurl",
            auth = PulsarClientConfig.PulsarAuthConfig(
                userName = "username",
                password = "password"
            ),
            statsInterval = "100ms"
        )
        clientFactory = PulsarClientFactory(clientConfig)
        verify(pulsarClientBuidler, times(1)).statsInterval(anyLong(), any(TimeUnit::class.java))
        verify(pulsarClientBuidler, times(1)).serviceUrl(anyString())
        verify(pulsarClientBuidler, times(1)).build()
        verify(pulsarClientBuidler, times(1)).authentication(any(Authentication::class.java))
    }

    private fun mockPulsarClient(): PulsarClient {
        val pulsarClient = mock(PulsarClient::class.java)
        val field: Field = PulsarClientFactory::class.java.getDeclaredField("pulsarClient")
        field.isAccessible = true
        field.set(clientFactory, pulsarClient)
        return pulsarClient
    }
}
