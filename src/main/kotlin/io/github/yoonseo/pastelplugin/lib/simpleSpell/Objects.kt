package io.github.yoonseo.pastelplugin.lib.simpleSpell

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.getOverworldLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Spell



// Spell createByShape (Shape) addGradual { <GradualCommands> } period by 5

infix fun Spell.create(block : SkillScope.() -> Unit){
}



fun stop() = Circle centered getOverworldLocation(0,0,0) makeByRadius 0.0





class SkillScope : CoroutineScope by HeartbeatScope() {

}