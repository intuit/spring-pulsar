package com.intuit.spring.pulsar.client.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.intuit.spring.pulsar.client.constant.AuthConstants
import org.apache.commons.lang3.StringUtils
import org.apache.pulsar.client.api.Authentication
import org.apache.pulsar.client.api.AuthenticationFactory
import org.apache.pulsar.client.impl.auth.AuthenticationBasic
import org.apache.pulsar.client.impl.auth.AuthenticationTls
import org.apache.pulsar.client.impl.auth.oauth2.AuthenticationFactoryOAuth2
import java.net.URL

/**
 * Properties mapping class for auth
 * configurations.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PulsarTlsAuthConfig::class, name = "tls"),
    JsonSubTypes.Type(value = PulsarAthenzAuthConfig::class, name = "athenz"),
    JsonSubTypes.Type(value = PulsarBasicAuthConfig::class, name = "basic"),
    JsonSubTypes.Type(value = PulsarOAuth2AuthConfig::class, name = "oauth2")
)
abstract class PulsarAuthConfig(val name: String) {
    abstract fun getAuthenticationConfig(): Authentication
}


data class PulsarBasicAuthConfig(
    var userName: String = StringUtils.EMPTY,
    var password: String = StringUtils.EMPTY
) : PulsarAuthConfig(name = AuthType.BASIC.name) {

    override fun getAuthenticationConfig(): Authentication {
        val authMap = mutableMapOf<String, String>()
        authMap[AuthConstants.USER_ID] = this.userName
        authMap[AuthConstants.PASSWORD] = this.password
        return AuthenticationFactory.create(
            AuthenticationBasic::class.java.name,
            authMap
        )
    }

}


data class PulsarTlsAuthConfig(
    var tlsCertFile: String,
    var tlsKeyFile: String
) : PulsarAuthConfig(name = AuthType.TLS.name) {

    override fun getAuthenticationConfig(): Authentication {
        val authParams: MutableMap<String, String> = mutableMapOf()
        authParams[AuthConstants.TLS_CERT_FILE] = this.tlsCertFile
        authParams[AuthConstants.TLS_KEY_FILE] = this.tlsKeyFile

        return AuthenticationFactory.create(
            AuthenticationTls::class.java.name,
            authParams)
    }
}


data class PulsarAthenzAuthConfig(
    val tenantDomain: String,
    val tenantService: String,
    val providerDomain: String,
    val privateKey: String,
    var keyId: String? = null
) : PulsarAuthConfig(name = AuthType.ATHENZ.name) {

    override fun getAuthenticationConfig(): Authentication {
        val authParams: MutableMap<String,String> = mutableMapOf()
        authParams[AuthConstants.TENANT_DOMAIN] = this.tenantDomain
        authParams[AuthConstants.TENANT_SERVICE] = this.providerDomain
        authParams[AuthConstants.PRIVATE_KEY] = this.privateKey
        this.keyId?.let { authParams[AuthConstants.KEY_ID] = this.keyId!! }
        TODO("Not yet implemented")
    }
}

data class PulsarOAuth2AuthConfig(
    val issueUrl: String,
    val credentialsUrl: String,
    val audience: String
) : PulsarAuthConfig(name = AuthType.OAUTH2.name) {

    override fun getAuthenticationConfig(): Authentication {
        return AuthenticationFactoryOAuth2.clientCredentials(
            URL(this.issueUrl),
            URL(this.credentialsUrl),
            this.audience)
    }
}
