package work.stdpi.proxide.bungee.triggers

import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import work.stdpi.proxide.core.trigger.AbstractTrigger

class PluginMessageEvent : AbstractTrigger(), Listener {
    override var name = "plugin_message"

    @EventHandler
    fun onPluginMessageEvent(event: PluginMessageEvent) {
        onEvent()
    }
}

