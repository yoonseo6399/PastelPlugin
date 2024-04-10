package io.github.yoonseo.pastelplugin.rpg.quest

import io.github.yoonseo.pastelplugin.waitForCondition
import org.bukkit.entity.Player

data class QuestSession(val quest: Quest, val session: Session){

    lateinit var conditionNode: ConditionNode
    lateinit var conversationNode: ConversationNode
    lateinit var executableNode: ExecutableNode



    suspend fun startSession(player: Player){
        waitForCondition { conditionNode(player, quest) }
        conversationNode.print(player)
        executableNode()
    }
}

