package work.stdpi.proxide.bungee.commands

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.plugin.Command
import work.stdpi.proxide.bungee.triggers.TriggerManager

class ToggleLoggingCommand(val tm: TriggerManager) : Command("proxide-toggle-logging") {
    override fun execute(p0: CommandSender?, p1: Array<out String?>?) {
        var builder = ComponentBuilder("Proxide event logging: ").color(ChatColor.WHITE)
        builder =
            if (tm.setLogging()) {
                builder.append("true").color(ChatColor.GREEN)
            } else {
                builder.append("false").color(ChatColor.RED)
            }
        p0?.sendMessage(builder.build())
    }
}
