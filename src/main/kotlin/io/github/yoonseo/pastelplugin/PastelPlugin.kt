package io.github.yoonseo.pastelplugin

import io.github.yoonseo.pastelplugin.ThorHammer.ThorHammer
//import io.github.yoonseo.pastelplugin.commands.PastelPluginCommand
import io.github.yoonseo.pastelplugin.rpg.quest.impl.Quests
import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import io.github.yoonseo.pastelplugin.rpg.moster.MonsterCommand
import io.github.yoonseo.pastelplugin.rpg.moster.impls.*
import io.github.yoonseo.pastelplugin.rpg.quest.QuestCommand

import io.github.yoonseo.pastelplugin.skillHelper.GlowingBlock
import io.github.yoonseo.pastelplugin.skillHelper.glowing
import io.github.yoonseo.pastelplugin.system.ListenerRegister
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.HashMap

class PastelPlugin : JavaPlugin() {


    companion object{
        lateinit var plugin : PastelPlugin
            private set
        val taskList = HashMap<String,Int>()
        val testingJob = Job()
        lateinit var testingScope : CoroutineScope
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this
        getCommand("quest")!!.setExecutor(QuestCommand())
        getCommand("monster")!!.setExecutor(MonsterCommand())


        ListenerRegister.registerEvent("$projectPath.itemHandlers.EventHandler")

        //getCommand("pastelplugin")?.setExecutor(PastelPluginCommand())

        //ListenerRegister.registerEvent("$projectPath.chair.EventHandler")//TODO

        ThorHammer().let{
            command_juho {
                inventory.addItem(it.getItem())
                Quests.starting().start(this)
            }
        }

        //Bukkit.getOnlinePlayers().forEach{
        //    Quests.starting().start(it)
        //}
        //command_juho()?.let { Quests.mineCoal().start(it) }


        Bukkit.getPlayer("Cube_x2")?.let {
            Monster.spawn(Wolf::class,it.location)
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