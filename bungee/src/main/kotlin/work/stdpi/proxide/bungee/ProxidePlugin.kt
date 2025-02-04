package work.stdpi.proxide.bungee

import net.md_5.bungee.api.plugin.Plugin
import work.stdpi.proxide.bungee.commands.ToggleLoggingCommand
import work.stdpi.proxide.bungee.commands.VersionCommand
import work.stdpi.proxide.bungee.hooks.ConnectedPlayerMetricHookGroup
import work.stdpi.proxide.bungee.hooks.ServerPingMetricHook
import work.stdpi.proxide.bungee.triggers.TriggerManager
import work.stdpi.proxide.core.Core

class ProxidePlugin : Plugin() {
    val config = PluginConfig(this.dataFolder)
    val core = Core(config, logger)
    private val hookGroup = ConnectedPlayerMetricHookGroup(this)
    private val serverPingHook = ServerPingMetricHook(this)
    private var triggerManager = TriggerManager(this)

    override fun onEnable() {
        super.onEnable()
        config.onEnable()
        logger.info(config.toString())
        core.onEnable()
        if (config.hooksPlayersEnabled) {
            hookGroup.onEnable()
            core.register(hookGroup)
        }
        if (config.hooksNetworkEnabled) {
            triggerManager.onEnable()
            serverPingHook.onEnable()
            core.register(serverPingHook)
        }
        proxy.pluginManager.registerCommand(this, VersionCommand())
        proxy.pluginManager.registerCommand(this, ToggleLoggingCommand(triggerManager))
        if (config.providerXCordEnabled) {
            try {
                Class.forName("me.minebuilders.xcordapi.XCordAPI")
            } catch (_: ClassNotFoundException) {
                logger.info("Runtime is not XCord")
            }
        }
    }

    override fun onDisable() {
        serverPingHook.onDisable()
        triggerManager.onDisable()
        core.onDisable()
        this.proxy.pluginManager.unregisterListeners(this)
    }
}
