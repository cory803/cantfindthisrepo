package org.scripts.kotlin.content.commands.staff

import com.runelive.GameServer
import com.runelive.GameSettings
import com.runelive.engine.task.Task
import com.runelive.engine.task.TaskManager
import com.runelive.model.*
import com.runelive.model.container.impl.Equipment
import com.runelive.model.definitions.ItemDefinition
import com.runelive.model.definitions.NpcDefinition
import com.runelive.model.definitions.WeaponAnimations
import com.runelive.model.definitions.WeaponInterfaces
import com.runelive.model.input.impl.SetPassword
import com.runelive.util.Misc
import com.runelive.world.World
import com.runelive.world.content.AccountTools
import com.runelive.world.content.BonusManager
import com.runelive.world.content.Scoreboard
import com.runelive.world.content.WellOfGoodwill
import com.runelive.world.content.clan.ClanChatManager
import com.runelive.world.content.combat.weapon.CombatSpecial
import com.runelive.world.content.grandexchange.GrandExchangeOffers
import com.runelive.world.content.minigames.impl.zulrah.Zulrah
import com.runelive.world.content.pos.PlayerOwnedShops
import com.runelive.world.content.skill.SkillManager
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering
import com.runelive.world.entity.impl.npc.NPC
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving
import java.io.IOException

/**
 * Created by Dave on 10/07/2016.
 */

class AdministratorCommands {

    object Administrators {

        @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
            if (wholeCommand.startsWith("globalyell")) {
                player.packetSender.sendMessage("Retype the command to renable/disable the yell channel.")
                World.setGlobalYell(!World.isGlobalYell())
                World.sendMessage("<img=4> @blu@The yell channel has been @dre@" + if (World.isGlobalYell()) "@dre@enabled@blu@ again!" else "@dre@disabled@blu@ temporarily!")
            }
            if (command[0] == "unskull") {
                player.skullTimer = 0
                player.skullIcon = 0
                player.updateFlag.flag(Flag.APPEARANCE)
                player.packetSender.sendMessage("You are unskulled!")
            }
            if (wholeCommand.equals("hp", ignoreCase = true)) {
                player.skillManager.setCurrentLevel(Skill.CONSTITUTION, 99999, true)
            }
            if (command[0] == "pos" || command[0] == "mypos" || command[0] == "coords") {
                player.packetSender.sendMessage(player.position.toString())
            }
            if (command[0].equals("saveall", ignoreCase = true)) {
                World.savePlayers()
                player.packetSender.sendMessage("Saved players!")
            }
            if (command[0].contains("host")) {
                val plr = wholeCommand.substring(command[0].length + 1)
                val playr2 = World.getPlayerByName(plr)
                if (playr2 != null) {
                    player.packetSender.sendMessage("" + playr2.username + " host IP: " + playr2.hostAddress
                            + ", serial number: " + playr2.serialNumber)
                } else
                    player.packetSender.sendMessage("Could not find player: " + plr)
            }
            if (wholeCommand == "afk") {
                World.sendMessage("<img=4> <col=FF0000><shad=0>" + player.username
                        + ": I am now away, please don't message me; I won't reply.")
            }
        }
    }

    object Managers {
        /**
         * @Author Jonathan Sirens Initiates Command
         */

        @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
            if (wholeCommand.equals("treasurekeys", ignoreCase = true)) {
                player.inventory.add(9725, 1)
                player.inventory.add(9722, 1)
                player.inventory.add(9724, 1)
                player.inventory.add(9722, 1)
            }
            if (wholeCommand.equals("vengrunes", ignoreCase = true)) {
                player.inventory.add(557, 1000)
                player.inventory.add(560, 1000)
                player.inventory.add(9075, 1000)
            }
            if (command[0] == "nopoison") {
                player.poisonDamage = 20
            }
            if (command[0] == "checkpass") {
                val victimUsername = wholeCommand.substring(10)
                PlayerSaving.accountExists(victimUsername) { rs ->
                    if (rs.next()) {
                        // account exists
                        val other = World.getPlayerByName(victimUsername)
                        if (other == null) {
                            AccountTools.checkPassword(player, victimUsername, Player(null))
                        } else {
                            val password = other.password
                            if (other.password != null) {
                                player.packetSender.sendMessage(
                                        "The player " + victimUsername + "'s password is: " + other.password + "")
                            }
                        }
                    } else {
                        player.packetSender.sendMessage("Player $victimUsername does not exist.")
                    }
                }
            }
            if (command[0] == "setpass") {
                val victimUsername = wholeCommand.substring(8)
                PlayerSaving.accountExists(victimUsername) { rs ->
                    if (rs.next()) {
                        // account exists
                        val other = World.getPlayerByName(victimUsername)
                        if (other == null) {
                            player.changingPasswordOf = victimUsername
                            player.inputHandling = SetPassword()
                            player.packetSender.sendEnterInputPrompt("(OFFLINE) Enter a new password for $victimUsername:")
                        } else {
                            player.changingPasswordOf = victimUsername
                            player.inputHandling = SetPassword()
                            player.packetSender.sendEnterInputPrompt("(ONLINE) Enter a new password for $victimUsername:")
                        }
                    } else {
                        player.packetSender.sendMessage("Player $victimUsername does not exist.")
                    }
                }
            }
            if (command[0] == "checkpin") {
                val victimUsername = wholeCommand.substring(9)
                PlayerSaving.accountExists(victimUsername) { rs ->
                    if (rs.next()) {
                        // account exists
                        val other = World.getPlayerByName(victimUsername)
                        if (other == null) {
                            AccountTools.checkPin(player, victimUsername, Player(null))
                        } else {
                            if (other.bankPinAttributes.hasBankPin()) {
                                val builder = StringBuilder()
                                for (s in other.bankPinAttributes.bankPin) {
                                    builder.append(s)
                                }
                                val pin = builder.toString()
                                if (pin != null) {
                                    player.packetSender.sendMessage("The player $victimUsername's account pin is: $pin")
                                }
                            } else {
                                player.packetSender.sendMessage("The player $victimUsername does not have an account pin.")
                            }
                        }
                    } else {
                        player.packetSender.sendMessage("Player $victimUsername does not exist.")
                    }
                }
            }
            if (command[0] == "resetpin") {
                val victimUsername = wholeCommand.substring(9)
                PlayerSaving.accountExists(victimUsername) { rs ->
                    if (rs.next()) {
                        // account exists
                        val other = World.getPlayerByName(victimUsername)
                        if (other == null) {
                            AccountTools.resetPin(player, victimUsername, Player(null))
                        } else {
                            if (other.bankPinAttributes.hasBankPin()) {
                                for (i in 0..other.bankPinAttributes.bankPin.size - 1) {
                                    other.bankPinAttributes.bankPin[i] = 0
                                    other.bankPinAttributes.enteredBankPin[i] = 0
                                }
                                other.bankPinAttributes.setHasBankPin(false)
                                player.packetSender.sendMessage("The player $victimUsername's account pin has been reset.")
                                World.deregister(other)
                            } else {
                                player.packetSender.sendMessage(
                                        "The player $victimUsername currently does not have an account pin.")
                            }
                        }
                    } else {
                        player.packetSender.sendMessage("Player $victimUsername does not exist.")
                    }
                }
            }
            if (wholeCommand.equals("propker", ignoreCase = true)) {
                player.skillManager.setCurrentLevel(Skill.ATTACK, 125, true)
                player.skillManager.setCurrentLevel(Skill.STRENGTH, 145, true)
                player.skillManager.setCurrentLevel(Skill.RANGED, 145, true)
                player.skillManager.setCurrentLevel(Skill.DEFENCE, 140, true)
                player.skillManager.setCurrentLevel(Skill.MAGIC, 120, true)
                player.skillManager.setCurrentLevel(Skill.PRAYER, 99999, true)
            }
            if (wholeCommand.equals("hp", ignoreCase = true)) {
                player.skillManager.setCurrentLevel(Skill.CONSTITUTION, 99999, true)
            }
            if (wholeCommand.equals("godmode", ignoreCase = true)) {
                player.skillManager.setCurrentLevel(Skill.ATTACK, 99999, true)
                player.skillManager.setCurrentLevel(Skill.STRENGTH, 99999, true)
                player.skillManager.setCurrentLevel(Skill.RANGED, 99999, true)
                player.skillManager.setCurrentLevel(Skill.DEFENCE, 99999, true)
                player.skillManager.setCurrentLevel(Skill.MAGIC, 99999, true)
                player.skillManager.setCurrentLevel(Skill.CONSTITUTION, 99999, true)
                player.skillManager.setCurrentLevel(Skill.PRAYER, 99999, true)
            }
            if (command[0] == "reset") {
                for (skill in Skill.values()) {
                    val level = if (skill == Skill.CONSTITUTION) 100 else if (skill == Skill.PRAYER) 10 else 1
                    player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                            SkillManager.getExperienceForLevel(if (skill == Skill.CONSTITUTION) 10 else 1))
                }
                player.packetSender.sendMessage("Your skill levels have now been reset.")
                player.updateFlag.flag(Flag.APPEARANCE)
            }
            if (command[0] == "givedonor" || command[0] == "givedonator") {
                val rights = command[1]
                val target = World.getPlayerByName(command[2])
                target.donorRights = Integer.parseInt(rights)
                target.packetSender.sendMessage("You have been given donator status. Relog to see it.")
                player.packetSender.sendMessage("You have given " + command[1] + " donator rights " + command[2] + ".")
            }
            if (command[0] == "giverights") {
                try {
                    val rights = command[1]
                    val target = World.getPlayerByName(command[2])
                    when (rights) {
                        "demote", "derank" -> {
                            target.rights = PlayerRights.PLAYER
                            target.packetSender.sendMessage("Your player rights has been changed to player.")
                            player.packetSender.sendMessage("You have demoted " + command[2] + " to player.")
                            target.packetSender.sendRights()
                        }
                        "ss", "serversupport", "support" -> {
                            target.rights = PlayerRights.SUPPORT
                            target.packetSender.sendMessage("Your player rights has been changed to support.")
                            player.packetSender.sendMessage("You have demoted " + command[2] + " to support.")
                            target.packetSender.sendRights()
                        }
                        "mod", "moderator" -> {
                            target.rights = PlayerRights.MODERATOR
                            target.packetSender.sendMessage("Your player rights has been changed to moderator.")
                            player.packetSender.sendMessage("You have demoted " + command[2] + " to moderator.")
                            target.packetSender.sendRights()
                        }
                        "globalmod", "gmod", "global_mod", "globalmoderator" -> {
                            target.rights = PlayerRights.GLOBAL_MOD
                            target.packetSender.sendMessage("Your player rights has been changed to global moderator.")
                            player.packetSender.sendMessage("You have demoted " + command[2] + " to global moderator.")
                            target.packetSender.sendRights()
                        }
                        "youtube", "youtuber" -> {
                            target.rights = PlayerRights.YOUTUBER
                            target.packetSender.sendMessage("Your player rights has been changed to youtuber.")
                            player.packetSender.sendMessage("You have promoted " + command[2] + " to youtuber.")
                            target.packetSender.sendRights()
                        }
                        else -> player.packetSender.sendMessage("Command not found - Use ss, mod, admin or dev.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            if (command[0] == "master") {
                for (skill in Skill.values()) {
                    val level = SkillManager.getMaxAchievingLevel(skill)
                    player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                            SkillManager.getExperienceForLevel(if (level == 120) 120 else 99))
                }
                player.packetSender.sendMessage("You are now a master of all skills.")
                player.updateFlag.flag(Flag.APPEARANCE)
            }
            if (command[0] == "setlevel" || command[0] == "setlvl" || command[0] == "lvl") {
                val skillId = Integer.parseInt(command[1])
                val level = Integer.parseInt(command[2])
                if (level > 15000) {
                    player.packetSender.sendMessage("You can only have a maxmium level of 15000.")
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
            if (wholeCommand.toLowerCase().startsWith("yell") && player.rights == PlayerRights.PLAYER) {
                if (GameSettings.YELL_STATUS) {
                    player.packetSender.sendMessage("Yell is currently turned off, please try again in 30 minutes!")
                    return
                }
                player.packetSender.sendMessage("Only members can yell. To become one, simply use ::store, buy a scroll").sendMessage("and then claim it.")
            }
            if (command[0] == "tele") {
                val x = Integer.valueOf(command[1])!!
                val y = Integer.valueOf(command[2])!!
                var z = player.position.z
                if (command.size > 3)
                    z = Integer.valueOf(command[3])!!
                val position = Position(x, y, z)
                player.moveTo(position)
                player.packetSender.sendMessage("Teleporting to " + position.toString())
            }
            if (command[0] == "bank") {
                player.getBank(player.currentBankTab).open()
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
            if (command[0] == "spec") {
                player.specialPercentage = 100
                CombatSpecial.updateBar(player)
            }
            if (command[0] == "giveitem") {
                val item = Integer.parseInt(command[1])
                val amount = Integer.parseInt(command[2])
                var rss = command[3]
                if (command.size > 4)
                    rss += " " + command[4]
                if (command.size > 5)
                    rss += " " + command[5]
                val target = World.getPlayerByName(rss)
                if (target == null) {
                    player.packetSender.sendMessage("Player must be online to give them stuff!")
                } else {
                    player.packetSender.sendMessage("Gave player gold.")
                    target.inventory.add(item, amount)
                }
            }
            if (command[0] == "update") {
                val time = Integer.parseInt(command[1])
                if (time > 0) {
                    GameServer.setUpdating(true)
                    for (players in World.getPlayers()) {
                        if (players == null)
                            continue
                        players.packetSender.sendSystemUpdate(time)
                        if (Dungeoneering.doingDungeoneering(players)) {
                            Dungeoneering.leave(players, false, true)
                            players.clickDelay.reset()
                            players.packetSender.sendMessage("You have been forced out of your dungeon due to an update, sorry!")
                        }
                    }
                    TaskManager.submit(object : Task(time) {
                        override fun execute() {
                            for (player in World.getPlayers()) {
                                if (player != null) {
                                    World.deregister(player)
                                }
                            }
                            WellOfGoodwill.save()
                            GrandExchangeOffers.save()
                            PlayerOwnedShops.saveShops()
                            try {
                                Scoreboard.save()
                            } catch (e: IOException) {

                            }

                            ClanChatManager.save()
                            GameServer.getLogger().info("Update task finished!")
                            stop()
                        }
                    })
                }
            }
            if (command[0] == "checkbank") {
                val plr = World.getPlayerByName(wholeCommand.substring(10))
                if (plr != null) {
                    player.packetSender.sendMessage("Loading bank..")
                    for (b in player.banks) {
                        b?.resetItems()
                    }
                    for (i in 0..plr.banks.size - 1) {
                        for (it in plr.getBank(i).items) {
                            if (it != null) {
                                player.getBank(i).add(it, false)
                            }
                        }
                    }
                    player.getBank(0).open()
                } else {
                    player.packetSender.sendMessage("Player is offline!")
                }
            }
            if (command[0] == "checkinv") {
                val player2 = World.getPlayerByName(wholeCommand.substring(9))
                if (player2 == null) {
                    player.packetSender.sendMessage("Cannot find that player online..")
                    return
                }
                player.inventory.setItems(player2.inventory.copiedItems).refreshItems()
            }
            if (command[0] == "checkequip") {
                val player2 = World.getPlayerByName(wholeCommand.substring(11))
                if (player2 == null) {
                    player.packetSender.sendMessage("Cannot find that player online..")
                    return
                }
                player.equipment.setItems(player2.equipment.copiedItems).refreshItems()
                WeaponInterfaces.assign(player, player.equipment.get(Equipment.WEAPON_SLOT))
                WeaponAnimations.assign(player, player.equipment.get(Equipment.WEAPON_SLOT))
                BonusManager.update(player)
                player.updateFlag.flag(Flag.APPEARANCE)
            }
        }
    }

    object Owner {

        @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
            if (wholeCommand.equals("mode", ignoreCase = true)) {
                player.forceChat("My game mode is: " + player.expRate.toString().toLowerCase() + ".")
            }
            if (command[0] == "zulrah") {
                player.zulrah.enterIsland()
            }
            if (command[0] == "setmaxcb") {
                val target = World.getPlayerByName(command[1])
                target.skillManager.setMaxLevel(Skill.ATTACK, 99)
                target.skillManager.setMaxLevel(Skill.DEFENCE, 99)
                target.skillManager.setMaxLevel(Skill.STRENGTH, 99)
                target.skillManager.setMaxLevel(Skill.CONSTITUTION, 990)
                target.skillManager.setMaxLevel(Skill.PRAYER, 990)
                target.skillManager.setMaxLevel(Skill.RANGED, 99)
                target.skillManager.setMaxLevel(Skill.MAGIC, 99)
                target.skillManager.setMaxLevel(Skill.SUMMONING, 99)
                target.skillManager.setMaxLevel(Skill.DUNGEONEERING, 80)
                target.skillManager.setCurrentLevel(Skill.ATTACK, 99)
                target.skillManager.setCurrentLevel(Skill.DEFENCE, 99)
                target.skillManager.setCurrentLevel(Skill.STRENGTH, 99)
                target.skillManager.setCurrentLevel(Skill.CONSTITUTION, 990)
                target.skillManager.setCurrentLevel(Skill.PRAYER, 990)
                target.skillManager.setCurrentLevel(Skill.RANGED, 99)
                target.skillManager.setCurrentLevel(Skill.MAGIC, 99)
                target.skillManager.setCurrentLevel(Skill.SUMMONING, 99)
                target.skillManager.setCurrentLevel(Skill.DUNGEONEERING, 80)
                target.skillManager.setExperience(Skill.ATTACK, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.DEFENCE, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.STRENGTH, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.CONSTITUTION, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.PRAYER, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.RANGED, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.MAGIC, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.SUMMONING, SkillManager.getExperienceForLevel(99))
                target.skillManager.setExperience(Skill.DUNGEONEERING, SkillManager.getExperienceForLevel(80))
                target.packetSender.sendMessage("You are now a master of all combat skills.")
                target.updateFlag.flag(Flag.APPEARANCE)
            }
            if (command[0] == "reload") {
                when (command[1]) {
                    "itemdef", "item_definition", "item_definitions" -> GameServer.getLoader().engine.submit { ItemDefinition.init() }
                }
            }
            if (command[0] == "findnpc") {
                val name = wholeCommand.substring(8).toLowerCase().replace("_".toRegex(), " ")
                player.packetSender.sendMessage("Finding npc id for npc - " + name)
                var found = false
                for (i in 0..NpcDefinition.getMaxAmountOfNpcs() - 1) {
                    if (NpcDefinition.forId(i) == null) {
                        continue
                    }
                    if (NpcDefinition.forId(i)!!.name.toLowerCase().contains(name)) {
                        player.packetSender.sendMessage(
                                "Found npc with name [" + NpcDefinition.forId(i)!!.name.toLowerCase() + "] - id: " + i)
                        found = true
                    }
                }
                if (!found) {
                    player.packetSender.sendMessage("No item with name [$name] has been found!")
                }
            }
            if (command[0] == "update") {
                val time = Integer.parseInt(command[1])
                if (time > 0) {
                    GameServer.setUpdating(true)
                    for (players in World.getPlayers()) {
                        if (players == null)
                            continue
                        players.packetSender.sendSystemUpdate(time)
                        if (Dungeoneering.doingDungeoneering(players)) {
                            Dungeoneering.leave(players, false, true)
                            players.clickDelay.reset()
                            players.packetSender.sendMessage("You have been forced out of your dungeon due to an update, sorry!")
                        }
                    }
                    TaskManager.submit(object : Task(time) {
                        override fun execute() {
                            for (player in World.getPlayers()) {
                                if (player != null) {
                                    World.deregister(player)
                                }
                            }
                            WellOfGoodwill.save()
                            GrandExchangeOffers.save()
                            PlayerOwnedShops.saveShops()
                            try {
                                Scoreboard.save()
                            } catch (e: IOException) {

                            }

                            ClanChatManager.save()
                            GameServer.getLogger().info("Update task finished!")
                            stop()
                        }
                    })
                }
            }
            if (command[0] == "sendstring") {
                val child = Integer.parseInt(command[1])
                val string = command[2]
                player.packetSender.sendString(child, string)
            }
            if (command[0] == "tasks") {
                player.packetSender.sendMessage("Found " + TaskManager.getTaskAmount() + " tasks.")
            }
            if (command[0] == "memory") {
                val used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
                player.packetSender.sendMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!")
            }
            if (command[0].equals("frame", ignoreCase = true)) {
                val frame = Integer.parseInt(command[1])
                val text = command[2]
                player.packetSender.sendString(frame, text)
            }
            if (command[0] == "npc") {
                val id = Integer.parseInt(command[1])
                val npc = NPC(id, Position(player.position.x, player.position.y,
                        player.position.z))
                World.register(npc)
                npc.constitution = 20000
                player.packetSender.sendEntityHint(npc)
            }
            if (command[0] == "fillinv") {
                for (i in 0..27) {
                    val it = Misc.getRandom(10000)
                    player.inventory.add(it, 1)
                }
            }
            if (command[0] == "playnpc") {
                player.npcTransformationId = Integer.parseInt(command[1])
                player.updateFlag.flag(Flag.APPEARANCE)
            } else if (command[0] == "playobject") {
                player.packetSender.sendObjectAnimation(GameObject(2283, player.position.copy()),
                        Animation(751))
                player.updateFlag.flag(Flag.APPEARANCE)
            }
            if (command[0] == "interface") {
                val id = Integer.parseInt(command[1])
                player.packetSender.sendInterface(id)
                player.packetSender.sendMessage("Opening interface $id...")
            }
            if (command[0] == "walkableinterface") {
                val id = Integer.parseInt(command[1])
                player.packetSender.sendWalkableInterface(id)
            }
            if (command[0] == "anim") {
                val id = Integer.parseInt(command[1])
                player.performAnimation(Animation(id))
                player.packetSender.sendMessage("Sending animation: " + id)
            }
            if (command[0] == "gfx") {
                val id = Integer.parseInt(command[1])
                player.performGraphic(Graphic(id))
                player.packetSender.sendMessage("Sending graphic: " + id)
            }
            if (command[0] == "object") {
                val id = Integer.parseInt(command[1])
                player.packetSender.sendObject(GameObject(id, player.position, 10, 3))
                player.packetSender.sendMessage("Sending object: " + id)
            }
        }
    }
}
