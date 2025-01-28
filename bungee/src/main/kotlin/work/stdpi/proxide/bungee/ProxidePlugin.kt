package work.stdpi.proxide.bungee

import net.md_5.bungee.api.plugin.Plugin
import work.stdpi.proxide.bungee.hooks.ConnectedPlayerHookGroup
import work.stdpi.proxide.bungee.hooks.ServerPingHook
import work.stdpi.proxide.core.Core

class ProxidePlugin : Plugin() {
    val config = PluginConfig(this.dataFolder)
    val core = Core(config, logger)
    private val hookGroup = ConnectedPlayerHookGroup(this)
    private val serverPingHook = ServerPingHook(this)
    private val listener = EventListener(core)

    override fun onEnable() {
        super.onEnable()
        config.onEnable()
        core.onEnable()
        hookGroup.onEnable()
        serverPingHook.onEnable()
        core.register(hookGroup)
        core.register(serverPingHook)
        try {
            Class.forName("me.minebuilders.xcordapi.XCordAPI")
        } catch (_: ClassNotFoundException) {
            logger.info("Runtime is not XCord")
        }
        this.proxy.pluginManager.registerListener(this, listener)
    }

    override fun onDisable() {
        core.onDisable()
        this.proxy.pluginManager.unregisterListeners(this)
    }
}
