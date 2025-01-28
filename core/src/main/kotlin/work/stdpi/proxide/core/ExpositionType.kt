package work.stdpi.proxide.core

enum class ExpositionType {
    COUNTER,
    GAUGE;

    override fun toString() = when (this) {
        COUNTER -> "counter";
        GAUGE -> "gauge";
    }
}