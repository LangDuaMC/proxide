package work.stdpi.proxide.core.metric

data class MetricTag<T>(val name: String, val value: T) {
    override fun toString() =
        "${name}=\"${
      value.toString()
        .replace("\\", "\\\\")
        .replace("\"", "\"")}\""
}
