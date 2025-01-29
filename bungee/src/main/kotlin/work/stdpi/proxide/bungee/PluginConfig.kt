package work.stdpi.proxide.bungee

import java.io.File
import java.io.IOException
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import work.stdpi.proxide.core.AbstractConfig

class PluginConfig(dataFolder: File) : AbstractConfig(dataFolder) {
    override fun loadConfig() {
        try {
            config =
                ConfigurationProvider.getProvider(YamlConfiguration::class.java)
                    .load(File(dataFolder, "config.yml"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override val platform = "bungeecord"

    override fun getString(path: String, default: String): String {
        return (config as Configuration).getString(path, default)
    }

    override fun getInt(path: String, default: Int): Int {
        return (config as Configuration).getInt(path, default)
    }

    override fun getBoolean(path: String, default: Boolean): Boolean {
        return (config as Configuration).getBoolean(path, default)
    }
}
