package work.stdpi.proxide.core.trigger

import work.stdpi.proxide.core.Core

abstract class AbstractTriggerManager(
    private val core: Core,
) {
    protected var triggers: MutableList<AbstractTrigger> = mutableListOf()
    open fun register(it: AbstractTrigger) {
        it.onEnable(core.getRegistry()!!, core.globalTags!!)
        triggers.add(it)
    }

    open fun onDisable() {
        triggers.forEach {
            it.onDisable()
        }
        triggers.clear()
    }
}
