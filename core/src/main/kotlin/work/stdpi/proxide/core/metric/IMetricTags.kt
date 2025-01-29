package work.stdpi.proxide.core.metric

interface IMetricTags {
    fun toTags(): Iterable<MetricTag<*>>
}
