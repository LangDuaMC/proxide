package work.stdpi.proxide.core.metric

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import work.stdpi.proxide.core.Core

class MetricEndpoint(
    val core: Core,
    val globalTags: Iterable<Tag>?,
    val hookRegistry: MetricHookRegistry,
) {
    val registry = PrometheusMeterRegistry(PromConfig)
    var server: EmbeddedServer<*, *>? = null

    fun onEnable() {
        server =
            embeddedServer(
                Netty,
                port = core.config.endpointPort,
                host = core.config.endpointHost,
            ) {
                install(Compression) {
                    gzip {
                        priority = 1.0 // Set priority for gzip
                        minimumSize(1024)
                    }
                    deflate {
                        priority = 0.9 // Set priority for deflate
                        minimumSize(1024) // Minimum size for compression (20 KB)
                    }
                }
                install(MicrometerMetrics) {
                    this.registry = this@MetricEndpoint.registry
                    globalTags!!.let { globalTags ->
                        meterBinders =
                            listOf(
                                JvmMemoryMetrics(globalTags),
                                JvmGcMetrics(globalTags),
                                ProcessorMetrics(globalTags),
                            )
                    }
                }
                routing {
                    get("/metrics") {
                        call.respond(
                            buildString {
                                append(this@MetricEndpoint.registry.scrape())
                                append(hookRegistry.dyRenderHooks())
                            },
                        )
                    }
                }
                core.logger.info("Starting at 0.0.0.0:8080")
            }
        server?.start(wait = false)
    }

    fun onDisable() {
        server?.stop()
    }

    object PromConfig : PrometheusConfig {
        override fun get(key: String): String? = null // Implement if needed

        override fun prefix() = "Minecraft"

        override fun descriptions() = true
    }
}
