package work.stdpi.proxide.core

import io.micrometer.core.instrument.Tag
import work.stdpi.proxide.core.intelligence.MaxmindProvider
import java.util.logging.Logger

class Core(val config: AbstractConfig, val logger: Logger) {
    private var globalTags: List<Tag>? = null;
    private var hookRegistry: ExpositionRegistry? = null
    private var metricsRegistry: MetricsRegistry? = null
    var maxmind: MaxmindProvider? = null
        private set

    fun register(hook: IHook) = hookRegistry?.register(hook)

    fun onEnable() {
        globalTags = listOf<Tag>(Tag.of("platform", config.platform), Tag.of("instance", config.instance))
        hookRegistry = ExpositionRegistry(this, globalTags)
        metricsRegistry = MetricsRegistry(this, globalTags, hookRegistry!!);
        if (config.providerMaxmindEnabled) {
            maxmind = MaxmindProvider(config.providerMaxmindId, config.providerMaxmindKey)
        }
        maxmind?.onEnable()
        metricsRegistry!!.onEnable()
    }

    fun onDisable() {
        maxmind?.onDisable()
        metricsRegistry?.onDisable()
    }
}