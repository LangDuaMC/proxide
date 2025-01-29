package work.stdpi.proxide.bungee.triggers

import net.md_5.bungee.api.plugin.Listener
import work.stdpi.proxide.bungee.ProxidePlugin
import work.stdpi.proxide.core.trigger.AbstractTrigger
import work.stdpi.proxide.core.trigger.AbstractTriggerManager

class TriggerManager(private val plugin: ProxidePlugin) :
    AbstractTriggerManager(plugin.core) {

    fun onEnable() {
        register(LoginEventListener())
        register(PlayerChatEventListener())
        register(PlayerCommandEventListener())
        register(PlayerDisconnectEventListener())
        register(PlayerJoinedEventListener())
        register(PluginMessageEvent())
    }

    override fun register(it: AbstractTrigger) {
        super.register(it)
        if (it is Listener) {
            plugin.proxy.pluginManager.registerListener(plugin, it)
        }
    }
}
