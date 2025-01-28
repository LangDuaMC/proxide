package work.stdpi.proxide.bungee.hooks

import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Listener
import work.stdpi.proxide.bungee.ProxidePlugin

class ConnectedPlayerPingHook(plugin: ProxidePlugin) : ConnectedPlayerListHookBase<Int>(plugin), Listener {
    override val name = "online_players_latency"
    override val description = "Stat that collects online players latency"

    override fun collect(player: ProxiedPlayer) = player.ping
}