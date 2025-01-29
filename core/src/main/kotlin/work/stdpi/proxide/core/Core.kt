package work.stdpi.proxide.core

import io.micrometer.core.instrument.Tag
import work.stdpi.proxide.core.intelligence.MaxmindProvider
import work.stdpi.proxide.core.metric.IMetricHook
import work.stdpi.proxide.core.metric.MetricEndpoint
import work.stdpi.proxide.core.metric.MetricHookRegistry
import java.util.logging.Logger

class Core(val config: AbstractConfig, val logger: Logger) {
    var globalTags: List<Tag>? = null
        private set

    private var hookRegistry: MetricHookRegistry? = null
    private var metricEndpoint: MetricEndpoint? = null
    var maxmind: MaxmindProvider? = null
        private set

    fun register(hook: IMetricHook) = hookRegistry?.register(hook)

    fun getRegistry() = metricEndpoint?.registry

    fun onEnable() {
        globalTags =
            listOf<Tag>(Tag.of("platform", config.platform), Tag.of("instance", config.instance))
        hookRegistry = MetricHookRegistry(this, globalTags)
        metricEndpoint = MetricEndpoint(this, globalTags, hookRegistry!!)
        if (config.providerMaxmindEnabled) {
            maxmind = MaxmindProvider(this, config.providerMaxmindId, config.providerMaxmindKey)
            maxmind!!.onEnable()
        }
        metricEndpoint!!.onEnable()
    }

    fun onDisable() {
        maxmind?.onDisable()
        metricEndpoint?.onDisable()
    }
}
