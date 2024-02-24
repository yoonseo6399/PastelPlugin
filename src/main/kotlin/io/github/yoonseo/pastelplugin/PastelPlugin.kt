package io.github.yoonseo.pastelplugin

import io.github.yoonseo.pastelplugin.ThorHammer.ThorHammer
import io.github.yoonseo.pastelplugin.display.Display
import io.github.yoonseo.pastelplugin.display.DisplayPosition
import io.github.yoonseo.pastelplugin.display.InteractiveElement
import io.github.yoonseo.pastelplugin.display.TextElement
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

        Display().apply display@{
            addElement(
                InteractiveElement(DisplayPosition(1f,1f),"close",
                    TextElement(DisplayPosition(0f,0f),"closing",Component.text("[X]").color(NamedTextColor.RED),TextElement.HEIGHT)
                ).apply {
                    whenInteracted { this@display.close() }
                }
            )
            addElement(
                InteractiveElement(DisplayPosition(0f,0f),"interaction1",
                    TextElement(DisplayPosition(0f,0f),"inner1",Component.text("option 1").color(NamedTextColor.GOLD),1f)
                ).apply {
                    whenInteracted { it.sendMessage("option 1")}
                }
            )
            addElement(
                InteractiveElement(DisplayPosition(0f,-TextElement.HEIGHT),"interaction2",
                    TextElement(DisplayPosition(0f,0f),"inner2",Component.text("option 2").color(NamedTextColor.GOLD),1f)
                ).apply {
                    whenInteracted { it.sendMessage("option 2")}
                }
            )
            summon(Bukkit.getPlayer("command_juho")!!.location)
        }
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