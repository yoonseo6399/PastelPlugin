package io.github.yoonseo.pastelplugin.lib

import com.destroystokyo.paper.event.server.ServerExceptionEvent
import io.github.yoonseo.pastelplugin.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object Debugger {
    private val lines = ArrayList<Component>()

    var isInitialized = false
    const val SIMPLE_STACKTRACE = true
    var lastException : Throwable? = null
    var repeatingWarning : Boolean = false
    val ERROR_LINE = Component.text("꒰⚘݄꒱₊_________________________________[ERROR]_________________________________₊꒰݄⚘꒱").color(NamedTextColor.RED)
    fun init(){
        isInitialized = true
        val listener = object : Listener {
            @EventHandler
            fun exceptionHandler(e : ServerExceptionEvent){
                if(lastException == e.exception) {
                    if(!repeatingWarning) lines.add(Component.text("Error Constantly Occur"))
                    repeatingWarning = true
                }else {
                    repeatingWarning = false
                    lines.add(ERROR_LINE)
                    lines.addAll(processException(e.exception))
                }

                flush()
            }
            fun processException(e : Throwable) : List<Component>{
                val lines = ArrayList<Component>()
                with(e){
                    lines.add(Component.text("$this").color(NamedTextColor.RED))

                    var lastLine = -1
                    if(SIMPLE_STACKTRACE){
                        for((i,element) in stackTrace.withIndex()){
                            if(element.toString().contains("pastelPlugin",true)) lastLine = i
                        }
                    }
                    for((i,e) in stackTrace.withIndex()){
                        if(i == lastLine+1 && SIMPLE_STACKTRACE) {
                            val lineBesides =  stackTrace.size - lastLine

                            lines.add(Component.text("[ Trace Simplified ] : $lineBesides lines Behind").color(NamedTextColor.GRAY))
                            break
                        }

                        var emphasize = false
                        if(e.toString().contains("pastelPlugin",true)){
                            emphasize = true
                        }
                        val trace = e.toString().replace("io.github.yoonseo.pastelplugin.","").replaceBefore("//","").replace("//","")
                        var temp = Component.text("  ╠ $trace")
                        if(emphasize) temp = temp.color(NamedTextColor.YELLOW)
                        lines.add(temp)

                    }


                    cause?.let { throwable ->
                        lines.add(Component.empty())
                        //if(cause!!.cause != null){
                        //    lines.add("Caused By : ".toComponent())
                        //    lines.addAll(processException(cause!!))
                        //}else {
                            val com = processException(throwable).map { Component.text("[ Cause ] ").color(NamedTextColor.GRAY) + it }.mergeWithSeparator(Component.text("\n"))

                            lines.add(Component.text("[ Cause Found : Click to see ]").color(NamedTextColor.WHITE).clickEvent(
                                ClickEvent.callback { if(it is Player) it.performCommand("tellraw @s ${JSONComponentSerializer.json().serialize(com)}") }
                            ))
                        //}


                    }
                }
                return lines
            }
        }
        Bukkit.getPluginManager().registerEvents(listener,PastelPlugin.plugin)
    }

    fun flush(){
        lines.forEach{
            command_juho()?.sendMessage(it)
        }
    }
}

