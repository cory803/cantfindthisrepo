package org.scripts.kotlin.content.skills.thieving

import com.runelive.model.Animation
import com.runelive.model.Item
import com.runelive.model.Skill
import com.runelive.util.Misc
import com.runelive.world.content.Achievements
import com.runelive.world.content.Achievements.AchievementData
import com.runelive.world.content.tasks.DailyTaskManager
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.content.Emotes.Skillcape_Data
import com.scripts.kotlin.content.skills.thieving.ThievBots

class Stall {
    companion object Stalls {
        var botStop: Int = 0

        fun stealFromStall(player: Player, lvlreq: Int, xp: Int, reward: Int, message: String) {
            if (player.isPassedRandom) {
                botStop = Misc.getRandom(200)
            } else {
                if (botStop == 1) {
                    player.isPassedRandom = false
                    player.inputHandling = ThievBots()
                    player.packetSender.sendEnterInputPrompt("Please enter ikov2")
                } else if (botStop == 2) {
                    player.isPassedRandom = false
                    player.inputHandling = ThievBots()
                    player.packetSender.sendEnterInputPrompt("What is 10+5?")
                } else if (botStop == 3) {
                    player.isPassedRandom = false
                    player.inputHandling = ThievBots()
                    player.packetSender.sendEnterInputPrompt("Are you botting?")
                }
                return
            }
            if (player.inventory.freeSlots < 1) {
                player.packetSender.sendMessage("You need some more inventory space to do this.")
                return
            }
            if (player.combatBuilder.isBeingAttacked) {
                player.packetSender.sendMessage("You must wait a few seconds after being out of combat before doing this.")
                return
            }
            if (!player.clickDelay.elapsed(2000))
                return
            if (player.skillManager.getMaxLevel(Skill.THIEVING) < lvlreq) {
                player.packetSender.sendMessage(
                        "You need a Thieving level of at least $lvlreq to steal from this stall.")
                return
            }
            var amount = 1
            if (Skillcape_Data.THIEVING.isWearingCape(player) || Skillcape_Data.MASTER_THIEVING.isWearingCape(player) && Misc.inclusiveRandom(0, 10) == 0) {
                if (!player.inventory.isFull) {
                    amount++
                    player.packetSender.sendMessage("You manage to steal two at once!")
                }
            }
            player.performAnimation(Animation(881))
            player.packetSender.sendMessage(message)
            player.packetSender.sendInterfaceRemoval()
            player.skillManager.addExperience(Skill.THIEVING, xp)
            player.clickDelay.reset()
            player.inventory.add(reward, 1)
            player.skillManager.stopSkilling()
            if (reward == 15009)
                Achievements.finishAchievement(player, AchievementData.STEAL_A_RING)
            else if (reward == 11998) {
                Achievements.doProgress(player, AchievementData.STEAL_140_SCIMITARS)
                Achievements.doProgress(player, AchievementData.STEAL_5000_SCIMITARS)
                if (player.dailyTask == 2 && !player.completedDailyTask) {
                    DailyTaskManager.doTaskProgress(player)
                }
            }
        }

        fun stealFromStall2(player: Player, lvlreq: Int, xp: Int,
                            reward: Int, goldAmount: Int, message: String) {
            if (player.inventory.freeSlots < 1) {
                player.packetSender.sendMessage(
                        "You need some more inventory space to do this.")
                return
            }
            if (player.combatBuilder.isBeingAttacked) {
                player.packetSender.sendMessage(
                        "You must wait a few seconds after being out of combat before doing this.")
                return
            }
            if (!player.clickDelay.elapsed(2000))
                return
            if (player.skillManager.getCurrentLevel(Skill.THIEVING) < lvlreq) {
                player.packetSender.sendMessage(
                        "You need a Thieving level of at least $lvlreq to steal from this stall.")
                return
            }
            player.performAnimation(Animation(881))
            player.packetSender.sendMessage(message)
            player.packetSender.sendInterfaceRemoval()
            player.skillManager.addExperience(Skill.THIEVING, xp)
            player.clickDelay.reset()
            if (player.dailyTask == 2 && !player.completedDailyTask) {
                DailyTaskManager.doTaskProgress(player)
            }
            var amount = 1
            if (Skillcape_Data.THIEVING.isWearingCape(player) && Misc.inclusiveRandom(0, 10) == 0) {
                if (!player.inventory.isFull) {
                    amount++
                    player.packetSender.sendMessage("You manage to steal two at once!")
                }
            }
            player.inventory.add(reward, amount)
            player.inventory.add(995, goldAmount)
            player.skillManager.stopSkilling()
            if (reward == 15009)
                Achievements.finishAchievement(player, AchievementData.STEAL_A_RING)
            else if (reward == 11998) {
                Achievements.doProgress(player, AchievementData.STEAL_140_SCIMITARS, amount)
                Achievements.doProgress(player,
                        AchievementData.STEAL_5000_SCIMITARS, amount)
            }
        }

        fun stealFromStall(player: Player, lvlreq: Int, xp: Int, item: Item, message: String,
                           which_stall: Boolean) {
            if (player.isPassedRandom) {
                botStop = Misc.getRandom(200)
            } else {
                if (botStop == 1) {
                    player.isPassedRandom = false
                    player.inputHandling = ThievBots()
                    player.packetSender.sendEnterInputPrompt("Please enter ikov2")
                } else if (botStop == 2) {
                    player.isPassedRandom = false
                    player.inputHandling = ThievBots()
                    player.packetSender.sendEnterInputPrompt("What is 10+5?")
                } else if (botStop == 3) {
                    player.isPassedRandom = false
                    player.inputHandling = ThievBots()
                    player.packetSender.sendEnterInputPrompt("Are you botting?")
                }
                return
            }
            if (player.inventory.freeSlots < 1) {
                player.packetSender.sendMessage("You need some more inventory space to do this.")
                return
            }
            if (player.combatBuilder.isBeingAttacked) {
                player.packetSender.sendMessage("You must wait a few seconds after being out of combat before doing this.")
                return
            }
            if (!player.clickDelay.elapsed(1700))
                return
            if (player.skillManager.getMaxLevel(Skill.THIEVING) < lvlreq) {
                player.packetSender.sendMessage(
                        "You need a Thieving level of at least $lvlreq to steal from this stall.")
                return
            }
            player.performAnimation(Animation(881))
            player.packetSender.sendMessage("You steal " + item.amount + " coins.")
            if (which_stall) {
                val fishyType = Misc.getRandom(6)
                if (fishyType == 1) {
                    player.inventory.add(15271, Misc.getRandom(10))
                } else if (fishyType == 2) {
                    player.inventory.add(384, Misc.getRandom(10))
                } else if (fishyType == 3) {
                    player.inventory.add(3143, Misc.getRandom(10))
                }
            } else {
                val veggieStall = Misc.getRandom(32)
                var amount = Misc.getRandom(2)
                amount++
                when (veggieStall) {
                    1 -> player.inventory.add(200, amount)
                    2 -> player.inventory.add(202, amount)
                    3 -> player.inventory.add(204, amount)
                    4 -> player.inventory.add(206, amount)
                    5 -> player.inventory.add(208, amount)
                    6 -> player.inventory.add(210, amount)
                    7 -> player.inventory.add(212, amount)
                    8 -> player.inventory.add(214, amount)
                    9 -> player.inventory.add(216, amount)
                    10 -> player.inventory.add(218, amount)
                    11 -> player.inventory.add(220, amount)
                }
            }
            if (player.dailyTask == 2 && !player.completedDailyTask) {
                DailyTaskManager.doTaskProgress(player)
            }
            player.packetSender.sendInterfaceRemoval()
            player.skillManager.addExperience(Skill.THIEVING, xp)
            player.clickDelay.reset()
            player.inventory.add(item.id, item.amount)
            player.skillManager.stopSkilling()
        }
    }
}
