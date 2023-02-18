package com.intuit.spring.pulsar.client.config

import org.apache.pulsar.client.impl.auth.AuthenticationTls
import org.apache.pulsar.client.impl.auth.oauth2.AuthenticationOAuth2
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PulsarAuthConfigTest {

    @Test
    fun `validate tls auth config creation`() {
        val pulsarTlsConfig = PulsarTlsAuthConfig(
            tlsCertFile = "/cert/file/path",
            tlsKeyFile = "/key/file/path"
        )

        val authentication = pulsarTlsConfig.getAuthenticationConfig() as AuthenticationTls

        assertNotNull(authentication)
        assertEquals("/cert/file/path",authentication.certFilePath)
        assertEquals("/key/file/path",authentication.keyFilePath)
        assertEquals(AuthType.TLS.name,pulsarTlsConfig.name)
    }

    @Test
    fun `validate oauth2 auth config creation`() {
        val pulsarOauthConfig = PulsarOAuth2AuthConfig(
            issueUrl = "https://issue.com/",
            credentialsUrl = "https://credential.com",
            audience = "1"
        )

        val authentication = pulsarOauthConfig.getAuthenticationConfig() as AuthenticationOAuth2
        assertNotNull(authentication)
        assertEquals(AuthType.OAUTH2.name,pulsarOauthConfig.name)
    }

    @Test
    fun `validate authenz auth config creation`() {
        assertThrows<NotImplementedError> {
            PulsarAthenzAuthConfig(
                tenantDomain = "tenantDomain",
                tenantService = "tenantService",
                providerDomain = "providerDomain",
                privateKey = "privateKey",
                keyId = "0"
            ).getAuthenticationConfig()
        }

        assertThrows<NotImplementedError> {
            PulsarAthenzAuthConfig(
                tenantDomain = "tenantDomain",
                tenantService = "tenantService",
                providerDomain = "providerDomain",
                privateKey = "privateKey"
            ).getAuthenticationConfig()
        }

        val pulsarAthenzAuthConfig = PulsarAthenzAuthConfig(
            tenantDomain = "tenantDomain",
            tenantService = "tenantService",
            providerDomain = "providerDomain",
            privateKey = "privateKey"
        )

        assertEquals(AuthType.ATHENZ.name,pulsarAthenzAuthConfig.name)
    }

    @Test
    fun `validate basic auth config creation`() {

        val pulsarBasicAuthConfig = PulsarBasicAuthConfig(
            userName = "username",
            password = "password"
        )

        val authentication = pulsarBasicAuthConfig.getAuthenticationConfig()

        assertNotNull(authentication)
        assertEquals(AuthType.BASIC.name,pulsarBasicAuthConfig.name)
    }
}
