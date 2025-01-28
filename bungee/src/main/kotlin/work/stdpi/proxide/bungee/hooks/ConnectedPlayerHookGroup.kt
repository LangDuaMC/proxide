package work.stdpi.proxide.bungee.hooks

import net.md_5.bungee.api.plugin.Listener
import work.stdpi.proxide.bungee.ProxidePlugin
import work.stdpi.proxide.core.Exposition
import work.stdpi.proxide.core.IHook
import work.stdpi.proxide.core.ExpositionEntry
import work.stdpi.proxide.core.ExpositionTag
import java.net.InetSocketAddress

class ConnectedPlayerHookGroup(val plugin: ProxidePlugin) : IHook {
    val hooks =
        listOf<ConnectedPlayerListHookBase<*>>(ConnectedPlayerPingHook(plugin), ConnectedPlayerSessionTimeHook(plugin))

    fun onEnable() {
        hooks.forEach {
            if (it is Listener) {
                plugin.proxy.pluginManager.registerListener(plugin, it)
            }
        }
    }

    override fun collect(): Iterable<Exposition> {
        val entries = plugin.proxy.players.map {
            val address = (it.socketAddress as InetSocketAddress).address
            val tags = mutableListOf<ExpositionTag<*>>(
                ExpositionTag("uuid", it.uniqueId),
                ExpositionTag("player", it.name),
                ExpositionTag("ip", address),
            )
            if (plugin.config.providerMaxmindEnabled && plugin.core.maxmind != null) {
                val asn = plugin.core.maxmind?.getAsn(address)
                val city = plugin.core.maxmind?.getLocation(address)
                tags.add(
                    ExpositionTag(
                        "asn", ""
                    )
                )
            }
            Pair(
                it,
                tags
            )
        }
        return hooks.map { h ->
            Exposition(h.name, h.description, entries.map {
                ExpositionEntry(it.second, h.collect(it.first))
            }, h.type)
        }
    }
}