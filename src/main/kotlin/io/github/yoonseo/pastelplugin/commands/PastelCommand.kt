package io.github.yoonseo.pastelplugin.commands

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.SimpleCommandMap
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.plugin.SimplePluginManager

abstract class PastelCommand
    constructor(command : String,val adminOnly : Boolean = false,val playerOnly : Boolean = false, maxArg : Int? = null, minArg : Int? = null): BukkitCommand(command), CommandExecutor

{
    constructor(command : String, adminOnly : Boolean = false,playerOnly : Boolean = false, requireArg : Int): this(command,adminOnly,playerOnly,requireArg,requireArg)


}