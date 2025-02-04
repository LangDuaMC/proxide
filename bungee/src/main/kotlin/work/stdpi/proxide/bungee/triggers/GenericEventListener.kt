package work.stdpi.proxide.bungee.triggers

import net.md_5.bungee.api.plugin.Event
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import work.stdpi.proxide.core.trigger.AbstractTrigger

class GenericEventListener : AbstractTrigger(), Listener {
    override var name = "all_events"

    @EventHandler
    fun onLoginEvent(event: Event) {
        onEvent()
    }
}
