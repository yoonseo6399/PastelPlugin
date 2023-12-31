package io.github.yoonseo.pastelplugin

import io.github.yoonseo.pastelplugin.ThorHammer.ThorHammer
import io.github.yoonseo.pastelplugin.boss.*
import io.github.yoonseo.pastelplugin.system.ListenerRegister
import io.github.yoonseo.pastelplugin.valorant.Jett.Dash
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PastelPlugin : JavaPlugin() {


    companion object{
        lateinit var plugin : PastelPlugin
            private set
        val taskList = HashMap<String,Int>()
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this
        //WitherKing()
        //Bukkit.getPlayer("command_juho")?.let {Boss.spawn("WitherKing",it.location) }

        ListenerRegister.registerEvent("$projectPath.itemHandlers.EventHandler")


        //ListenerRegister.registerAllEvents()

        Bukkit.getPlayer("command_juho")?.inventory?.addItem(ThorHammer().getItem())
        Bukkit.getPlayer("command_juho")?.inventory?.addItem(Dash().getItem())
        Bukkit.getLogger().info("PastelPlugin is Loaded")
    }

    override fun onDisable() {
        // Plugin shutdown logic

    }
}

val plugin = PastelPlugin.plugin
const val projectPath = """io.github.yoonseo.pastelplugin"""
val overworld = Bukkit.getWorld("world")!!