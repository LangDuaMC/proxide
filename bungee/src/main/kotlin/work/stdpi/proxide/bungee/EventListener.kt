package work.stdpi.proxide.bungee

import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import work.stdpi.proxide.core.Core

class EventListener(core: Core) : Listener {
    @EventHandler
    fun onPostLogin() {}
}