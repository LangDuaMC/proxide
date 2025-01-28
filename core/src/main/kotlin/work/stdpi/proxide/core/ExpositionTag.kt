package work.stdpi.proxide.core

data class ExpositionTag<T>(val name: String, val value: T) {
    override fun toString() = "${name}=\"${value.toString()
        .replace("\\", "\\\\")
        .replace("\"", "\"")}\""
}
