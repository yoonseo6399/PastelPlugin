package io.github.yoonseo.pastelplugin.commands

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.SimpleCommandMap
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.plugin.SimplePluginManager

interface PastelCommand : CommandExecutor{
    val name : String
}