package io.github.yoonseo.pastelplugin.rpg.quest.impl

import io.github.yoonseo.pastelplugin.rpg.quest.Npc
import io.github.yoonseo.pastelplugin.rpg.quest.Quest
import io.github.yoonseo.pastelplugin.rpg.quest.Session
import io.github.yoonseo.pastelplugin.rpg.quest.create
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Quests{
    val cacerin = Npc(Component.text("aaa"))

    fun starting() = Quest.create(Component.text("SEXY BOY")) {
        Session.Begin.open {
            condition {
                obtain(ItemStack(Material.STONE),10)
            }


            conversation {
                cacerin.talk(Component.text("aaa"))
                cacerin.talk(Component.text("ccc"))
                cacerin.talk(Component.text("dd"))
                executes {
                    giveItem( ItemStack(Material.STICK,4))
                }
                option {
                    choice(Component.text("[ 111 ]")){
                        cacerin.talk(Component.text("aaa1"))

                    }
                    choice(Component.text("[ 222 ]")){
                        cacerin.talk(Component.text("aaa2"))
                    }
                }
            }
            executes {
                giveItem(ItemStack(Material.STICK,4))
            }
        }
        Session.End.open {
            condition {
                obtain(ItemStack(Material.IRON_INGOT),10)
            }
            conversation {
                cacerin.talk(Component.text("wow"))
            }
        }
    }
}