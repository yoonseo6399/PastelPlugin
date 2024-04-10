package io.github.yoonseo.pastelplugin.rpg.quest

import io.github.yoonseo.pastelplugin.waitForCondition
import org.bukkit.entity.Player

data class QuestSession(val quest: Quest, val session: Session){

    var conditionNode: ConditionNode? = null
    var conversationNode: ConversationNode? = null
    var executableNode: ExecutableNode? = null



    suspend fun startSession(player: Player){
        waitForCondition { conditionNode?.let { it(player, quest) } == true }
        conversationNode?.print(player)
        executableNode?.let { it() }
    }
}

