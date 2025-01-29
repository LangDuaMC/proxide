package work.stdpi.proxide.bungee.triggers

import net.md_5.bungee.api.event.*
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import work.stdpi.proxide.core.trigger.AbstractTrigger

class PlayerCommandEventListener : AbstractTrigger(), Listener {
    override var name = "player_commands"

    @EventHandler
    fun onPlayerCommandEvent(event: ChatEvent) {
        if (!event.isCommand) return
        onEvent()
    }
}
