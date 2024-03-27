package io.github.yoonseo.pastelplugin.rpg

import io.github.yoonseo.pastelplugin.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Quests {
    val me = Component.text("[ 나 ] ").color(NamedTextColor.GRAY)
    val cacerin = Component.text("[ 캐서린 ] ").color(NamedTextColor.GREEN)
    val miner = Component.text("[ 광부 ] ").color(NamedTextColor.DARK_GRAY)

    val cacerinL = getOverworldLocation(-570, 35, 29233)
    val minerL = getOverworldLocation(-620, 30, 29289)
    val libL = getOverworldLocation(-564, 36, 29159)
    fun starting() = Quest {

        start {


            questScreen(Quest.QuestLabel + Component.text("길드에서 캐서린 만나기"))

        }

        end {
            condition {
                approachAt(cacerinL,1f)
            }
            conversation(
                cacerin + Component.text("길드에 온것을 환영해!").color(NamedTextColor.WHITE),
                cacerin + Component.text("무엇을 도와줄까?").color(NamedTextColor.WHITE),
                me + Component.text("모험가를 등록하고싶어요").color(NamedTextColor.WHITE),
                cacerin + Component.text("그럼 우선 모험가의 자질이 있는지 확인해볼까?").color(NamedTextColor.WHITE),
                cacerin + Component.text("이 마법 수정구슬에 손을 올려봐").color(NamedTextColor.WHITE),
                cacerin + Component.text("음.. 너는 운과 전투능력만 빼면 실력이 좋은걸?").color(NamedTextColor.WHITE),
                cacerin + Component.text("모험가를 등록하고 싶다면 등록금 5에메랄드가 필요해").color(NamedTextColor.WHITE),
                me + Component.text("앗 돈이 없는데 어떻게 벌죠?").color(NamedTextColor.WHITE),
                cacerin + Component.text("돈을 버는방법은 여러가지가 있지, 그중 나는 광질을 추천해!").color(NamedTextColor.WHITE),
                cacerin + Component.text("마침 길드에서 이벤트로 기초광질 세트를 나눠주거든").color(NamedTextColor.WHITE),
                me + Component.text("오 좋네요 그방법으로 할게요").color(NamedTextColor.WHITE),
                cacerin + Component.text("자 여기 기초세트고.. 나머지 설명들은 광부에게 찾아가봐!").color(NamedTextColor.WHITE),
            )
            execute {
                inventory.addItem(ItemStack(Material.IRON_PICKAXE))
            }
            nextQuest(findMine())
        }
    }
    fun findMine() = Quest{
        start {
            questScreen(Quest.QuestLabel + Component.text("광부를 찾아가기"))


        }

        end {
            condition {
                approachAt(minerL,1f)
            }
            conversation(
                miner + Component.text("당신이 캐서린이 말한 뉴비 광부인가? 허허").color(NamedTextColor.WHITE),
                me + Component.text("네 캐서린님이 돈버는 기초적인 방법으로 이걸 소개시켜 주셨어요").color(NamedTextColor.WHITE),
                miner + Component.text("좋아 일단 뉴비이니 석탄과 돌을 캐는것으로 시작해보자").color(NamedTextColor.WHITE),
                miner + Component.text("캐서린에게 받은 곡괭이로 밑에있는 철을 캐보게나").color(NamedTextColor.WHITE),
                miner + Component.text("하지만 조심하게.. 아래로 내려갈수록 가치있는 광물들이 많지만").color(NamedTextColor.WHITE),
                miner + Component.text("위험도 도사리고있다네..").color(NamedTextColor.RED),
            )
            nextQuest(mineCoal())
        }
    }
    fun mineCoal() = Quest {
        start {
            questScreen(Quest.QuestLabel + Component.text("캐서린이 준 곡괭이로 철 원석 10개 캐고 광부에게 찾아가기"))
        }
        end {
            condition {
                inventory.containsAtLeast(ItemStack(Material.RAW_IRON),10) && approachAt(minerL,1f)
            }
            conversation(
                me + Component.text("여기 철 10개 캤어요").color(NamedTextColor.WHITE),
                miner + Component.text("좋아 내가 특별히 널 위해 원석 10개를 에메랄드 5개로 사주도록 하지").color(NamedTextColor.WHITE),
                miner + Component.text("일단 이걸로 등록금을 지불하게나.").color(NamedTextColor.WHITE),
                me + Component.text("감사합니다").color(NamedTextColor.WHITE),
            )
            execute {
                inventory.removeItem(ItemStack(Material.RAW_IRON,10))
                inventory.addItem(ItemStack(Material.EMERALD,10))
            }
            nextQuest(meetCacerin())
        }
    }
    fun meetCacerin() = Quest{
        start {
            questScreen(Quest.QuestLabel + Component.text("캐서린에게 다시 찾아가기"))
            execute {
                inventory.removeItem(ItemStack(Material.EMERALD,5))
            }
        }

        end {
            condition {
                approachAt(cacerinL,1f)
            }
            conversation(
                cacerin + Component.text("에메랄드 5개를 가지고 왔니?").color(NamedTextColor.WHITE),
                me + Component.text("네 길드에 모험가로 등록해 주세요").color(NamedTextColor.WHITE),
                cacerin + Component.text("좋아 이제 너의 첫번째 모험퀘스트야").color(NamedTextColor.WHITE),
                cacerin + Component.text("평원으로 가서 슬라임 5마리를 잡아서 돌아와").color(NamedTextColor.WHITE),
                cacerin + Component.text("보수는 에메랄드 3개야").color(NamedTextColor.WHITE),
                Component.text(""),
                cacerin + Component.text("아 그리고, 어디로 가야할지 모르겠다면 도서관에 들려").color(NamedTextColor.WHITE),
            )
            nextQuest(goLib())

        }
    }
    fun goLib() = Quest{
        start {
            questScreen(Quest.QuestLabel + Component.text("도서관에 가 평원에 관련된 서적 확인하기"))
        }

        end {
            condition {
                approachAt(libL,1f)
            }
        }
    }
}