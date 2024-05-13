package io.github.yoonseo.pastelplugin

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.ThorHammer.ThorHammer
import io.github.yoonseo.pastelplugin.lib.AdjustableValueCommand
import io.github.yoonseo.pastelplugin.rpg.magic.RedGloom.Explosion
import io.github.yoonseo.pastelplugin.rpg.magic.RedGloom.Maline
import io.github.yoonseo.pastelplugin.rpg.magic.RedGloom.ScatteredFaith
import io.github.yoonseo.pastelplugin.rpg.magic.earth.RollingThunder
import io.github.yoonseo.pastelplugin.rpg.magic.earth.Valagart
import io.github.yoonseo.pastelplugin.rpg.magic.fire.Fireball
import io.github.yoonseo.pastelplugin.rpg.magic.ice.Friche
import io.github.yoonseo.pastelplugin.rpg.moster.Monster
//import io.github.yoonseo.pastelplugin.commands.PastelPluginCommand
import io.github.yoonseo.pastelplugin.rpg.moster.MonsterCommand
import io.github.yoonseo.pastelplugin.rpg.quest.QuestCommand

import io.github.yoonseo.pastelplugin.skillHelper.GlowingBlock
import io.github.yoonseo.pastelplugin.skillHelper.glowing
import io.github.yoonseo.pastelplugin.system.ListenerRegister
import io.github.yoonseo.pastelplugin.valorant.chamber.TourDeForce
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
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
        getCommand("setvalue")!!.setExecutor(AdjustableValueCommand())

        ListenerRegister.registerEvent("$projectPath.itemHandlers.EventHandler")

        TourDeForce()
        ThorHammer()
        Valagart()
        Friche()
        Fireball()
        command_juho {
            inventory.addItem(Maline().getItem())
        }

        autoReload()







        Bukkit.getLogger().info("PastelPlugin is Loaded")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        val keys = HashSet(GlowingBlock.onGlowing.keys)
        keys.forEach { debug(it); it.glowing(false) }
        Monster.list.forEach{
            it.mob?.remove()
        }
    }


    fun autoReload(){
        val file = File("/Users/yoonseo/Desktop/경제서버_시즌_3/plugins/PastelPlugin-1.0-SNAPSHOT.jar")
        val lastMod = file.lastModified()
        HeartbeatScope().launch {
            while (true){
                val curMod = file.lastModified()

                if(lastMod != curMod){
                    delay(1000)
                    val lastestMod = file.lastModified()
                    delay(500)
                    if(lastestMod == curMod){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"pm reload PastelPlugin")
                        debug("Auto Reloading pastelPlugin!!")
                    }

                }
                delay(1000)
            }

        }
    }
}

val plugin = PastelPlugin.plugin
const val projectPath = """io.github.yoonseo.pastelplugin"""
val overworld = Bukkit.getWorld("world")!!