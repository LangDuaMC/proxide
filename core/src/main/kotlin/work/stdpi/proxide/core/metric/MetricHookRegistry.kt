package work.stdpi.proxide.core.metric

import io.micrometer.core.instrument.Tag
import work.stdpi.proxide.core.Core

class MetricHookRegistry(val core: Core, val globalTags: List<Tag>?) {
    private val hooks = ArrayList<IMetricHook>()

    fun register(hook: IMetricHook) {
        hooks.add(hook)
    }

    fun dyRenderHooks(): String {
        val sb = StringBuilder(1024)
        val uniqueSeries = HashSet<String>(256)
        val renderedGlobalTags: Iterable<MetricTag<*>> =
            globalTags!!.map { MetricTag(it.key, it.value) }
        val prefix = "proxide_"

        hooks.forEach { hook ->
            hook.collect().forEach { hook ->
                sb.append("# HELP ")
                    .append(prefix + hook.name)
                    .append(' ')
                    .append(hook.description)
                    .append('\n') //
                    .append("# TYPE ")
                    .append(prefix + hook.name)
                    .append(' ')
                    .append(hook.type)
                    .append('\n')

                hook.data.forEach { entry ->
                    val seriesIdentifier = buildString {
                        append(prefix + hook.name)
                        if (entry.metricTags.isNotEmpty()) {
                            append('{')
                                .append((renderedGlobalTags + entry.metricTags).joinToString(","))
                                .append('}')
                        }
                    }

                    if (uniqueSeries.add(seriesIdentifier)) {
                        sb.append(seriesIdentifier)
                            .append(' ')
                            .append(entry.value.toString())
                            .append('\n')
                    }
                }
            }
        }

        return sb.toString()
    }
}
