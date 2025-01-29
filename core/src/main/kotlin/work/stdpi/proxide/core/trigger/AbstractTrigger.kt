package work.stdpi.proxide.core.trigger

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Tag
import io.micrometer.prometheus.PrometheusMeterRegistry

abstract class AbstractTrigger : ITrigger {
    abstract var name: String

    var registry: PrometheusMeterRegistry? = null
    var counter: Counter? = null

    fun onEnable(registry: PrometheusMeterRegistry, tags: Iterable<Tag>) {
        this.registry = registry
        counter = registry.counter("proxide_$name", tags)
    }

    fun onDisable() {
        counter?.close()
        counter = null
        registry = null
    }

    final override fun onEvent() {
        counter?.count()
    }
}
