package work.stdpi.proxide.core

class Exposition(
    val name: String,
    val description: String,
    val data: Iterable<ExpositionEntry<*>>,
    val type: ExpositionType = ExpositionType.GAUGE
)