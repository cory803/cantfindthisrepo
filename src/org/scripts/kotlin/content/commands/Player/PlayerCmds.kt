package com.runelive.commands.ranks

import com.runelive.GameSettings
import com.runelive.model.*
import com.runelive.model.Locations.Location
import com.runelive.model.definitions.ItemDefinition
import com.runelive.model.input.impl.ChangePassword
import com.runelive.model.player.GameMode
import com.runelive.util.ForumDatabase
import com.runelive.util.Misc
import com.runelive.world.World
import com.runelive.world.content.Command
import com.runelive.world.content.PlayersOnlineInterface
import com.runelive.world.content.combat.CombatFactory
import com.runelive.world.content.combat.DesolaceFormulas
import com.runelive.world.content.skill.SkillManager
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering
import com.runelive.world.content.transportation.TeleportHandler
import com.runelive.world.entity.impl.player.Player
import java.text.DecimalFormat

object PlayerCmds {

    @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
        if (player.isJailed) {
            player.packetSender.sendMessage("You cannot use commands in jail... You're in jail.")
            return
        }
        if (command[0] == "switchmode") {
            try {
                val mode = command[1]
                when (mode) {
                    "sir"-> {
                        player.gameModeAssistant.setNewGamemode(GameMode.SIR)
                        player.packetSender.sendMessage("Your game mode has been changed to "+player.gameModeAssistant.gameMode+".")
                        for (skill in Skill.values()) {
                            val level = if (skill == Skill.CONSTITUTION) 100 else if (skill == Skill.PRAYER) 10 else 1
                            player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                                    SkillManager.getExperienceForLevel(if (skill == Skill.CONSTITUTION) 10 else 1))
                        }
                        player.packetSender.sendMessage("Your skill levels have now been reset.")
                    }
                     "lord" -> {
                        player.gameModeAssistant.setNewGamemode(GameMode.LORD)
                        player.packetSender.sendMessage("Your game mode has been changed to "+player.gameModeAssistant.gameMode+".")
                         for (skill in Skill.values()) {
                             val level = if (skill == Skill.CONSTITUTION) 100 else if (skill == Skill.PRAYER) 10 else 1
                             player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                                     SkillManager.getExperienceForLevel(if (skill == Skill.CONSTITUTION) 10 else 1))
                         }
                         player.packetSender.sendMessage("Your skill levels have now been reset.")
                    }
                    "legend" -> {
                        player.gameModeAssistant.setNewGamemode(GameMode.LEGEND)
                        player.packetSender.sendMessage("Your game mode has been changed to "+player.gameModeAssistant.gameMode+".")
                        for (skill in Skill.values()) {
                            val level = if (skill == Skill.CONSTITUTION) 100 else if (skill == Skill.PRAYER) 10 else 1
                            player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                                    SkillManager.getExperienceForLevel(if (skill == Skill.CONSTITUTION) 10 else 1))
                        }
                        player.packetSender.sendMessage("Your skill levels have now been reset.")
                    }
                    "extreme" -> {
                        player.gameModeAssistant.setNewGamemode(GameMode.EXTREME)
                        player.packetSender.sendMessage("Your game mode has been changed to "+player.gameModeAssistant.gameMode+".")
                        for (skill in Skill.values()) {
                            val level = if (skill == Skill.CONSTITUTION) 100 else if (skill == Skill.PRAYER) 10 else 1
                            player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                                    SkillManager.getExperienceForLevel(if (skill == Skill.CONSTITUTION) 10 else 1))
                        }
                        player.packetSender.sendMessage("Your skill levels have now been reset.")
                    }
                    "realism" -> {
                        player.gameModeAssistant.setNewGamemode(GameMode.REALISM)
                        player.packetSender.sendMessage("Your game mode has been changed to "+player.gameModeAssistant.gameMode+".")
                        for (skill in Skill.values()) {
                            val level = if (skill == Skill.CONSTITUTION) 100 else if (skill == Skill.PRAYER) 10 else 1
                            player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                                    SkillManager.getExperienceForLevel(if (skill == Skill.CONSTITUTION) 10 else 1))
                        }
                        player.packetSender.sendMessage("Your skill levels have now been reset.")
                    }
                    else -> player.packetSender.sendMessage("Command not found - Use sir, lord, legend, extreme or realism.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (command[0] == "find") {
            val name = wholeCommand.substring(5).toLowerCase().replace("_".toRegex(), " ")
            player.packetSender.sendMessage("Finding item id for item - " + name)
            var found = false
            for (i in 0..ItemDefinition.getMaxAmountOfItems() - 1) {
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.packetSender.sendMessage("Found item with name ["
                            + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i)
                    found = true
                }
            }
            if (!found) {
                player.packetSender.sendMessage("No item with name [$name] has been found!")
            }
        }
        if(command[0] == "master") {
            player.skillManager.setMaxLevel(Skill.ATTACK, 99)
            player.skillManager.setMaxLevel(Skill.DEFENCE, 99)
            player.skillManager.setMaxLevel(Skill.STRENGTH, 99)
            player.skillManager.setMaxLevel(Skill.CONSTITUTION, 990)
            player.skillManager.setMaxLevel(Skill.PRAYER, 990)
            player.skillManager.setMaxLevel(Skill.RANGED, 99)
            player.skillManager.setMaxLevel(Skill.MAGIC, 99)
            player.skillManager.setMaxLevel(Skill.SUMMONING, 99)
            player.skillManager.setMaxLevel(Skill.DUNGEONEERING, 80)
            player.skillManager.setCurrentLevel(Skill.ATTACK, 99)
            player.skillManager.setCurrentLevel(Skill.DEFENCE, 99)
            player.skillManager.setCurrentLevel(Skill.STRENGTH, 99)
            player.skillManager.setCurrentLevel(Skill.CONSTITUTION, 990)
            player.skillManager.setCurrentLevel(Skill.PRAYER, 990)
            player.skillManager.setCurrentLevel(Skill.RANGED, 99)
            player.skillManager.setCurrentLevel(Skill.MAGIC, 99)
            player.skillManager.setCurrentLevel(Skill.SUMMONING, 99)
            player.skillManager.setCurrentLevel(Skill.DUNGEONEERING, 80)
            player.skillManager.setExperience(Skill.ATTACK, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.DEFENCE, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.STRENGTH, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.CONSTITUTION, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.PRAYER, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.RANGED, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.MAGIC, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.SUMMONING, SkillManager.getExperienceForLevel(99))
            player.skillManager.setExperience(Skill.DUNGEONEERING, SkillManager.getExperienceForLevel(80))
            player.packetSender.sendMessage("You are now a master of all combat skills.")
            player.updateFlag.flag(Flag.APPEARANCE)
        }
        if (command[0] == "setlevel" || command[0] == "setlvl" || command[0] == "lvl") {
            val skillId = Integer.parseInt(command[1])
            val level = Integer.parseInt(command[2])
            if (level > 99) {
                player.packetSender.sendMessage("You can only have a maxmium level of 99.")
                return
            }
            val skill = Skill.forId(skillId)
            player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                    SkillManager.getExperienceForLevel(level))
            player.packetSender.sendMessage("You have set your " + skill.getName() + " level to " + level)
        }
        if (command[0] == "item") {
            val id = Integer.parseInt(command[1])
            var amount = if (command.size == 2)
                1
            else
                Integer.parseInt(command[2].trim { it <= ' ' }.toLowerCase().replace("k".toRegex(), "000").replace("m".toRegex(), "000000").replace("b".toRegex(), "000000000"))
            if (amount > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE
            }
            val item = Item(id, amount)
            player.inventory.add(item, true)

            player.packetSender.sendItemOnInterface(47052, 11694, 1)
        }
        if (command[0] == "spawn") {
            val name = wholeCommand.substring(6, wholeCommand.indexOf(":")).toLowerCase().replace("_".toRegex(), " ")
            val what = wholeCommand.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val amount_of = Integer.parseInt(what[1])
            player.packetSender.sendMessage("Finding item id for item - " + name)
            var found2 = false
            for (i in 0..ItemDefinition.getMaxAmountOfItems() - 1) {
                if (found2)
                    break
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.inventory.add(i, amount_of)
                    player.packetSender.sendMessage(
                            "Spawned item [" + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i)
                    found2 = true
                }
            }
            if (!found2) {
                player.packetSender.sendMessage("No item with name [$name] has been found!")
            }
            player.packetSender.sendItemOnInterface(47052, 11694, 1)
        }
        if(command[0].equals("mode", ignoreCase = true)) {
            player.forceChat("[RuneLive] My game mode is "+player.gameModeAssistant.gameMode+".")
        }
        if (command[0].equals("bosses", ignoreCase = true)) {
            player.forceChat("[RuneLive] " + player.username + " has slain " + player.bossPoints + " bosses.")
        }
        if (command[0].equals("kdr", ignoreCase = true)) {
            val KDR = player.playerKillingAttributes.playerKills / player.playerKillingAttributes.playerDeaths
            val df = DecimalFormat("#.00")
            player.forceChat(
                    "[RuneLive] My Kill to Death ration is " + player.playerKillingAttributes.playerKills
                            + " kills to " + player.playerKillingAttributes.playerDeaths
                            + " deaths, which is " + df.format(KDR.toLong()) + " K/D.")
        }
        if (command[0].equals("time", ignoreCase = true)) {
            player.forceChat("[runelive] " + player.username + " has played for ["
                    + Misc.getHoursPlayed(player.totalPlayTime + player.recordedLogin.elapsed()) + "]")
        }
         if (command[0].equals("commands", ignoreCase = true)) {
            if (player.location === Location.DUNGEONEERING) {
                player.packetSender.sendMessage("You cannot open the commands in dungeoneering.")
                return
            }
            player.packetSender.sendTab(GameSettings.QUESTS_TAB)
            Command.open(player)
        }
        if (command[0] == "skull") {
            if (player.skullTimer > 0) {
                player.packetSender.sendMessage("You are already skulled!")
                return
            } else {
                CombatFactory.skullPlayer(player)
            }
        }
        if (command[0] == "forumrank") {
            if (player.forumConnections > 0) {
                player.packetSender.sendMessage("You have just used this command, please relog and try again!")
                return
            }
            if (!GameSettings.FORUM_DATABASE_CONNECTIONS) {
                player.packetSender.sendMessage("This is currently disabled, try again in 30 minutes!")
                return
            }
            if (!player.rights.isStaff) {
                try {
                    player.addForumConnections(60)
                    ForumDatabase.forumRankUpdate(player)
                    player.packetSender.sendMessage("Your in-game rank has been added to your forum account.")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                player.packetSender.sendMessage("Staff members are not allowed to use this command.")
            }
        }
        if (wholeCommand.equals("donate", ignoreCase = true) || wholeCommand.equals("store", ignoreCase = true)) {
            if (!GameSettings.STORE_CONNECTIONS) {
                player.packetSender.sendMessage("The Store is currently turned off, please try again in 30 minutes!")
                return
            }
            player.packetSender.sendString(1, "www.rune.live/store/")
            player.packetSender.sendMessage("Attempting to open: www.rune.live/store/")
        }
        if (wholeCommand.equals("wiki", ignoreCase = true)) {
            player.packetSender.sendString(1, "www.runelive-2.wikia.com/wiki/runelive_2_Wikia")
            player.packetSender.sendMessage("Attempting to open: www.rune.live/wiki/")
        }
        if (wholeCommand.startsWith("auth")) {
            if (!GameSettings.VOTING_CONNECTIONS) {
                player.packetSender.sendMessage("Voting connections are currently turned off, try again in 30 minutes!")
                return
            }
            if (!player.voteTimer.elapsed(30000)) {
                player.packetSender.sendMessage("You have to wait 30 seconds in order to use ::auth!")
                return
            }
            player.voteTimer.reset()
            Voting.useAuth(player, command[1])
        }
        if (command[0].equals("attacks", ignoreCase = true)) {
            val attack = DesolaceFormulas.getMeleeAttack(player)
            val range = DesolaceFormulas.getRangedAttack(player)
            val magic = DesolaceFormulas.getMagicAttack(player)
            player.packetSender.sendMessage("@bla@Melee attack: @or2@$attack@bla@, ranged attack: @or2@$range@bla@, magic attack: @or2@$magic")
        }
        if (command[0] == "save") {
            player.save()
            player.packetSender.sendMessage("Your progress has been saved.")
        }
        if (command[0] == "vote") {
            if (!GameSettings.VOTING_CONNECTIONS) {
                player.packetSender.sendMessage("Voting is currently turned off, please try again in 30 minutes!")
                return
            }
            player.packetSender.sendString(1, "www.rune.live/vote/")
            player.packetSender.sendMessage("Attempting to open: www.rune.live/vote/")
        }
        if (command[0] == "help" || command[0] == "support") {
            player.packetSender.sendString(1,
                    "wwwrune.live/forum/index.php?app=core&module=global&section=register")
            player.packetSender.sendMessage("Attempting to open: www.rune.live/forum/?app=tickets")
            player.packetSender.sendMessage("Please note this requires you to register on the forums, type ::register!")
        }
        if (command[0] == "register") {
            player.packetSender.sendString(1,
                    "www.rune.live/forum/index.php?app=core&module=global&section=register")
            player.packetSender.sendMessage(
                    "Attempting to open: www.rune.live/forum/index.php?app=core&module=global&section=register")
        }
        if (command[0] == "forum" || command[0] == "forums") {
            player.packetSender.sendString(1, "www.rune.live/forum/")
            player.packetSender.sendMessage("Attempting to open: www.rune.live/forum/")
        }
        if (command[0] == "scores" || command[0] == "hiscores" || command[0] == "highscores") {
            if (!GameSettings.HIGHSCORE_CONNECTIONS) {
                player.packetSender.sendMessage("Hiscores is currently turned off, please try again in 30 minutes!")
                return
            }
            player.packetSender.sendString(1, "www.rune.live/hiscores/")
            player.packetSender.sendMessage("Attempting to open: www.rune.live/hiscores/")
        }
        if (command[0] == "thread") {
            val thread = Integer.parseInt(command[1])
            player.packetSender.sendString(1,
                    "www.rune.live/forum/index.php?/topic/$thread-threadcommand/")
            player.packetSender.sendMessage("Attempting to open: Thread " + thread)
        }
        if (command[0].startsWith("changepass") || command[0].equals("password")) {
            player.inputHandling = ChangePassword()
            player.packetSender.sendEnterInputPrompt("Enter a new password:")
        }
        if (command[0] == "home") {
            if (Dungeoneering.doingDungeoneering(player)) {
                player.packetSender.sendMessage("You can't use this command in a dungeon.")
                return
            }
            if (player.location != null && player.wildernessLevel > 20) {
                player.packetSender.sendMessage("You cannot do this at the moment.")
                return
            }
            val position = Position(3212, 3428, 0)
            TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
            player.packetSender.sendMessage("Teleporting you home!")
        }
        if (command[0] == "train") {
            if (Dungeoneering.doingDungeoneering(player)) {
                player.packetSender.sendMessage("You can't use this command in a dungeon.")
                return
            }
            if (player.location != null && player.wildernessLevel > 20) {
                player.packetSender.sendMessage("You cannot do this at the moment.")
                return
            }
            val position = Position(2679, 3720, 0)
            TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
            player.packetSender.sendMessage("Teleporting you to rock crabs!")
        }
        if (command[0] == "edge") {
            if (Dungeoneering.doingDungeoneering(player)) {
                player.packetSender.sendMessage("You can't use this command in a dungeon.")
                return
            }
            if (player.location != null && player.wildernessLevel > 20) {
                player.packetSender.sendMessage("You cannot do this at the moment.")
                return
            }
            val position = Position(3087, 3502, 0)
            TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
            player.packetSender.sendMessage("Teleporting you to edgeville!")
        }
        if (command[0] == "duel") {
            if (Dungeoneering.doingDungeoneering(player)) {
                player.packetSender.sendMessage("You can't use this command in a dungeon.")
                return
            }
            if (player.location != null && player.wildernessLevel > 20) {
                player.packetSender.sendMessage("You cannot do this at the moment.")
                return
            }
            if (player.location === Location.DUEL_ARENA) {
                player.packetSender.sendMessage("You can't do this right now.")
                return
            }
            val position = Position(3370, 3267, 0)
            TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
            player.packetSender.sendMessage("Teleporting you to the duel arena!")
        }
        if (command[0] == "teamspeak") {
            player.packetSender.sendMessage("Teamspeak address: ts3.rune.live")
        }
        if (command[0] == "market") {
            if (Dungeoneering.doingDungeoneering(player)) {
                player.packetSender.sendMessage("You can't use this command in a dungeon.")
                return
            }
            if (player.location != null && player.wildernessLevel > 20) {
                player.packetSender.sendMessage("You cannot do this at the moment.")
                return
            }
            val random = Misc.getRandom(3)
            when (random) {
                0 -> TeleportHandler.teleportPlayer(player, Position(3212, 3429, 0),
                        player.spellbook.teleportType)
                1 -> TeleportHandler.teleportPlayer(player, Position(3213, 3429, 0),
                        player.spellbook.teleportType)
                2 -> TeleportHandler.teleportPlayer(player, Position(3213, 3428, 0),
                        player.spellbook.teleportType)
                3 -> TeleportHandler.teleportPlayer(player, Position(3212, 3428, 0),
                        player.spellbook.teleportType)
            }
            player.packetSender.sendMessage("Welcome to the Market!")
        }
        if (command[0] == "claim") {
            if (!GameSettings.STORE_CONNECTIONS) {
                player.packetSender.sendMessage("The store is currently offline! Try again in 30 minutes.")
                return
            }
            if (player.location == Location.DUNGEONEERING || player.location == Location.WILDERNESS) {
                player.packetSender.sendMessage("You cannot do this here, you'll lose your scroll!")
                return
            }
            if (player.claimingStoreItems) {
                player.packetSender.sendMessage("You already have a active store claim process going...")
                return
            }
            player.packetSender.sendMessage("Checking for any store purchases...")
            Store.claimItem(player)
        }
        if (command[0] == "gamble") {
            if (Dungeoneering.doingDungeoneering(player)) {
                player.packetSender.sendMessage("You can't use this command in a dungeon.")
                return
            }
            if (player.location != null && player.wildernessLevel > 20) {
                player.packetSender.sendMessage("You cannot do this at the moment.")
                return
            }
            val position = Position(2441, 3090, 0)
            TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
            player.packetSender.sendMessage("Welcome to the Gambling Area, make sure you always use a middle man for high bets!")
            player.packetSender.sendMessage("Recording your stake will only get the player banned if they scam.")
        }
        if (command[0] == "players") {
            player.packetSender.sendInterfaceRemoval()
            PlayersOnlineInterface.showInterface(player)
        }
    }
}