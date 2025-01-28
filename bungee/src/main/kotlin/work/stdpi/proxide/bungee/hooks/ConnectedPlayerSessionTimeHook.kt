package work.stdpi.proxide.bungee.hooks

import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.event.ServerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import work.stdpi.proxide.bungee.ProxidePlugin
import work.stdpi.proxide.core.ExpositionType
import java.util.UUID

class ConnectedPlayerSessionTimeHook(plugin: ProxidePlugin) : ConnectedPlayerListHookBase<Long>(plugin), Listener {
    override val name = "online_players_time"
    override val description = "Stat that collects online players and their session time"
    override val type = ExpositionType.COUNTER
    private val sessionStartTimes = HashMap<UUID, Long>()

    override fun collect(player: ProxiedPlayer) = sessionStartTimes[player.uniqueId]?.let { startTime ->
        System.currentTimeMillis() / 1000 - startTime
    } ?: -1

    @EventHandler
    fun onServerConnected(event: ServerConnectedEvent) { // Changed to ServerConnectedEvent
        sessionStartTimes[event.player.uniqueId] = System.currentTimeMillis() / 1000
    }

    @EventHandler
    fun onServerDisconnect(event: ServerDisconnectEvent) {
        sessionStartTimes.remove(event.player.uniqueId)
    }
}