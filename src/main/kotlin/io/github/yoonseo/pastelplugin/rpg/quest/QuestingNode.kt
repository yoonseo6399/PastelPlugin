package io.github.yoonseo.pastelplugin.rpg.quest

class QuestingNode(private val session: QuestSession) {
    fun condition(conditionNode: ConditionNode.() -> Unit){
        session.conditionNode = ConditionNode().also { conditionNode(it) }
    }
    fun conversation(conversationNode: ConversationNode.() -> Unit){
        session.conversationNode = ConversationNode().also { conversationNode(it) }
    }
    fun executes(executableNode: ExecutableNode.() -> Unit){
        session.executableNode = ExecutableNode(session).also { executableNode(it) }
    }
}