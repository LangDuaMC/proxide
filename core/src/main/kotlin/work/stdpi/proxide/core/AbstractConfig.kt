package work.stdpi.proxide.core

import java.io.File
import java.io.IOException
import java.nio.file.Files

abstract class AbstractConfig(protected val dataFolder: File) {
    protected lateinit var config: Any // Platform-specific configuration object
    private val configFile: File = File(dataFolder, "config.yml")

    fun onEnable() {
        createDefaultConfig()
        loadConfig()
    }

    /**
     * Creates the default configuration file if it doesn't exist.
     */
    private fun createDefaultConfig() {
        if (!configFile.exists()) {
            try {
                Files.createDirectories(configFile.parentFile.toPath())
                this::class.java.getResourceAsStream("/config.yml")?.use { inputStream ->
                    Files.copy(inputStream, configFile.toPath())
                } ?: throw IOException("Default config.yml not found in resources.")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Loads the configuration. To be implemented in subclasses.
     */
    protected abstract fun loadConfig()

    /**
     * Retrieves a string value from the configuration with a default fallback.
     */
    protected abstract fun getString(path: String, default: String = ""): String

    /**
     * Retrieves an integer value from the configuration with a default fallback.
     */
    protected abstract fun getInt(path: String, default: Int = 0): Int

    /**
     * Retrieves a boolean value from the configuration with a default fallback.
     */
    protected abstract fun getBoolean(path: String, default: Boolean = false): Boolean

    abstract val platform: String

    val instance: String
        get() = getString("instance", "default")

    val endpointHost: String
        get() = getString("endpoint.host", "0.0.0.0")

    val endpointPort: Int
        get() = getInt("endpoint.port", 25080)

    val hooksPlayersEnabled: Boolean
        get() = getBoolean("hooks.players.enabled", true)

    val hooksNetworkEnabled: Boolean
        get() = getBoolean("hooks.network.enabled", true)

    val providerMaxmindEnabled: Boolean
        get() = getBoolean("provider.maxmind.enabled")

    val providerMaxmindId: String
        get() = getString("provider.maxmind.id", "")

    val providerMaxmindKey: String
        get() = getString("provider.maxmind.key", "")

    val providerXCordEnabled: Boolean
        get() = getBoolean("provider.xcord.enabled")
}
