package work.stdpi.proxide.core

interface IHook {
    abstract fun collect(): Iterable<Exposition>
}