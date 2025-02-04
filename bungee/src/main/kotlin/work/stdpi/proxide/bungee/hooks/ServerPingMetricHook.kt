package work.stdpi.proxide.bungee.hooks

import java.net.Socket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import net.md_5.bungee.api.scheduler.ScheduledTask
import work.stdpi.proxide.bungee.ProxidePlugin
import work.stdpi.proxide.core.metric.IMetricHook
import work.stdpi.proxide.core.metric.Metric
import work.stdpi.proxide.core.metric.MetricEntry
import work.stdpi.proxide.core.metric.MetricTag
import work.stdpi.proxide.core.metric.MetricType

class ServerPingMetricHook(private val plugin: ProxidePlugin) : IMetricHook {
    private val latencyCache = ConcurrentHashMap<String, Double>()
    private var pingTask: ScheduledTask? = null
    private val connectionTimeout = 1500L // 1.5 seconds

    private fun pingServers() {
        plugin.proxy.servers.values.forEach { server ->
            plugin.proxy.scheduler.runAsync(
                plugin,
                Runnable {
                    try {
                        val address = server.socketAddress ?: return@Runnable
                        val startTime = System.nanoTime()

                        Socket().use { socket ->
                            socket.soTimeout = connectionTimeout.toInt()
                            socket.connect(address, connectionTimeout.toInt())

                            val latency = (System.nanoTime() - startTime) * 1e-6
                            latencyCache[server.name] = latency
                        }
                    } catch (e: Exception) {
                        latencyCache[server.name] = -1.0
                    }
                },
            )
        }
    }

    fun onEnable() {
        pingTask =
            plugin.proxy.scheduler.schedule(
                plugin,
                { pingServers() },
                1L,
                3L,
                TimeUnit.SECONDS, // Initial delay 1s, repeat every 3s
            )
    }

    fun onDisable() {
        pingTask?.cancel()
        pingTask = null
    }

    override fun collect() =
        listOf(
            Metric(
                "server_latency",
                "TCP connection latency to backend servers in milliseconds",
                plugin.proxy.servers.values.map { server ->
                    MetricEntry(
                        listOf(MetricTag("server", server.name)),
                        latencyCache[server.name] ?: -3.0, // -3 = not checked yet
                    )
                },
                type = MetricType.GAUGE,
            )
        )
}
