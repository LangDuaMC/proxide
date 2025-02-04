package work.stdpi.proxide.bungee.commands

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.plugin.Command

class VersionCommand : Command("proxide-version") {
    override fun execute(sender: CommandSender?, args: Array<out String?>?) {
        sender?.sendMessage(
            ComponentBuilder("Proxide version: ") //
                .color(ChatColor.WHITE)
                .append(work.stdpi.proxide.BuildMetadata.VERSION)
                .color(ChatColor.GREEN)
                .build()
        )
    }
}
