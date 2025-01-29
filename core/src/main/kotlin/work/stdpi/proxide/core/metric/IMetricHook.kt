package work.stdpi.proxide.core.metric

interface IMetricHook {
    abstract fun collect(): Iterable<Metric>
}
