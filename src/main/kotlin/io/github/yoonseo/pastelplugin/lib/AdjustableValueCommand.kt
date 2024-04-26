package io.github.yoonseo.pastelplugin.lib

import io.github.yoonseo.pastelplugin.debug
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class AdjustableValueCommand : CommandExecutor{
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(p3 != null && p3.size == 3){
            val ins = AdjustableValue.getInstance(p3[0]) ?: return false
            debug("a")
            val succeed = ins.commandedChange(p3[1],p3[2])
            if(succeed) {
                p0.sendMessage("Property Named '${p3[0]}' value changed to ${p3[1]} Typed ${p3[2]}")
                return true
            } else return false
        }else return false
    }
}