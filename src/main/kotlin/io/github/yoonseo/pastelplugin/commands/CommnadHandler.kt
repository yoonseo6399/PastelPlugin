package io.github.yoonseo.pastelplugin.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

abstract class CommandHandler(
    val command : String,
    vararg val argName :String
) : BukkitCommand(command),CommandExecutor {
    override fun execute(p0: CommandSender, p1: String, p2: Array<out String>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        //return onCommand(p0)
    }
    abstract fun onCommand(sender: CommandSender,argumentMap : HashMap<String,Any>)



}

