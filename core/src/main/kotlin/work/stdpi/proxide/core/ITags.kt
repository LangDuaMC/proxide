package work.stdpi.proxide.core

interface ITags {
    fun toTags(): Iterable<ExpositionTag<*>>
}