package io.github.yoonseo.pastelplugin.lib

import com.destroystokyo.paper.event.server.ServerExceptionEvent
import io.github.yoonseo.pastelplugin.PastelPlugin
import io.github.yoonseo.pastelplugin.command_juho
import io.github.yoonseo.pastelplugin.mergeWithSeparator
import io.github.yoonseo.pastelplugin.toComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object Debugger {
    private val lines = ArrayList<Component>()

    var isInitialized = false
    const val SIMPLESTACKTRACE = true
    const val ERRORLINE = "--------------------[ERROR]"
    fun init(){
        isInitialized = true
        val listener = object : Listener {
            @EventHandler
            fun exceptionHandler(e : ServerExceptionEvent){
                lines.addAll(processException(e.exception))

                flush()
            }
            fun processException(e : Throwable) : List<Component>{
                val lines = ArrayList<Component>()
                with(e){
                    lines.add(Component.text("$this").color(NamedTextColor.RED))

                    var lastLine = -1
                    if(SIMPLESTACKTRACE){
                        for((i,element) in stackTrace.withIndex()){
                            if(element.toString().contains("pastelPlugin",true)) lastLine = i
                        }
                    }
                    for((i,e) in stackTrace.withIndex()){
                        if(i == lastLine+1 && SIMPLESTACKTRACE) {
                            val lineBesides =  stackTrace.size - lastLine

                            lines.add(Component.text("[ Trace Simplified ] : $lineBesides lines Behind").color(NamedTextColor.GRAY))
                            break
                        }

                        val trace = e.toString().replace("io.github.yoonseo.pastelplugin.","").replaceBefore("//","").replace("//","")
                        var emphasize = false
                        if(e.toString().contains("pastelPlugin",true)){
                            emphasize = true
                        }
                        val temp = "  ╠ $trace".toComponent()
                        if(emphasize) temp.color(NamedTextColor.YELLOW)
                        lines.add(temp)

                    }


                    cause?.let {
                        lines.add(Component.empty())
                        if(cause!!.cause != null){
                            lines.add("Caused By : ".toComponent())
                            lines.addAll(processException(cause!!))
                        }else {
                            val com = processException(it).mergeWithSeparator(Component.text("\n"))

                            lines.add(Component.text("[ Cause Found : Hover to see ]").hoverEvent(
                                HoverEvent.showText(com)
                            ))
                        }


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

