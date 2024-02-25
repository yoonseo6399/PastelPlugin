package io.github.yoonseo.pastelplugin

import io.github.yoonseo.pastelplugin.ThorHammer.ThorHammer
import io.github.yoonseo.pastelplugin.display.*
import io.github.yoonseo.pastelplugin.robotArm.ArmController
import io.github.yoonseo.pastelplugin.skillHelper.GlowingBlock
import io.github.yoonseo.pastelplugin.skillHelper.glowing
import io.github.yoonseo.pastelplugin.system.ListenerRegister
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.HashMap

class PastelPlugin : JavaPlugin() {


    companion object{
        lateinit var plugin : PastelPlugin
            private set
        val taskList = HashMap<String,Int>()
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this


        ListenerRegister.registerEvent("$projectPath.itemHandlers.EventHandler")
        //Bukkit.getPlayer("command_juho")?.let { CustomEntity(Dragon::class).spawn(it.location) }

        //ListenerRegister.registerAllEvents()

        Bukkit.getPlayer("command_juho")?.inventory?.addItem(ThorHammer().getItem())
        //Bukkit.getPlayer("command_juho")?.inventory?.addItem(Dash().getItem())
        //overworld.getBlockAt(-36 ,35 ,30048).glowing(true)
//        Bukkit.getPlayer("command_juho")?.let {
//            ArmController(it)
//        }


        Bukkit.getPlayer("command_juho")?.let { test().summon(it.location) }





        Bukkit.getLogger().info("PastelPlugin is Loaded")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        val keys = HashSet(GlowingBlock.onGlowing.keys)
        keys.forEach { debug(it); it.glowing(false) }
    }
}

val plugin = PastelPlugin.plugin
const val projectPath = """io.github.yoonseo.pastelplugin"""
val overworld = Bukkit.getWorld("world")!!