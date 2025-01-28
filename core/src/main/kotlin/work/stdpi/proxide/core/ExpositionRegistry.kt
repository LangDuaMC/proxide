package work.stdpi.proxide.core

import io.micrometer.core.instrument.Tag

class ExpositionRegistry(val core: Core, val globalTags: List<Tag>?) {
    private val hooks = ArrayList<IHook>()

    fun register(hook: IHook) {
        hooks.add(hook)
    }

    fun dyRenderHooks(): String {
        val sb = StringBuilder(1024)
        val uniqueSeries = HashSet<String>(256)
        val renderedGlobalTags: Iterable<ExpositionTag<*>> = globalTags!!.map {
            ExpositionTag(it.key, it.value)
        }
        val prefix = "proxide_"

        hooks.forEach { hook ->
            hook.collect().forEach { hook ->
                sb.append("# HELP ").append(prefix + hook.name).append(' ').append(hook.description).append('\n') //
                    .append("# TYPE ").append(prefix + hook.name).append(' ').append(hook.type).append('\n')

                hook.data.forEach { entry ->
                    val seriesIdentifier = buildString {
                        append(prefix + hook.name)
                        if (entry.expositionTags.isNotEmpty()) {
                            append('{').append((renderedGlobalTags + entry.expositionTags).joinToString(","))
                                .append('}')
                        }
                    }

                    if (uniqueSeries.add(seriesIdentifier)) {
                        sb.append(seriesIdentifier).append(' ').append(entry.value.toString()).append('\n')
                    }
                }
            }
        }

        return sb.toString()
    }
}