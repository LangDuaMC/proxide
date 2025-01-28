package work.stdpi.proxide.bungee.hooks

import net.md_5.bungee.api.scheduler.ScheduledTask
import work.stdpi.proxide.bungee.ProxidePlugin
import work.stdpi.proxide.core.IHook
import work.stdpi.proxide.core.Exposition
import work.stdpi.proxide.core.ExpositionEntry
import work.stdpi.proxide.core.ExpositionTag
import work.stdpi.proxide.core.ExpositionType
import java.io.IOException
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class ServerPingHook(private val plugin: ProxidePlugin) : IHook {
    private val latencyCache = ConcurrentHashMap<String, Double>()
    private var pingTask: ScheduledTask? = null
    private val connectionTimeout = 1500L // 1.5 seconds

    private fun pingServers() {
        plugin.proxy.servers.values.forEach { server ->
            plugin.proxy.scheduler.runAsync(plugin, Runnable {
                try {
                    val address = server.socketAddress ?: return@Runnable
                    val startTime = System.nanoTime()

                    Socket().use { socket ->
                        socket.soTimeout = connectionTimeout.toInt()
                        socket.connect(address, connectionTimeout.toInt())

                        val latency = (System.nanoTime() - startTime) * 1e-6
                        latencyCache[server.name] = latency
                    }
                } catch (e: IOException) {
                    latencyCache[server.name] = -1.0
                } catch (e: Exception) {
                    // Handle other potential exceptions
                    latencyCache[server.name] = -2.0
                }
            })
        }
    }

    fun onEnable() {
        pingTask = plugin.proxy.scheduler.schedule(
            plugin,
            { pingServers() },
            1L, 3L, TimeUnit.SECONDS // Initial delay 1s, repeat every 3s
        )
    }

    override fun collect() = listOf(
        Exposition(
            "server_latency",
            "TCP connection latency to backend servers in milliseconds",
            plugin.proxy.servers.values.map { server ->
                ExpositionEntry(
                    listOf(ExpositionTag("server", server.name)),
                    latencyCache[server.name] ?: -3.0 // -3 = not checked yet
                )
            },
            type = ExpositionType.GAUGE
        )
    )
}