package io.github.yoonseo.pastelplugin.rpg

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class QuestNode(val quest: Quest) {
    fun start(node : StartNode.() -> Unit){
        node(StartNode(quest))
    }
    fun end(node : EndNode.() -> Unit){
        node(EndNode(quest))
    }
    fun addSubQuest(subQuest: Quest){
        quest.subQuests.add(subQuest)
    }
}
class StartNode(val quest: Quest){
    fun questScreen(message : Component){
        quest.questScreen = message
    }
    fun conversation(vararg conversation : Component){
        quest.startConversation = conversation.toList()
    }
    fun subQuestEnable(){
        quest.mustCompleteSubs = true
    }

    fun execute(task : (Player.() -> Unit)){
        quest.startRunCode = task
    }
}
class EndNode(val quest: Quest){
    fun condition(endAt : Player.(Quest) -> Boolean){
        quest.endAt = endAt
    }
    fun conversation(vararg conversation : Component){
        quest.endConversation = conversation.toList()
    }
    fun nextQuest(nextQuest: Quest){
        quest.nextQuest = nextQuest
    }
    fun execute(task : (Player.() -> Unit)){
        quest.endRunCode = task
    }
}

