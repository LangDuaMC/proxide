package work.stdpi.proxide.bungee.hooks

import net.md_5.bungee.api.connection.ProxiedPlayer
import work.stdpi.proxide.bungee.ProxidePlugin
import work.stdpi.proxide.core.metric.MetricType

abstract class ConnectedPlayerListHookBase<T>(val plugin: ProxidePlugin) {
    abstract val name: String
    abstract val description: String

    abstract fun collect(player: ProxiedPlayer): T

    open val type = MetricType.GAUGE
}
