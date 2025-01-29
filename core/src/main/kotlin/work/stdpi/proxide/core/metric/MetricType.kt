package work.stdpi.proxide.core.metric

enum class MetricType {
    COUNTER,
    GAUGE;

    override fun toString() =
        when (this) {
            COUNTER -> "counter"
            GAUGE -> "gauge"
        }
}
