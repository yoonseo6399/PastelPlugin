package io.github.yoonseo.pastelplugin.rpg.quest.impl

import io.github.yoonseo.pastelplugin.getOverworldLocation
import io.github.yoonseo.pastelplugin.rpg.quest.Npc
import io.github.yoonseo.pastelplugin.rpg.quest.Quest
import io.github.yoonseo.pastelplugin.rpg.quest.Session
import io.github.yoonseo.pastelplugin.rpg.quest.create
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Quests{



    val woodHunter = Npc(Component.text("aaa"), getOverworldLocation(-530,42,29346))
    val cacerin = Npc(Component.text("캐서린"), getOverworldLocation(-519,36,29067))
    val me = Npc(Component.text("나"), getOverworldLocation(0,0,0))

    fun starting() = Quest.create(Component.text("Tutorial")) {
        Session.Begin.open {
            condition {
                approachAt(woodHunter.loc,10f)
            }
            conversation {
                tell("아버지:아들아 진짜 갈꺼니?.....")
                tell("나:네 아버지 저는 개쩌는 모험가가 될꺼에요!")
                tell("아버지:그래....아들아 꼭 살아 돌아 오거라...")
                tell("나:예 아버지!")

                QuestScreen(Component.text(" 길을 따라 숲을 벗어나세요 "))
            }
        }
        Session.Middle.open {
            condition {
                approachAt(getOverworldLocation(-539,34,29268),10f)
            }
            conversation {
                QuestScreen(Component.text("모험가 길드에 가서 캐서린에게 말걸고 모험가 등록 하기"))
            }
        }
        Session.End.open {
            condition {
                approachAt(cacerin.loc,3f)
            }
            conversation {
                cacerin.talk("안녕하세요!!")
                cacerin.talk("무엇을 도와 드릴까요?")
                me.talk("모험가 등록 하러 왔어요")
                cacerin.talk("모험가 되었어요!")
                tell("QUEST END")
            }
        }
    }
}