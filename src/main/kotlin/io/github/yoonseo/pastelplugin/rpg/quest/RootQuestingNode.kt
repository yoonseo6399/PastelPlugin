package io.github.yoonseo.pastelplugin.rpg.quest

class RootQuestingNode(val quest: Quest) {
    fun Session.open(node: QuestingNode.() -> Unit){
        val session = QuestSession(quest, this)
        node(QuestingNode(session))
        quest.sessions.add(session)
    }
}