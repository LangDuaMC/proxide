package work.stdpi.proxide.bungee.hooks

import java.net.InetSocketAddress
import net.md_5.bungee.api.plugin.Listener
import work.stdpi.proxide.bungee.ProxidePlugin
import work.stdpi.proxide.core.metric.IMetricHook
import work.stdpi.proxide.core.metric.Metric
import work.stdpi.proxide.core.metric.MetricEntry
import work.stdpi.proxide.core.metric.MetricTag

class ConnectedPlayerMetricHookGroup(val plugin: ProxidePlugin) : IMetricHook {
    val hooks =
        listOf<ConnectedPlayerListHookBase<*>>(
            ConnectedPlayerPingHook(plugin),
            ConnectedPlayerSessionTimeHook(plugin),
        )

    fun onEnable() {
        hooks.forEach {
            if (it is Listener) {
                plugin.proxy.pluginManager.registerListener(plugin, it)
            }
        }
    }

    override fun collect(): Iterable<Metric> {
        val entries =
            plugin.proxy.players.map {
                val address = (it.socketAddress as InetSocketAddress).address
                val tags =
                    mutableListOf<MetricTag<*>>(
                        MetricTag("uuid", it.uniqueId),
                        MetricTag("player", it.name),
                        MetricTag("server", it.server.info.name),
                        MetricTag("ip", address),
                    )
                if (plugin.config.providerMaxmindEnabled && plugin.core.maxmind != null) {
                    plugin.core.maxmind?.getAsn(address)?.let { asn ->
                        tags.add(MetricTag("asn", asn.autonomousSystemNumber))
                        tags.add(MetricTag("isp", asn.autonomousSystemOrganization))
                    }
                    plugin.core.maxmind?.getLocation(address)?.let { city ->
                        tags.add(MetricTag("country", city.countryIsoCode))
                        tags.add(MetricTag("city", city.cityName))
                        tags.add(MetricTag("latitude", city.latitude))
                        tags.add(MetricTag("longitude", city.longitude))
                    }
                }
                Pair(it, tags)
            }
        return hooks.map { h ->
            Metric(
                h.name,
                h.description,
                entries.map { MetricEntry(it.second, h.collect(it.first)) },
                h.type,
            )
        }
    }
}
