package work.stdpi.proxide.core.metric

class Metric(
    val name: String,
    val description: String,
    val data: Iterable<MetricEntry<*>>,
    val type: MetricType = MetricType.GAUGE,
)
