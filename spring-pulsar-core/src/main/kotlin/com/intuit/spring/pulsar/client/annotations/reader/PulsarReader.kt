package com.intuit.spring.pulsar.client.annotations.reader

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PulsarReaderConfig
import org.apache.commons.lang3.StringUtils

@Suppress("LongParameterList")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PulsarReader(
    val topicName: String = StringUtils.EMPTY,
    val readerName: String = StringUtils.EMPTY,
    val cryptoFailureAction: String = StringUtils.EMPTY,
    val queue: ReaderQueue = ReaderQueue(),
    val subscriptionRolePrefix: String = StringUtils.EMPTY,
    val resetIncludeHead: String = false.toString(),
    val startMessageId: String = StringUtils.EMPTY,
    val startMessageFromRollbackDuration: StartMessageFromRollbackDuration = StartMessageFromRollbackDuration(),
    val defaultCryptoKeyReader: String = StringUtils.EMPTY,
    val keyHashRange: Array<Range> = []
)

/**
 * Extension function to map [PulsarReader] to [PulsarReaderConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [PulsarReader]
 */
fun PulsarReader.map(resolver: IAnnotationPropertyResolver): PulsarReaderConfig {
    return PulsarReaderConfig(
        topicName = resolver.resolve(this.topicName),
        readerName = resolver.resolve(this.readerName),
        cryptoFailureAction = resolver.resolve(this.cryptoFailureAction),
        queue = this.queue.map(resolver),
        subscriptionRolePrefix = resolver.resolve(this.subscriptionRolePrefix),
        resetIncludeHead = resolver.resolve(this.resetIncludeHead).toBoolean(),
        startMessageId = resolver.resolve(this.startMessageId),
        startMessageFromRollbackDuration = this.startMessageFromRollbackDuration.map(
            resolver
        ),
        defaultCryptoKeyReader = resolver.resolve(this.defaultCryptoKeyReader),
        keyHashRange = this.keyHashRange.map { range -> range.map(resolver) }
            .toMutableList()
    )
}

