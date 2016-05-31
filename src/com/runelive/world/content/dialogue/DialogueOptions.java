package com.runelive.world.content.dialogue;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import com.runelive.model.definitions.ItemDefinition;
import com.runelive.world.content.Scoreboard;
import com.runelive.GameSettings;
import com.runelive.world.content.Scoreboard;
import com.runelive.engine.task.Task;
import com.runelive.world.content.combat.CombatFactory;
import com.runelive.engine.task.TaskManager;
import com.runelive.engine.task.impl.BonusExperienceTask;
import com.runelive.model.Animation;
import com.runelive.model.Flag;
import com.runelive.model.GameMode;
import com.runelive.model.GameObject;
import com.runelive.model.Hit;
import com.runelive.model.Item;
import com.runelive.model.Locations;
import com.runelive.model.Locations.Location;
import com.runelive.model.PlayerRelations.PrivateChatStatus;
import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.model.Skill;
import com.runelive.model.container.impl.Shop.ShopManager;
import com.runelive.model.input.impl.BuyShards;
import com.runelive.model.input.impl.ChangePassword;
import com.runelive.model.input.impl.DonateToWell;
import com.runelive.model.input.impl.EnterYellTag;
import com.runelive.model.input.impl.GambleAdvertiser;
import com.runelive.model.input.impl.PosSearchShop;
import com.runelive.model.input.impl.SellShards;
import com.runelive.model.input.impl.SetEmail;
import com.runelive.model.input.impl.ToxicStaffZulrahScales;
import com.runelive.model.movement.MovementQueue;
import com.runelive.net.security.ConnectionHandler;
import com.runelive.util.Misc;
import com.runelive.util.NameUtils;
import com.runelive.world.World;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.Artifacts;
import com.runelive.world.content.BankPin;
import com.runelive.world.content.BossSystem;
import com.runelive.world.content.BossSystem.Bosses;
import com.runelive.world.content.CustomObjects;
import com.runelive.world.content.Effigies;
import com.runelive.world.content.Gambling.FlowersData;
import com.runelive.world.content.Lottery;
import com.runelive.world.content.MemberScrolls;
import com.runelive.world.content.PkSets;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.content.WellOfGoodwill;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.ShootingStar;
import com.runelive.world.content.dialogue.impl.AgilityTicketExchange;
import com.runelive.world.content.dialogue.impl.Mandrith;
import com.runelive.world.content.minigames.impl.Graveyard;
import com.runelive.world.content.minigames.impl.Nomad;
import com.runelive.world.content.minigames.impl.RecipeForDisaster;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.content.skill.impl.agility.Agility;
import com.runelive.world.content.skill.impl.construction.Construction;
import com.runelive.world.content.skill.impl.construction.ConstructionConstants;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.content.skill.impl.dungeoneering.DungeoneeringFloor;
import com.runelive.world.content.skill.impl.mining.Mining;
import com.runelive.world.content.skill.impl.slayer.Slayer;
import com.runelive.world.content.skill.impl.slayer.SlayerDialogues;
import com.runelive.world.content.skill.impl.slayer.SlayerMaster;
import com.runelive.world.content.skill.impl.summoning.CharmingImp;
import com.runelive.world.content.skill.impl.summoning.SummoningTab;
import com.runelive.world.content.transportation.JewelryTeleporting;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.content.transportation.TeleportType;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.npc.NpcAggression;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.content.skill.impl.construction.ConstructionData.HouseLocation;
import com.runelive.world.content.skill.impl.construction.ConstructionData.HouseTheme;

public class DialogueOptions {

	/**
	 * 5k Vengeance
	 */

	public static int DEATH_RUNE = 560;
	public static int DEATH_RUNE_PRICE = ItemDefinition.forId(DEATH_RUNE).getValue() * 5000;
	public static int EARTH_RUNE = 557;
	public static int EARTH_RUNE_PRICE = ItemDefinition.forId(EARTH_RUNE).getValue() * 5000;
	public static int ASTRAL_RUNE = 9075;
	public static int ASTRAL_RUNE_PRICE = ItemDefinition.forId(ASTRAL_RUNE).getValue() * 5000;

	/**
	 * 5k Barrage
     */

	public static int WATER_RUNE = 555;
	public static int WATER_RUNE_PRICE = ItemDefinition.forId(WATER_RUNE).getValue() * 5000;
	public static int BLOOD_RUNE = 565;
	public static int BLOOD_RUNE_PRICE = ItemDefinition.forId(BLOOD_RUNE).getValue() * 5000;

	/**
	 * 10k Elemental Runes
     */

	public static int AIR_RUNE = 556;
	public static int AIR_RUNE_PRICE = ItemDefinition.forId(AIR_RUNE).getValue() * 10000;
	public static int FIRE_RUNE = 554;
	public static int FIRE_RUNE_PRICE = ItemDefinition.forId(FIRE_RUNE).getValue() * 10000;
	public static int EARTH_RUNE_PRICE2 = ItemDefinition.forId(EARTH_RUNE).getValue() * 10000;
	public static int WATER_RUNE_PRICE2 = ItemDefinition.forId(WATER_RUNE).getValue() * 10000;


  // Last id used = 78

	public static void handle(Player player, int id) {
		int credits = 0;
		if(player.getRights() == PlayerRights.OWNER) {
			player.getPacketSender().sendMessage("Dialogue button id: "+id+", action id: "+player.getDialogueActionId()).sendConsoleMessage("Dialogue button id: "+id+", action id: "+player.getDialogueActionId());
		}
		if(Effigies.handleEffigyAction(player, id)) {
			return;
		}
		if(id == FIRST_OPTION_OF_FIVE) {
			switch(player.getDialogueActionId()) {
			case 178:
						TeleportHandler.teleportPlayer(player, new Position(3087, 3502, 0), player.getSpellbook().getTeleportType());
				break;
			case 179:
				TeleportHandler.teleportPlayer(player, new Position(3092, 3248, 0), player.getSpellbook().getTeleportType());
				break;
			case 180:
				TeleportHandler.teleportPlayer(player, new Position(3276, 3166, 0), player.getSpellbook().getTeleportType());
				break;
			case 137:
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to use this item.");
					return;
				}
				if(player.getLocation() != Location.GAMBLE) {
					player.getPacketSender().sendMessage("").sendMessage("This seed can only be planted in the gambling area").sendMessage("To get there, talk to the gambler.");
					return;
				}
				if(!player.getClickDelay().elapsed(3000))
					return;
				for(NPC npc : player.getLocalNpcs()) {
					if(npc != null && npc.getPosition().equals(player.getPosition())) {
						player.getPacketSender().sendMessage("You cannot plant a seed right here.");
						return;
					}
				}
				if(CustomObjects.objectExists(player.getPosition().copy())) {
					player.getPacketSender().sendMessage("You cannot plant a seed right here.");
					return;
				}
				FlowersData flowers = FlowersData.BLUE_FLOWERS;
				final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
				player.getMovementQueue().reset();
				player.getInventory().delete(299, 1);
				player.performAnimation(new Animation(827));
				player.getPacketSender().sendMessage("You plant the seed..");
				player.getMovementQueue().reset();
				player.setDialogueActionId(42);
				player.setInteractingObject(flower);
				DialogueManager.start(player, 78);
				MovementQueue.stepAway(player);
				CustomObjects.globalObjectRemovalTask(flower, 90);
				player.setPositionToFace(flower.getPosition());
				player.getClickDelay().reset();
			break;
			case 0:
				TeleportHandler.teleportPlayer(player, new Position(2679, 3720), player.getSpellbook().getTeleportType());
				break;
			case 1:
				TeleportHandler.teleportPlayer(player, new Position(3420, 3510), player.getSpellbook().getTeleportType());
				break;
			case 2:
				TeleportHandler.teleportPlayer(player, new Position(3235, 3295), player.getSpellbook().getTeleportType());
				break;
			case 9:
				DialogueManager.start(player, 16);
				break;
			case 10:
				Artifacts.sellArtifacts(player);
				break;
			case 11:
				Scoreboard.open(player, 2);
				break;
			case 12:
				TeleportHandler.teleportPlayer(player, new Position(3087, 3517), player.getSpellbook().getTeleportType());
				break;
			case 13:
				player.setDialogueActionId(78);
				DialogueManager.start(player, 124);
				break;
			case 14:
				TeleportHandler.teleportPlayer(player, new Position(3097 + Misc.getRandom(1), 9869 + Misc.getRandom(1)), player.getSpellbook().getTeleportType());
				break;
			case 15:
				player.getPacketSender().sendInterfaceRemoval();
				int total = player.getSkillManager().getMaxLevel(Skill.ATTACK) + player.getSkillManager().getMaxLevel(Skill.STRENGTH);
				boolean has99 = player.getSkillManager().getMaxLevel(Skill.ATTACK) >= 99 || player.getSkillManager().getMaxLevel(Skill.STRENGTH) >= 99;
				if(total < 130 && !has99) {
					player.getPacketSender().sendMessage("");
					player.getPacketSender().sendMessage("You do not meet the requirements of a Warrior.");
					player.getPacketSender().sendMessage("You need to have a total Attack and Strength level of at least 140.");
					player.getPacketSender().sendMessage("Having level 99 in either is fine aswell.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2855, 3543), player.getSpellbook().getTeleportType());
				break;
			case 17:
				player.setInputHandling(new SetEmail());
				player.getPacketSender().sendEnterInputPrompt("Enter your email address:");
				break;
			case 29:
				SlayerMaster.changeSlayerMaster(player, SlayerMaster.VANNAKA);
				break;
			case 36:
				TeleportHandler.teleportPlayer(player, new Position(2871, 5318, 2), player.getSpellbook().getTeleportType());
				break;
			case 38:
				TeleportHandler.teleportPlayer(player, new Position(3003, 3850, 0), player.getSpellbook().getTeleportType());
				break;
			case 40:
				TeleportHandler.teleportPlayer(player, new Position(3476, 9502), player.getSpellbook().getTeleportType());
				break;
			case 48:
				JewelryTeleporting.teleport(player, new Position(3088, 3506));
				break;
			case 59:
				if(player.getClickDelay().elapsed(1500)) {
					PkSets.buySet(player, PkSets.PURE_SET);
				}
				break;
			case 60:
				player.setDialogueActionId(61);
				DialogueManager.start(player, 102);
				break;
			case 67:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
					if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername().equals(player.getUsername())) {
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setDungeoneeringFloor(DungeoneeringFloor.FIRST_FLOOR);
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().sendMessage("The party leader has changed floor.");
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
					}
				}
				break;
			case 68:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
					if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername().equals(player.getUsername())) {
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(1);
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().sendMessage("The party leader has changed complexity.");
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
					}
				}
				break;
			case 80:
				TeleportHandler.teleportPlayer(player, new Position(2679, 3720), player.getSpellbook().getTeleportType());
				break;
			case 134://first boss
				BossSystem.startInstance(player, BossSystem.Bosses.KBD.getBossID(), player.isBossSolo());
				player.setLastBoss(Bosses.KBD.getBossID());
				player.getPacketSender().sendInterfaceRemoval();
				break;
				case 197:
					if(ConstructionConstants.MOVING_ENABLED)
					{
						Construction.moveHouse(player, HouseLocation.TAVERLY);
					}
					break;
				case 198:
					if(ConstructionConstants.THEMES_ENABLED)
					{
						Construction.switchTheme(player, HouseTheme.BASIC_WOOD);
					}
					break;
			}
		} else if(id == SECOND_OPTION_OF_FIVE) {
			switch(player.getDialogueActionId()) {
				case 197:
					if(ConstructionConstants.MOVING_ENABLED)
					{
						Construction.moveHouse(player, HouseLocation.RIMMINGTON);
					}
					break;
				case 198:
					if(ConstructionConstants.THEMES_ENABLED)
					{
						Construction.switchTheme(player, HouseTheme.BASIC_STONE);
					}
					break;
			case 178:
				TeleportHandler.teleportPlayer(player, new Position(2965, 3379, 0), player.getSpellbook().getTeleportType());
				break;
			case 179:
				TeleportHandler.teleportPlayer(player, new Position(2662, 3305, 0), player.getSpellbook().getTeleportType());
				break;
			case 180:
				TeleportHandler.teleportPlayer(player, new Position(2606, 3093, 0), player.getSpellbook().getTeleportType());
				break;
			case 137:
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to use this item.");
					return;
				}
				if(player.getLocation() != Location.GAMBLE) {
					player.getPacketSender().sendMessage("").sendMessage("This seed can only be planted in the gambling area").sendMessage("To get there, talk to the gambler.");
					return;
				}
				if(!player.getClickDelay().elapsed(3000))
					return;
				for(NPC npc : player.getLocalNpcs()) {
					if(npc != null && npc.getPosition().equals(player.getPosition())) {
						player.getPacketSender().sendMessage("You cannot plant a seed right here.");
						return;
					}
				}
				if(CustomObjects.objectExists(player.getPosition().copy())) {
					player.getPacketSender().sendMessage("You cannot plant a seed right here.");
					return;
				}
				FlowersData flowers = FlowersData.ORANGE_FLOWERS;
				final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
				player.getMovementQueue().reset();
				player.getInventory().delete(299, 1);
				player.performAnimation(new Animation(827));
				player.getPacketSender().sendMessage("You plant the seed..");
				player.getMovementQueue().reset();
				player.setDialogueActionId(42);
				player.setInteractingObject(flower);
				DialogueManager.start(player, 78);
				MovementQueue.stepAway(player);
				CustomObjects.globalObjectRemovalTask(flower, 90);
				player.setPositionToFace(flower.getPosition());
				player.getClickDelay().reset();
			break;
			case 0:
				TeleportHandler.teleportPlayer(player, new Position(3557 + (Misc.getRandom(2)), 9946 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
				break;
			case 1:
				TeleportHandler.teleportPlayer(player, new Position(2933, 9849), player.getSpellbook().getTeleportType());
				break;
			case 2:
				TeleportHandler.teleportPlayer(player, new Position(2802, 9148), player.getSpellbook().getTeleportType());
				break;
			case 9:
				Lottery.enterLottery(player);
				break;
			case 10:
				player.setDialogueActionId(59);
				DialogueManager.start(player, 100);
				break;
			case 11:
				Scoreboard.open(player, 1); //Top Pkers
				break;
			case 12:
				TeleportHandler.teleportPlayer(player, new Position(2980 + Misc.getRandom(3), 3596 + Misc.getRandom(3)), player.getSpellbook().getTeleportType());
				break;
			case 13:
				player.getPacketSender().sendInterfaceRemoval();
				for(AchievementData d : AchievementData.values()) {
					if(!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
						player.getPacketSender().sendMessage("You must have completed all achievements in order to buy this cape.");
						return;
					}
				}
				boolean usePouch = player.getMoneyInPouch() >= 100000000;
				if(!usePouch && player.getInventory().getAmount(995) < 100000000) {
					player.getPacketSender().sendMessage("You do not have enough coins.");
					return;
				}
				if(usePouch) {
					player.setMoneyInPouch(player.getMoneyInPouch() - 100000000);
					player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
				} else
					player.getInventory().delete(995, 100000000);
				player.getInventory().add(14022, 1);
				player.getPacketSender().sendMessage("You've purchased an Completionist cape.");
				break;
			case 14:
				TeleportHandler.teleportPlayer(player, new Position(3184 + Misc.getRandom(1), 5471 + Misc.getRandom(1)), player.getSpellbook().getTeleportType());
				break;
			case 15:
				TeleportHandler.teleportPlayer(player, new Position(2663 + Misc.getRandom(1), 2651 + Misc.getRandom(1)), player.getSpellbook().getTeleportType());
				break;
			case 17:
				if(player.getBankPinAttributes().hasBankPin()) {
					DialogueManager.start(player, 12);
					player.setDialogueActionId(8);
				} else {
					BankPin.init(player, false);
				}
				break;
			case 29:
				SlayerMaster.changeSlayerMaster(player, SlayerMaster.DURADEL);
				break;
			case 36:
				TeleportHandler.teleportPlayer(player, new Position(1908, 4367), player.getSpellbook().getTeleportType());
				break;
			case 38:
				DialogueManager.start(player, 70);
				player.setDialogueActionId(39);
				break;
			case 40:
				TeleportHandler.teleportPlayer(player, new Position(2839, 9557), player.getSpellbook().getTeleportType());
				break;
			case 48:
				JewelryTeleporting.teleport(player, new Position(3213, 3423));
				break;
			case 59:
				if(player.getClickDelay().elapsed(1500)) {
					PkSets.buySet(player, PkSets.MELEE_MAIN_SET);
				}
				break;
			case 60:
				player.setDialogueActionId(62);
				DialogueManager.start(player, 102);
				break;
			case 68:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
					if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername().equals(player.getUsername())) {
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(2);
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().sendMessage("The party leader has changed complexity.");
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
					}
				}
				break;
			case 80:
				TeleportHandler.teleportPlayer(player, new Position(2679, 3720), player.getSpellbook().getTeleportType());
				break;
			case 134:
				BossSystem.startInstance(player, BossSystem.Bosses.TD.getBossID(), player.isBossSolo());
				player.setLastBoss(Bosses.TD.getBossID());
				player.getPacketSender().sendInterfaceRemoval();
				break;
			}
		} else if(id == THIRD_OPTION_OF_FIVE) {
			switch(player.getDialogueActionId()) {
				case 197:
					if(ConstructionConstants.MOVING_ENABLED)
					{
						Construction.moveHouse(player, HouseLocation.RELLEKKA);
					}
					break;
				case 198:
					if(ConstructionConstants.THEMES_ENABLED)
					{
						Construction.switchTheme(player, HouseTheme.WHITEWASHED_STONE);
					}
					break;
			case 178:
				TeleportHandler.teleportPlayer(player, new Position(3222, 3218, 0), player.getSpellbook().getTeleportType());
				break;
			case 179:
				TeleportHandler.teleportPlayer(player, new Position(3496, 3486, 0), player.getSpellbook().getTeleportType());
				break;
			case 180:
				TeleportHandler.teleportPlayer(player, new Position(2852, 2962, 0), player.getSpellbook().getTeleportType());
				break;
			case 137:
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to use this item.");
					return;
				}
				if(player.getLocation() != Location.GAMBLE) {
					player.getPacketSender().sendMessage("").sendMessage("This seed can only be planted in the gambling area").sendMessage("To get there, talk to the gambler.");
					return;
				}
				if(!player.getClickDelay().elapsed(3000))
					return;
				for(NPC npc : player.getLocalNpcs()) {
					if(npc != null && npc.getPosition().equals(player.getPosition())) {
						player.getPacketSender().sendMessage("You cannot plant a seed right here.");
						return;
					}
				}
				if(CustomObjects.objectExists(player.getPosition().copy())) {
					player.getPacketSender().sendMessage("You cannot plant a seed right here.");
					return;
				}
				FlowersData flowers = FlowersData.YELLOW_FLOWERS;
				final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
				player.getMovementQueue().reset();
				player.getInventory().delete(299, 1);
				player.performAnimation(new Animation(827));
				player.getPacketSender().sendMessage("You plant the seed..");
				player.getMovementQueue().reset();
				player.setDialogueActionId(42);
				player.setInteractingObject(flower);
				DialogueManager.start(player, 78);
				MovementQueue.stepAway(player);
				CustomObjects.globalObjectRemovalTask(flower, 90);
				player.setPositionToFace(flower.getPosition());
				player.getClickDelay().reset();
			break;
			case 0:
				TeleportHandler.teleportPlayer(player, new Position(3204 + (Misc.getRandom(2)), 3263 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
				break;
			case 1:
				TeleportHandler.teleportPlayer(player, new Position(2480, 5175), player.getSpellbook().getTeleportType());
				break;
			case 2:
				TeleportHandler.teleportPlayer(player, new Position(2793, 2773), player.getSpellbook().getTeleportType());
				break;
			case 9:
				DialogueManager.start(player, Lottery.Dialogues.getCurrentPot(player));
				break;
			case 10:
				DialogueManager.start(player, Mandrith.getDialogue(player));
				break;
			case 11:
				Scoreboard.open(player, 3);
				break;
			case 12:
				TeleportHandler.teleportPlayer(player, new Position(3234 + Misc.getRandom(2), 3637 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
				break;
			case 13:
				if(Misc.getHoursPlayedNumeric(player.getTotalPlayTime()) >= 2000) {
					player.getPacketSender().sendInterfaceRemoval();
					boolean usePouch = player.getMoneyInPouch() >= 75000000;
					if (!usePouch && player.getInventory().getAmount(995) < 75000000) {
						player.getPacketSender().sendMessage("You do not have enough coins.");
						return;
					}
					if (usePouch) {
						player.setMoneyInPouch(player.getMoneyInPouch() - 75000000);
						player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
					} else
						player.getInventory().delete(995, 75000000);
					player.getInventory().add(14021, 1);
					player.getPacketSender().sendMessage("You've purchased a Veteran cape.");
					DialogueManager.start(player, 122);
					player.setDialogueActionId(76);
				} else {
					player.getPacketSender().sendMessage("You've played "+Misc.getHoursPlayedNumeric(player.getTotalPlayTime())+"/2000 Hours.");
					player.getPacketSender().sendInterfaceRemoval();
				}
				break;
			case 14:
				TeleportHandler.teleportPlayer(player, new Position(2713, 9564), player.getSpellbook().getTeleportType());
				break;
			case 15:
				TeleportHandler.teleportPlayer(player, new Position(3368 + Misc.getRandom(5), 3267+ Misc.getRandom(3)), player.getSpellbook().getTeleportType());
				break;
			case 17:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getBankPinAttributes().hasBankPin()) {
					player.getPacketSender().sendMessage("Please visit the nearest bank and enter your pin before doing this.");
					return;
				}
				if(player.getSummoning().getFamiliar() != null) {
					player.getPacketSender().sendMessage("Please dismiss your familiar first.");
					return;
				}
				if(player.getGameMode() == GameMode.NORMAL) {
					DialogueManager.start(player, 83);
				} else {
					player.setDialogueActionId(46);
					DialogueManager.start(player, 84);
				}
				break;
			case 29:
				SlayerMaster.changeSlayerMaster(player, SlayerMaster.KURADEL);
				break;
			case 36:
				player.setDialogueActionId(37);
				DialogueManager.start(player, 70);
				break;
			case 38:
				TeleportHandler.teleportPlayer(player, new Position(2547, 9448), player.getSpellbook().getTeleportType());
				break;
			case 40:
				TeleportHandler.teleportPlayer(player, new Position(2891, 4767), player.getSpellbook().getTeleportType());
				break;
			case 48:
				JewelryTeleporting.teleport(player, new Position(3368, 3267));
				break;
			case 59:
				if(player.getClickDelay().elapsed(1500)) {
					PkSets.buySet(player, PkSets.RANGE_MAIN_SET);
				}
				break;
			case 60:
				player.setDialogueActionId(63);
				DialogueManager.start(player, 102);
				break;
			case 68:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
					if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername().equals(player.getUsername())) {
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(3);
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().sendMessage("The party leader has changed complexity.");
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
					}
				}
				break;
			case 134:
				BossSystem.startInstance(player, BossSystem.Bosses.CORP.getBossID(), player.isBossSolo());
				player.setLastBoss(Bosses.CORP.getBossID());
				player.getPacketSender().sendInterfaceRemoval();
				break;
			}
		} else if(id == FOURTH_OPTION_OF_FIVE) {
			switch(player.getDialogueActionId()) {
				case 197:
					if(ConstructionConstants.MOVING_ENABLED)
					{
						Construction.moveHouse(player, HouseLocation.YANILLE);
					}
					break;
				case 198:
					if(ConstructionConstants.THEMES_ENABLED)
					{
						Construction.switchTheme(player, HouseTheme.FREMENNIK_STYLE_WOOD);
					}
					break;
			case 178:
				TeleportHandler.teleportPlayer(player, new Position(2757, 3477, 0), player.getSpellbook().getTeleportType());
				break;
			case 179:
				TeleportHandler.teleportPlayer(player, new Position(2809, 3435, 0), player.getSpellbook().getTeleportType());
				break;
			case 180:
				TeleportHandler.teleportPlayer(player, new Position(2336, 3803, 0), player.getSpellbook().getTeleportType());
				break;
			case 137:
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to use this item.");
					return;
				}
				if(player.getLocation() != Location.GAMBLE) {
					player.getPacketSender().sendMessage("").sendMessage("This seed can only be planted in the gambling area").sendMessage("To get there, talk to the gambler.");
					return;
				}
				if(!player.getClickDelay().elapsed(3000))
					return;
				for(NPC npc : player.getLocalNpcs()) {
					if(npc != null && npc.getPosition().equals(player.getPosition())) {
						player.getPacketSender().sendMessage("You cannot plant a seed right here.");
						return;
					}
				}
				if(CustomObjects.objectExists(player.getPosition().copy())) {
					player.getPacketSender().sendMessage("You cannot plant a seed right here.");
					return;
				}
				FlowersData flowers = FlowersData.RED_FLOWERS;
				final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
				player.getMovementQueue().reset();
				player.getInventory().delete(299, 1);
				player.performAnimation(new Animation(827));
				player.getPacketSender().sendMessage("You plant the seed..");
				player.getMovementQueue().reset();
				player.setDialogueActionId(42);
				player.setInteractingObject(flower);
				DialogueManager.start(player, 78);
				MovementQueue.stepAway(player);
				CustomObjects.globalObjectRemovalTask(flower, 90);
				player.setPositionToFace(flower.getPosition());
				player.getClickDelay().reset();
			break;
			case 0:
				TeleportHandler.teleportPlayer(player, new Position(3173 - (Misc.getRandom(2)), 2981 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
				break;
			case 1:
				TeleportHandler.teleportPlayer(player, new Position(3279, 2964), player.getSpellbook().getTeleportType());
				break;
			case 2:
				TeleportHandler.teleportPlayer(player, new Position(3085, 9672), player.getSpellbook().getTeleportType());
				break;
			case 9:
				DialogueManager.start(player, Lottery.Dialogues.getLastWinner(player));
				break;
			case 10:
				ShopManager.getShops().get(26).open(player);
				break;
			case 11:
				if(GameSettings.TOURNAMENT_MODE) {
					Scoreboard.open(player, 4);
				} else {
					player.getPacketSender().sendMessage("There currently is no tournament mode open!");
				}	
				break;
			case 12:
				TeleportHandler.teleportPlayer(player, new Position(3372 + Misc.getRandom(2), 3686 + Misc.getRandom(2), 0), player.getSpellbook().getTeleportType());
				break;
			case 13:
				player.getPacketSender().sendInterfaceRemoval();
				if(!player.getSkillManager().maxStats()) {
					player.getPacketSender().sendMessage("You must have 99's in every skill in order to purchase the max cape!");
					return;
				}
				boolean usePouch = player.getMoneyInPouch() >= 50000000;
				if(!usePouch && player.getInventory().getAmount(995) < 50000000) {
					player.getPacketSender().sendMessage("You do not have enough coins.");
					return;
				}
				if(usePouch) {
					player.setMoneyInPouch(player.getMoneyInPouch() - 50000000);
					player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
				} else
					player.getInventory().delete(995, 50000000);
				player.getInventory().add(14019, 1);
				player.getPacketSender().sendMessage("You've purchased a Max cape.");
				break;
			case 14:
				TeleportHandler.teleportPlayer(player, new Position(2884, 9797), player.getSpellbook().getTeleportType());
				break;
			case 15:
				TeleportHandler.teleportPlayer(player, new Position(3565, 3313), player.getSpellbook().getTeleportType());
				break;
			case 17:
				player.setInputHandling(new ChangePassword());
				player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
				break;
			case 29:
				SlayerMaster.changeSlayerMaster(player, SlayerMaster.SUMONA);
				break;
			case 36:
				if (player.getLocation() == Location.BOSS_SYSTEM) {
					Locations.Location.BOSS_SYSTEM.leave(player);
					player.getPacketSender().sendInterfaceRemoval();
				} else {
					TeleportHandler.teleportPlayer(player, new Position(2861, 9640), player.getSpellbook().getTeleportType());
				}
				break;
			case 38:
				TeleportHandler.teleportPlayer(player, new Position(1891, 3177), player.getSpellbook().getTeleportType());
				break;
			case 40:
				TeleportHandler.teleportPlayer(player, new Position(3050, 9573), player.getSpellbook().getTeleportType());
				break;
			case 48:
				JewelryTeleporting.teleport(player, new Position(2447, 5169));
				break;
			case 59:
				if(player.getClickDelay().elapsed(1500)) {
					PkSets.buySet(player, PkSets.MAGIC_MAIN_SET);
				}
				break;
			case 60:
				player.setDialogueActionId(64);
				DialogueManager.start(player, 102);
				break;
			case 68:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
					if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername().equals(player.getUsername())) {
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(4);
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().sendMessage("The party leader has changed complexity.");
						player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
					}
				}
				break;
			case 134:
				player.getPacketSender().sendMessage("This is currently the only page you can spawn monsters from.");
				player.getPacketSender().sendInterfaceRemoval();
				break;
			}
		} else if(id == FIFTH_OPTION_OF_FIVE) {
			switch(player.getDialogueActionId()) {
				case 197:
				case 198:
					player.getPacketSender().sendInterfaceRemoval();
					break;
			case 178:
				player.setDialogueActionId(179);
				DialogueManager.start(player, 179);
				break;
			case 179:
				player.setDialogueActionId(180);
				DialogueManager.start(player, 180);
				break;
			case 180:
				TeleportHandler.teleportPlayer(player, new Position(2929, 3559, 0), player.getSpellbook().getTeleportType());
				break;
			case 137:
				player.setDialogueActionId(138);
				DialogueManager.start(player, 138);
				break;
			case 0:
				player.setDialogueActionId(1);
				DialogueManager.next(player);
				break;
			case 1:
				player.setDialogueActionId(2);
				DialogueManager.next(player);
				break;
			case 2:
				player.setDialogueActionId(0);
				DialogueManager.start(player, 0);
				break;
			case 10:
				DialogueManager.start(player, 217);
				player.setDialogueActionId(220);
				break;
			case 9:
			case 11:
			case 13:
			case 17:
			case 29:
			case 48:
			case 60:
			case 67:
			case 68:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 12:
				TeleportHandler.teleportPlayer(player, new Position(2539, 4715, 0), player.getSpellbook().getTeleportType());
				break;
			case 14:
				DialogueManager.start(player, 23);
				player.setDialogueActionId(14);
				break;
			case 15:
				DialogueManager.start(player, 32);
				player.setDialogueActionId(18);
				break;
			case 36:
				DialogueManager.start(player, 66);
				player.setDialogueActionId(38);
				break;
			case 38:
				DialogueManager.start(player, 68);
				player.setDialogueActionId(40);
				break;
			case 40:
				DialogueManager.start(player, 69);
				player.setDialogueActionId(41);
				break;
			case 59:
				if(player.getClickDelay().elapsed(1500)) {
					PkSets.buySet(player, PkSets.HYBRIDING_MAIN_SET);
				}
				break;
			case 134:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			}
		} else if(id == FIRST_OPTION_OF_FOUR) {
			switch(player.getDialogueActionId()) {
			case 227:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(2976, 3921, 0), player.getSpellbook().getTeleportType());
			break;
			case 214:
				if(!GameSettings.POS_ENABLED) {
					player.getPacketSender().sendMessage("Player owned shops have been disabled.");
					return;
				}
				player.getPacketSender().sendString(41900, "");
				PlayerOwnedShops.openShop(player.getUsername(), player);
				break;
			case 136:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(2845, 5335, 2), player.getSpellbook().getTeleportType());
				break;
			case 5:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 8:
				ShopManager.getShops().get(27).open(player);
				break;
			case 9:
				TeleportHandler.teleportPlayer(player, new Position(3184, 3434), player.getSpellbook().getTeleportType());
				break;
			case 14:
				TeleportHandler.teleportPlayer(player, new Position(2871, 5318, 2), player.getSpellbook().getTeleportType());
				break;
			case 18:
				TeleportHandler.teleportPlayer(player, new Position(2439 + Misc.getRandom(2), 5171 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
				break;
			case 26:
				TeleportHandler.teleportPlayer(player, new Position(2480, 3435), player.getSpellbook().getTeleportType());
				break;
			case 27:
				ClanChatManager.createClan(player);
				break;
			case 28:
				player.setDialogueActionId(29);
				DialogueManager.start(player, 62);
				break;
			case 30:
				player.getSlayer().assignTask();
				break;
			case 31:
				DialogueManager.start(player, SlayerDialogues.findAssignment(player));
				break;
			case 41:
				DialogueManager.start(player, 76);
				break;
			case 45:
				GameMode.set(player, GameMode.NORMAL, false);
				break;
			case 50:
				TeleportHandler.teleportPlayer(player, new Position(2399, 5177), player.getSpellbook().getTeleportType());
				break;
			}
		} else if(id == SECOND_OPTION_OF_FOUR) {
			switch(player.getDialogueActionId()) {
			case 227:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(3293, 3926, 0), player.getSpellbook().getTeleportType());
			break;
			case 136:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(2891, 5356, 2), player.getSpellbook().getTeleportType());
				break;
			case 214:
				if(!GameSettings.POS_ENABLED) {
					player.getPacketSender().sendMessage("Player owned shops have been disabled.");
					return;
				}
				if(player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
					player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
					return;
				}
				PlayerOwnedShops.openItemSearch(player, true);
				break;
			case 5:
				DialogueManager.start(player, MemberScrolls.getTotalFunds(player));
				break;
			case 8:
				//LoyaltyProgramme.open(player);
				break;
			case 9:
				DialogueManager.start(player, 14);
				break;
			case 14:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 50) {
					player.getPacketSender().sendMessage("You need a Slayer level of at least 50 to visit this dungeon.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2731, 5095), player.getSpellbook().getTeleportType());
				break;
			case 18:
				TeleportHandler.teleportPlayer(player, new Position(2399, 5177), player.getSpellbook().getTeleportType());
				break;
			case 26:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getSkillManager().getMaxLevel(Skill.AGILITY) < 35) {
					player.getPacketSender().sendMessage("You need an Agility level of at least level 35 to use this course.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2552, 3556), player.getSpellbook().getTeleportType());
				break;
			case 27:
				ClanChatManager.clanChatSetupInterface(player, true);
				break;
			case 28:
				if(player.getSlayer().getSlayerMaster().getPosition() != null) {
					TeleportHandler.teleportPlayer(player, new Position(player.getSlayer().getSlayerMaster().getPosition().getX(), player.getSlayer().getSlayerMaster().getPosition().getY(), player.getSlayer().getSlayerMaster().getPosition().getZ()), player.getSpellbook().getTeleportType());
					if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) <= 50)
						player.getPacketSender().sendMessage("").sendMessage("You can train Slayer with a friend by using a Slayer gem on them.").sendMessage("Slayer gems can be bought from all Slayer masters.");;
				}
				break;
			case 31:
				DialogueManager.start(player, SlayerDialogues.resetTaskDialogue(player));
				break;
			case 41:
				WellOfGoodwill.lookDownWell(player);
				break;
			case 45:
				GameMode.set(player, GameMode.IRONMAN, false);
				break;
			case 50:
				TeleportHandler.teleportPlayer(player, new Position(2399, 5177), player.getSpellbook().getTeleportType());
				break;
			}
		} else if(id == THIRD_OPTION_OF_FOUR) {
			switch(player.getDialogueActionId()) {
			case 227:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(3300, 3888, 0), player.getSpellbook().getTeleportType());
			break;
			case 136:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(2917, 5272, 0), player.getSpellbook().getTeleportType());
				break;
			case 214:
				if(!GameSettings.POS_ENABLED) {
					player.getPacketSender().sendMessage("Player owned shops have been disabled.");
					return;
				}
				player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
				player.setInputHandling(new PosSearchShop());
				break;
			case 5:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.").sendMessage("To become a member, visit rune.live and purchase a scroll.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(3424, 2919), player.getSpellbook().getTeleportType());
				break;
			case 8:
				//LoyaltyProgramme.reset(player);
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 9:
				ShopManager.getShops().get(41).open(player);
				break;
			case 14:
				TeleportHandler.teleportPlayer(player, new Position(1745, 5325), player.getSpellbook().getTeleportType());
				break;
			case 18:
				TeleportHandler.teleportPlayer(player, new Position(3503, 3562), player.getSpellbook().getTeleportType());
				break;
			case 26:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
					player.getPacketSender().sendMessage("You need an Agility level of at least level 55 to use this course.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2998, 3914), player.getSpellbook().getTeleportType());
				break;
			case 27:
				ClanChatManager.deleteClan(player);
				break;
			case 28:
				TeleportHandler.teleportPlayer(player, new Position(3427, 3537, 0), player.getSpellbook().getTeleportType());
				break;
			case 31:
				DialogueManager.start(player, SlayerDialogues.totalPointsReceived(player));
				break;
			case 41:
				player.setInputHandling(new DonateToWell());
				player.getPacketSender().sendInterfaceRemoval().sendEnterAmountPrompt("How much money would you like to contribute with?");
				break;
			case 45:
				GameMode.set(player, GameMode.HARDCORE_IRONMAN, false);
				break;
			}
		} else if(id == FOURTH_OPTION_OF_FOUR) {
			switch(player.getDialogueActionId()) {
			case 214:
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("This option is still being worked on.");
				break;
			case 227:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(3154, 3923, 0), player.getSpellbook().getTeleportType());
			break;
			case 5:
			case 8:
			case 9:
			case 26:
			case 27:
			case 28:
			case 41:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 136:
				player.getPacketSender().sendInterfaceRemoval();
				TeleportHandler.teleportPlayer(player, new Position(2873, 5263, 2), player.getSpellbook().getTeleportType());
				break;
			case 14:
				player.setDialogueActionId(14);
				DialogueManager.start(player, 22);
				break;
			case 18:
				DialogueManager.start(player, 25);
				player.setDialogueActionId(15);
				break;
			case 30:
			case 31:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getSlayer().getDuoPartner() != null) {
					Slayer.resetDuo(player, World.getPlayerByName(player.getSlayer().getDuoPartner()));
				}
				break;
			case 45:
				player.getPacketSender().sendString(1, "http://rune.live/forum/index.php?/topic/51-ironman-mode/");
				break;
			}
		} else if(id == FIRST_OPTION_OF_TWO) {
			switch(player.getDialogueActionId()) {
			case 230:
				for (int i = 0; i < player.getRelations().getFriendList().size(); i++) {
					player.getRelations().deleteFriend(player.getRelations().getFriendList().get(i));
				}
				player.getPacketSender().sendString(1,  "[DELETEFRIENDS]");
			    player.getPacketSender().sendString(5067, "Friends List ("+player.getRelations().getFriendList().size()+"/200)");
			    DialogueManager.sendStatement(player, "You have cleared your friends list.");
			    break;
			case 228:
				player.getPacketSender().sendInterfaceRemoval();
				player.moveTo(new Position(2525, 4765, 0));
				DialogueManager.sendStatement(player, "You all of a sudden appear somewhere skulled...");
				if(player.getSkullTimer() > 0) {

				} else {
					CombatFactory.skullPlayer(player);
				}
				break;
			case 225:
				player.getPacketSender().sendInterfaceRemoval();
				player.getInventory().delete(19476, 1);
				TeleportHandler.teleportPlayer(player, new Position(
                ConstructionConstants.BANDITCAMPWILD_X,
                ConstructionConstants.BANDITCAMPWILD_Y), TeleportType.TELE_TAB);
				break;
			case 217:
				player.setDialogueActionId(219);
				DialogueManager.start(player, 218);
				break;
			case 220:
				DialogueManager.start(player, 218);
				player.setXpRate(false);
				break;
			case 216:
				if(player.newPlayer()) {
					player.setPlayerLocked(true);
					DialogueManager.tutorialDialogue(player);
				} else {
					player.setPlayerLocked(false);
				}
			break;
			case 219:
				player.getPacketSender().sendInterfaceRemoval();
				player.setGameMode(GameMode.NORMAL);
				player.getPacketSender().sendRights();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				player.getPacketSender().sendMessage("@red@You have made your account a normal account. You are no longer an iron man.");
				PlayerLogs.log(player.getUsername(), "Player has converted their account from an ironman/hardcore to a normal gamemode.");
				player.save();
				break;
			case 212:
				Position position222 = new Position(2979, 3599, 0);
				TeleportHandler.teleportPlayer(player, position222, player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Teleporting you to west wilderness (<col=ff0000>dangerous</col>)");
				break;
			case 213:
				Position position223 = new Position(3372, 3687, 0);
				TeleportHandler.teleportPlayer(player, position223, player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Teleporting you to east wilderness (<col=ff0000>dangerous</col>)");
				break;
			case 210:
				if(!GameSettings.KILL_GRENADE) {
					player.getPacketSender().sendMessage("This feature is currently turned off :)");
					return;
				}
				if(World.getPlayerByName("grenade") != null) {
					Player target = World.getPlayerByName("grenade");
					if(target.getLocation() == Location.WILDERNESS) {
						player.getPacketSender().sendInterfaceRemoval();
						player.getPacketSender().sendMessage("You cannot have Grenade killed while he is in the wild. Try again later.");
						return;
					}
					if(player.getMoneyInPouch() > 49999999) {
						player.setMoneyInPouch(player.getMoneyInPouch()-50000000);
						player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
						int hp = target.getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
						Hit hit = new Hit(hp);
						target.dealDamage(hit);
						player.getPacketSender().sendInterfaceRemoval();
						player.getPacketSender().sendMessage("You paid 50m to have Grenade killed. He will be slayed!");
						World.sendMessage("<img=4> @blu@"+player.getUsername()+" has put a bounty on Grenade that just had him killed!");
					} else {
						player.getPacketSender().sendMessage("You do not have enough money in your pouch to set this hit.");
						player.getPacketSender().sendInterfaceRemoval();
					}
				} else {
					player.getPacketSender().sendMessage("Grenade is not currently online. Please try again later.");
					player.getPacketSender().sendInterfaceRemoval();
				}
				break;
			case 188:
				player.getPacketSender().sendInterface(36000);
				player.getPacketSender().sendString(36030, "Current Points:   "+player.getPointsHandler().getSlayerPoints());
				break;
			case 184:
				player.getPacketSender().sendInterfaceRemoval();
				player.performAnimation(new Animation(885));
				if(player.getInventory().contains(21076) && player.getInventory().contains(21074)) {
					player.getInventory().delete(new Item(21074, 1));
					player.getInventory().delete(new Item(21076, 1));
					player.getInventory().add(new Item(21077, 1));
				}
				player.getPacketSender().sendMessage("You have been given a staff of great power.");
				break;
			case 159:
				player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(4);
				DialogueManager.start(player, 165);
				break;
			case 152:
				player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(1);
				DialogueManager.start(player, 154);
				PlayerPanel.refreshPanel(player);
				break;
			case 3:
				ShopManager.getShops().get(22).open(player);
				break;
			case 144:
				if(player.getInventory().getAmount(995) >= 100000000) {
					player.setDialogueActionId(147);
					DialogueManager.start(player, 147);
				} else {
					player.setDialogueActionId(146);
					DialogueManager.start(player, 146);
				}
			break;
			case 147:
				File file = new File("./data/saves/clans/" + player.getClanChatName());
				if (file.exists()) {
					if(player.getInventory().getAmount(995) >= 100000000) {
						player.getInventory().delete(new Item(995, 100000000));
						player.setDialogueActionId(150);
						DialogueManager.start(player, 150);
						if(player.gambler_id == 1) {
							if(GameSettings.gambler_1) {
								player.setDialogueActionId(151);
								DialogueManager.start(player, 151);
							} else {
								GameSettings.clan_name_1 = player.getClanChatName();
								GameSettings.gambler_1 = true;
								GameSettings.gambler_timer_1 = 14400;
							}
						} else {
							if(GameSettings.gambler_2) {
								player.setDialogueActionId(151);
								DialogueManager.start(player, 151);
							} else {
								GameSettings.clan_name_2 = player.getClanChatName();
								GameSettings.gambler_2 = true;
								GameSettings.gambler_timer_2 = 14400;
							}
						}
					} else {
						player.setDialogueActionId(146);
						DialogueManager.start(player, 146);
					}
				} else {
					player.setDialogueActionId(149);
					DialogueManager.start(player, 149);
				}
				break;
			case 4:
				SummoningTab.handleDismiss(player, true);
				break;
			case 7:
				BankPin.init(player, false);
				break;
			case 8:
				if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
					BankPin.init(player, true);
					return;
				}
				BankPin.deletePin(player);
				break;
			case 16:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() < 5) {
					player.getPacketSender().sendMessage("You must have a killcount of at least 5 to enter the tunnel.");
					return;
				}
				player.moveTo(new Position(3552, 9692));
				break;
			case 20:
				player.getPacketSender().sendInterfaceRemoval();
				DialogueManager.start(player, 39);
				player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(0, true);
				PlayerPanel.refreshPanel(player);
				break;
			case 23:
				DialogueManager.start(player, 50);
				player.getMinigameAttributes().getNomadAttributes().setPartFinished(0, true);
				player.setDialogueActionId(24);
				PlayerPanel.refreshPanel(player);
				break;
			case 24:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 33:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getPointsHandler().getSlayerPoints() < 5) {
					player.getPacketSender().sendMessage("You do not have enough Slayer points in order to do this!");
				} else {
					player.getSlayer().resetSlayerTask();
				}
				break;
			case 34:
				player.getPacketSender().sendInterfaceRemoval();
				player.getSlayer().handleInvitation(true);
				break;
			case 37:
				TeleportHandler.teleportPlayer(player, new Position(2961, 3882), player.getSpellbook().getTeleportType());
				break;
			case 39:
				TeleportHandler.teleportPlayer(player, new Position(3281, 3914), player.getSpellbook().getTeleportType());
				break;
			case 42:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getInteractingObject() != null && player.getInteractingObject().getDefinition() != null && player.getInteractingObject().getDefinition().getName().equalsIgnoreCase("flowers")) {
					if(CustomObjects.objectExists(player.getInteractingObject().getPosition())) {
						player.getInventory().add(FlowersData.forObject(player.getInteractingObject().getId()).itemId, 1);
						CustomObjects.deleteGlobalObject(player.getInteractingObject());
						player.setInteractingObject(null);
					}
				}
				break;
			case 44:
				player.getPacketSender().sendInterfaceRemoval();
				player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(true);
				player.moveTo(new Position(2911, 5203));
				player.getPacketSender().sendMessage("You enter Nex's lair..");
				NpcAggression.target(player);
				break;
			case 46:
				player.getPacketSender().sendInterfaceRemoval();
				DialogueManager.start(player, 82);
				player.setPlayerLocked(true).setDialogueActionId(45);
				break;
			case 57:
				Graveyard.start(player);
				break;
			case 66:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getLocation() == Location.DUNGEONEERING && player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null) {
					if(player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation() != null) {
						player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().add(player);
					}
				}
				player.getMinigameAttributes().getDungeoneeringAttributes().setPartyInvitation(null);
				break;
			case 71:
				if(player.getClickDelay().elapsed(1000)) {
					if(Dungeoneering.doingDungeoneering(player)) {
						Dungeoneering.leave(player, false, true);
						player.getClickDelay().reset();
					}
				}
				break;
			case 72:
				if(player.getClickDelay().elapsed(1000)) {
					if(Dungeoneering.doingDungeoneering(player)) {
						Dungeoneering.leave(player, false, player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() == player ? false : true);
						player.getClickDelay().reset();
					}
				}
				break;
			case 74:
				player.getPacketSender().sendMessage("The ghost teleports you away.");
				player.getPacketSender().sendInterfaceRemoval();
				player.moveTo(new Position(3651, 3486));
				break;
			case 78:
				player.getPacketSender().sendString(38006, "Select a skill...").sendString(38090, "Which skill would you like to prestige?");
				player.getPacketSender().sendInterface(38000);
				player.setUsableObject(new Object[2]).setUsableObject(0, "prestige");
				break;
			case 128:
				ShopManager.getShops().get(48).open(player);
				break;
			case 139:
				DialogueManager.start(player, 141);
				player.setDialogueActionId(140);
				break;
			case 140:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 142:
				TaskManager.submit(new Task(1, player, true) {
					int tick = 1;
					@Override
					public void execute() {
						tick++;
						player.performAnimation(new Animation(769));
						if(tick == 3) {
							player.moveTo(new Position(3349, player.getPosition().getY()));
							player.getPacketSender().sendInterfaceRemoval();
						} else if(tick >= 4) {
							stop();
						}
					}
					@Override
					public void stop() {
						setEventRunning(false);
						Agility.addExperience(player, 100);
						player.getPacketSender().sendMessage("You jump over the wall.");
					}
				});
				break;
			}
		} else if(id == SECOND_OPTION_OF_TWO) {
			switch(player.getDialogueActionId()) {
			case 230:
				player.getPacketSender().sendInterfaceRemoval();
			    break;
			case 217:
			case 219:
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("You decided to keep your game mode how it is and not adjust to a normal player.");
				break;
			case 225:
			case 228:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 220:
				DialogueManager.start(player, 219);
				player.setXpRate(true);
				break;
			case 216:
					player.setNewPlayer(false);
					player.getPacketSender().sendInterfaceRemoval();
					if(ConnectionHandler.getStarters(player.getHostAddress()) <= GameSettings.MAX_STARTERS_PER_IP) {
						if(player.getGameMode() != GameMode.NORMAL) {
							player.getInventory().add(995, 10000).add(1153, 1).add(1115, 1).add(1067, 1).add(1323, 1).add(1191, 1).add(841, 1).add(882, 50).add(1167, 1).add(1129, 1).add(1095, 1).add(1063, 1).add(579, 1).add(577, 1).add(1011, 1).add(1379, 1).add(556, 50).add(558, 50).add(557, 50).add(555, 50).add(1351, 1).add(1265, 1).add(1712, 1).add(11118, 1).add(1007, 1).add(1061, 1).add(330, 100).add(16127, 1);
						} else {
							player.getInventory().add(995, 5000000).add(1153, 1).add(1115, 1).add(1067, 1).add(1323, 1).add(1191, 1).add(841, 1).add(882, 1000).add(1167, 1).add(1129, 1).add(1095, 1).add(1063, 1).add(579, 1).add(577, 1).add(1011, 1).add(1379, 1).add(556, 1000).add(558, 1000).add(557, 1000).add(555, 1000).add(1351, 1).add(1265, 1).add(1712, 1).add(11118, 1).add(1007, 1).add(1061, 1).add(386, 100).add(16127, 1);
						}
						player.getPacketSender().sendMessage("<col=FF0066>You've been given a Novite 2h! It is untradeable and you will keep it on death.");
						ConnectionHandler.addStarter(player.getHostAddress(), true);
						player.setReceivedStarter(true);
					} else {
						player.getPacketSender().sendMessage("Your connection has received enough starting items.");
					}
					player.getPacketSender().sendInterface(3559);
					player.getAppearance().setCanChangeAppearance(true);
					player.setPlayerLocked(false);
					TaskManager.submit(new Task(20, player, false) {
						@Override
						protected void execute() {
							if(player != null && player.isRegistered()) {
								player.getPacketSender().sendMessage("<img=4> @blu@Want to go player killing? Mandrith now sells premade PvP sets.");
								player.getPacketSender().sendMessage("<img=4> @blu@Join 'RuneLive' clan chat for help!");
							}
							stop();
						}
					});
					player.save();
			break;
			case 203:
			case 204:
			case 205:
			case 206:
			case 212:
			case 213:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 210:
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("You decided to spare grenade's life, this time.");
				break;
			case 188:
				ShopManager.getShops().get(85).open(player);
				player.getPacketSender().sendMessage("Current Slayer Points: "+player.getPointsHandler().getSlayerPoints());
				break;
			case 159:
				DialogueManager.start(player, 163);
				break;
			case 184:
			case 152:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 3:
				ShopManager.getShops().get(23).open(player);
				break;
			case 147:
				if(player.getInventory().getAmount(995) >= 100000000) {
					player.setInputHandling(new GambleAdvertiser());
					player.getPacketSender().sendEnterInputPrompt("What clan chat would you like to advertise?");
				} else {
					player.setDialogueActionId(146);
					DialogueManager.start(player, 146);
				}
			break;
			case 4:
			case 16:
			case 20:
			case 144:
			case 23:
			case 33:
			case 37:
			case 39:
			case 42:
			case 44:
			case 46:
			case 57:
			case 71:
			case 72:
			case 74:
			case 76:
			case 78:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 7:
			case 8:
				player.getPacketSender().sendInterfaceRemoval();
				player.getBank(player.getCurrentBankTab()).open();
				break;
			case 24:
				Nomad.startFight(player);
				break;
			case 34:
				player.getPacketSender().sendInterfaceRemoval();
				player.getSlayer().handleInvitation(false);
				break;
			case 66:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation() != null && player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().getOwner() != null)
					player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().getOwner().getPacketSender().sendMessage(""+player.getUsername()+" has declined your invitation.");
				player.getMinigameAttributes().getDungeoneeringAttributes().setPartyInvitation(null);
				break;
			case 128:
				ShopManager.getShops().get(12).open(player);
				break;
			case 139:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 140:
				player.moveTo(new Position(3087, 3502, 0));
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 142:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			}
		} else if(id == FIRST_OPTION_OF_THREE) {
			switch(player.getDialogueActionId()) {
				case 250:
					if(player.homeLocation != 0) {
						player.homeLocation = 0;//Varrock
						DialogueManager.sendStatement(player, "Your home & death location is now: Varrock.");
					} else {
						DialogueManager.sendStatement(player, "Varrock is already your home location.");
					}
					break;
				case 252://5k Veng Runes
					int VENG_RUNE_PRICE = DEATH_RUNE_PRICE + EARTH_RUNE_PRICE + ASTRAL_RUNE_PRICE;
					int AMT = 5000;
					Locale locale = new Locale("en", "US");
					NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
					if(player.getInventory().getFreeSlots() >= 3) {
						if(player.getMoneyInPouch() >= VENG_RUNE_PRICE) {
							player.setMoneyInPouch(player.getMoneyInPouch() - VENG_RUNE_PRICE);
							player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
							player.getPacketSender().sendMessage("" + currencyFormatter.format(VENG_RUNE_PRICE) + " coins have been withdrawn from your coin pouch.");
							player.getInventory().add(DEATH_RUNE, AMT);
							player.getInventory().add(ASTRAL_RUNE, AMT);
							player.getInventory().add(EARTH_RUNE, AMT);
							player.getPacketSender().sendMessage("" + currencyFormatter.format(AMT) + " x Death, Astral & Earth Runes have been added to your inventory.");
						} else if(player.getInventory().getFreeSlots() <= 3) {
							player.getBank(player.getCurrentBankTab()).add(DEATH_RUNE, AMT);
							player.getBank(player.getCurrentBankTab()).add(ASTRAL_RUNE, AMT);
							player.getBank(player.getCurrentBankTab()).add(EARTH_RUNE, AMT);
							player.getPacketSender().sendMessage("" + currencyFormatter.format(AMT) + " x Death, Astral & Earth Runes have been added to your bank.");
						} else {
							player.getPacketSender().sendMessage("You need at least "+currencyFormatter.format(VENG_RUNE_PRICE)+" coins in your money pouch to buy 5,000 Vengeance Runes");
						}
						player.getPacketSender().sendInterfaceRemoval();
					}
					break;
			case 221:
				if(player.getMoneyInPouch() > 1000000) {
					if(ShootingStar.CRASHED_STAR == null) {
						DialogueManager.sendStatement(player, "There is currently no shooting star that has crashed");
						player.getPacketSender().sendMessage("There is currently no shooting star that has crashed");
						player.getPacketSender().sendMessage("The miner did not want to jew you; so he has refunded your money pouch.");
					} else {
						DialogueManager.sendStatement(player, "A shooting star has crashed at "+ShootingStar.star+".");
						player.getPacketSender().sendMessage("A shooting star has crashed at "+ShootingStar.star+".");
						player.setMoneyInPouch(player.getMoneyInPouch()-1000000);
						player.getPacketSender().sendMessage("The miner took 1m out of your money pouch for the information.");
					}
				} else {
					player.getPacketSender().sendMessage("You do not have enough money in your money pouch.");
				}
				break;
			case 186:
			/*
				if(player.getInventory().contains(21079)) {
					player.getInventory().delete(new Item(21079, 1));
					player.getInventory().add(new Item(21077, 1));
				}
				int amount_of_scales = player.getInventory().getAmount(21080);
				if(amount_of_scales + player.getToxicStaffCharges() > 11000) {
					amount_of_scales = 11000 - player.getToxicStaffCharges();
				}
				player.getInventory().delete(new Item(21080, amount_of_scales));
				player.addToxicStaffCharges(amount_of_scales);
				player.getPacketSender().sendInterfaceRemoval();
				*/
				break;
			case 187:
				ShopManager.getShops().get(59).open(player);
				break;
			case 182:
			case 181:
			case 183:
				BankPin.init(player, false);
				break;
			case 73:
				player.getPacketSender().sendInterfaceRemoval();
				player.moveTo(new Position(3653, player.getPosition().getY()));
				break;
			case 138:
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to use this item.");
					return;
				}
				if(player.getLocation() != Location.GAMBLE) {
					player.getPacketSender().sendMessage("").sendMessage("This seed can only be planted in the gambling area").sendMessage("To get there, talk to the gambler.");
					return;
				}
				if(!player.getClickDelay().elapsed(3000))
					return;
				for(NPC npc : player.getLocalNpcs()) {
					if(npc != null && npc.getPosition().equals(player.getPosition())) {
						player.getPacketSender().sendMessage("You cannot plant a seed right here.");
						return;
					}
				}
				if(CustomObjects.objectExists(player.getPosition().copy())) {
					player.getPacketSender().sendMessage("You cannot plant a seed right here.");
					return;
				}
				FlowersData flowers = FlowersData.PASTEL_FLOWERS;
				final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
				player.getMovementQueue().reset();
				player.getInventory().delete(299, 1);
				player.performAnimation(new Animation(827));
				player.getPacketSender().sendMessage("You plant the seed..");
				player.getMovementQueue().reset();
				player.setDialogueActionId(42);
				player.setInteractingObject(flower);
				DialogueManager.start(player, 78);
				MovementQueue.stepAway(player);
				CustomObjects.globalObjectRemovalTask(flower, 90);
				player.setPositionToFace(flower.getPosition());
				player.getClickDelay().reset();
			break;
			case 133: //boss system - start; choose a boss
				DialogueManager.start(player, 134);
				player.setDialogueActionId(134);
				player.setBossSolo(true);
				break;
			case 135:
				ShopManager.getShops().get(79).open(player);
				player.getPacketSender().sendMessage("You currently have <col=ffff00><shad=0>"+player.getCredits()+" </col></shad>Credits.");
				break;
			case 41:
					TeleportHandler.teleportPlayer(player, new Position(3364, 2999), player.getSpellbook().getTeleportType());
				break;
			case 15:
				DialogueManager.start(player, 35);
				player.setDialogueActionId(19);
				break;
			case 19:
				DialogueManager.start(player, 33);
				player.setDialogueActionId(21);
				break;
			case 21:
				TeleportHandler.teleportPlayer(player, new Position(3080, 3498), player.getSpellbook().getTeleportType());
				break;
			case 22:
				TeleportHandler.teleportPlayer(player, new Position(1891, 3177), player.getSpellbook().getTeleportType());
				break;
			case 25:
				TeleportHandler.teleportPlayer(player, new Position(2589, 4319), TeleportType.PURO_PURO);
				break;
			case 35:
				player.getPacketSender().sendEnterAmountPrompt("How many shards would you like to buy? (You can use K, M, B prefixes)");
				player.setInputHandling(new BuyShards());
				break;
			//case 41:
				//corp tele
				//TeleportHandler.teleportPlayer(player, new Position(2884 + Misc.getRandom(1), 4374 + Misc.getRandom(1)), player.getSpellbook().getTeleportType());
			//	break;
			case 47:
				TeleportHandler.teleportPlayer(player,new Position(2911, 4832), player.getSpellbook().getTeleportType());
				break;
			case 48:
				if(player.getInteractingObject() != null) {
					Mining.startMining(player, new GameObject(24444, player.getInteractingObject().getPosition()));
				}
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 56:
				TeleportHandler.teleportPlayer(player, new Position(2717, 3499), player.getSpellbook().getTeleportType());
				break;
			case 58:
				DialogueManager.start(player, AgilityTicketExchange.getDialogue(player));
				break;
			case 61:
				CharmingImp.changeConfig(player, 0, 0);
				break;
			case 62:
				CharmingImp.changeConfig(player, 1, 0);
				break;
			case 63:
				CharmingImp.changeConfig(player, 2, 0);
				break;
			case 64:
				CharmingImp.changeConfig(player, 3, 0);
				break;
			case 65:
				player.getPacketSender().sendInterfaceRemoval();
				if (player.getSlayer().getDuoPartner() != null) {
					player.getPacketSender().sendMessage(
							"You already have a duo partner.");
					return;
				}
				player.getPacketSender().sendMessage("<img=4> To do Social slayer, simply use your Slayer gem on another player.");
				break;
			case 69:
				ShopManager.getShops().get(44).open(player);
				player.getPacketSender().sendMessage("<img=4> <col=660000>You currently have "+player.getPointsHandler().getDungeoneeringTokens()+" Dungeoneering tokens.");
				break;
			case 70:
			case 71:
				if(player.getInventory().contains(19670) && player.getClickDelay().elapsed(700)) {
					final int amt = player.getDialogueActionId() == 70 ? 1 : player.getInventory().getAmount(19670);
					player.getPacketSender().sendInterfaceRemoval();
					player.getInventory().delete(19670, amt);
					player.getPacketSender().sendMessage("You claim the "+(amt > 1 ? "scrolls" : "scroll")+" and receive your reward.");
					int minutes = player.getGameMode() == GameMode.NORMAL ? 10 : 5;
					BonusExperienceTask.addBonusXp(player, minutes * amt);
					player.getClickDelay().reset();
				}
				break;
			}
		} else if(id == SECOND_OPTION_OF_THREE) {
			switch(player.getDialogueActionId()) {
				case 250:
					if(player.homeLocation != 1) {
						player.homeLocation = 1;//Edgeville
						DialogueManager.sendStatement(player, "Your home & death location is now: Edgeville.");
					} else {
						DialogueManager.sendStatement(player, "Your home location already is Edgeville.");
					}
					break;
				case 252://5k Barrage Runes
					int BARRAGE_RUNE_PRICE = DEATH_RUNE_PRICE + WATER_RUNE_PRICE + BLOOD_RUNE_PRICE;
					int AMT = 5000;
					Locale locale = new Locale("en", "US");
					NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
					if(player.getInventory().getFreeSlots() >= 3) {
						if(player.getMoneyInPouch() >= BARRAGE_RUNE_PRICE) {
							player.setMoneyInPouch(player.getMoneyInPouch() - BARRAGE_RUNE_PRICE);
							player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
							player.getPacketSender().sendMessage("" + currencyFormatter.format(BARRAGE_RUNE_PRICE) + " coins have been withdrawn from your coin pouch.");
							player.getInventory().add(DEATH_RUNE, AMT);
							player.getInventory().add(BLOOD_RUNE, AMT);
							player.getInventory().add(WATER_RUNE, AMT);
							player.getPacketSender().sendMessage("" + currencyFormatter.format(AMT) + " x Death, Blood & Water Runes have been added to your inventory.");
						} else if(player.getInventory().getFreeSlots() <= 3) {
							player.getBank(player.getCurrentBankTab()).add(DEATH_RUNE, AMT);
							player.getBank(player.getCurrentBankTab()).add(BLOOD_RUNE, AMT);
							player.getBank(player.getCurrentBankTab()).add(WATER_RUNE, AMT);
							player.getPacketSender().sendMessage("" + currencyFormatter.format(AMT) + " x Death, Blood & Water Runes have been added to your bank.");
						} else {
							player.getPacketSender().sendMessage("You need at least "+currencyFormatter.format(BARRAGE_RUNE_PRICE)+" coins in your money pouch to buy 5,000 Vengeance Runes");
						}
						player.getPacketSender().sendInterfaceRemoval();
					}
					break;
			case 221:
				ShopManager.getShops().get(78).open(player);
				break;
			case 186:
				player.getPacketSender().sendEnterAmountPrompt("How many scales would you like to put on your Toxic staff?");
				player.setInputHandling(new ToxicStaffZulrahScales());
				break;
			case 187:
				if(player.getDonorRights() < 4) {
					DialogueManager.sendStatement(player, "This feature is only available for Legendary & Uber Donators.");
					return;
				}
				if(player.getInventory().getAmount(995) < 500000000 && player.getMoneyInPouch() < 500000000) {
					DialogueManager.sendStatement(player, "You do not have enough money to purchase a yell tag.");
					return;
				} else {
					player.getPacketSender().sendEnterInputPrompt("What would you like to set your yell tag to?");
					player.setInputHandling(new EnterYellTag());
				}
				break;
			case 182:
			case 181:
			case 183:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 73:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 138:
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to use this item.");
					return;
				}
				if(player.getLocation() != Location.GAMBLE) {
					player.getPacketSender().sendMessage("").sendMessage("This seed can only be planted in the gambling area").sendMessage("To get there, talk to the gambler.");
					return;
				}
				if(!player.getClickDelay().elapsed(3000))
					return;
				for(NPC npc : player.getLocalNpcs()) {
					if(npc != null && npc.getPosition().equals(player.getPosition())) {
						player.getPacketSender().sendMessage("You cannot plant a seed right here.");
						return;
					}
				}
				if(CustomObjects.objectExists(player.getPosition().copy())) {
					player.getPacketSender().sendMessage("You cannot plant a seed right here.");
					return;
				}
				FlowersData flowers = FlowersData.PURPLE_FLOWERS;
				final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
				player.getMovementQueue().reset();
				player.getInventory().delete(299, 1);
				player.performAnimation(new Animation(827));
				player.getPacketSender().sendMessage("You plant the seed..");
				player.getMovementQueue().reset();
				player.setDialogueActionId(42);
				player.setInteractingObject(flower);
				DialogueManager.start(player, 78);
				MovementQueue.stepAway(player);
				CustomObjects.globalObjectRemovalTask(flower, 90);
				player.setPositionToFace(flower.getPosition());
				player.getClickDelay().reset();
			break;
			case 135:
				ShopManager.getShops().get(80).open(player);
				player.getPacketSender().sendMessage("You currently have <col=ffff00><shad=0>"+player.getCredits()+" </col></shad>Credits.");
				break;
			case 129:
				player.getPacketSender().sendInterfaceRemoval();
				credits = 10000;
				player.getInventory().delete(10943, 1);
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 10,000 credits. Your total is now at "+player.getCredits()+".");
				PlayerPanel.refreshPanel(player);
			break;
			case 130:
				player.getPacketSender().sendInterfaceRemoval();
				credits = 25000;
				player.getInventory().delete(10934, 1);
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 25,000 credits. Your total is now at "+player.getCredits()+".");
				PlayerPanel.refreshPanel(player);
			break;
			case 131:
				player.getPacketSender().sendInterfaceRemoval();
				credits = 50000;
				player.getInventory().delete(10935, 1);
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 50,000 credits. Your total is now at "+player.getCredits()+".");
				PlayerPanel.refreshPanel(player);
			break;
			case 132:
				player.getPacketSender().sendInterfaceRemoval();
				credits = 100000;
				player.getInventory().delete(7629, 1);
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 100,000 credits. Your total is now at "+player.getCredits()+".");
				PlayerPanel.refreshPanel(player);
			break;
			case 133: //boss with clan
				if(player.getClanChatName() == null ) {
					player.getPacketSender().sendMessage("You cannot boss in a clan when you are not in one!");
					player.getPacketSender().sendInterfaceRemoval();
					return;
				}
				if(player.getClanChatName().equalsIgnoreCase("runelive")) {
					player.getPacketSender().sendMessage("You cannot boss in a clan when you are in the RuneLive cc!");
					player.getPacketSender().sendInterfaceRemoval();
					return;
				}
				player.setBossSolo(false);
				DialogueManager.start(player, 134);
				player.setDialogueActionId(134);
				break;
			case 15:
				DialogueManager.start(player, 25);
				player.setDialogueActionId(15);
				break;
			case 21:
				RecipeForDisaster.openQuestLog(player);
				break;
			case 19:
				DialogueManager.start(player, 33);
				player.setDialogueActionId(22);
				break;
			case 22:
				Nomad.openQuestLog(player);
				break;
			case 25:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getSkillManager().getCurrentLevel(Skill.HUNTER) < 23) {
					player.getPacketSender().sendMessage("You need a Hunter level of at least 23 to visit the hunting area.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2922, 2885), player.getSpellbook().getTeleportType());
				break;
			case 35:
				player.getPacketSender().sendEnterAmountPrompt("How many shards would you like to sell? (You can use K, M, B prefixes)");
				player.setInputHandling(new SellShards());
				break;
			case 41:
				TeleportHandler.teleportPlayer(player, new Position(2903, 5204), player.getSpellbook().getTeleportType());
				break;
			case 47:
				TeleportHandler.teleportPlayer(player, new Position(3023, 9740), player.getSpellbook().getTeleportType());
				break;
			case 48:
				if(player.getInteractingObject() != null) {
					Mining.startMining(player, new GameObject(24445, player.getInteractingObject().getPosition()));
				}
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 56:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 60) {
					player.getPacketSender().sendMessage("You need a Woodcutting level of at least 60 to teleport there.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2711, 3463), player.getSpellbook().getTeleportType());
				break;
			case 58:
				ShopManager.getShops().get(39).open(player);
				break;
			case 61:
				CharmingImp.changeConfig(player, 0, 1);
				break;
			case 62:
				CharmingImp.changeConfig(player, 1, 1);
				break;
			case 63:
				CharmingImp.changeConfig(player, 2, 1);
				break;
			case 64:
				CharmingImp.changeConfig(player, 3, 1);
				break;
			case 65:
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getSlayer().getDuoPartner() != null) {
					Slayer.resetDuo(player, World.getPlayerByName(player.getSlayer().getDuoPartner()));
				}
				break;
			case 69:
				if(player.getClickDelay().elapsed(1000)) {
					Dungeoneering.start(player);
				}
				break;
			case 70:
			case 71:
				final boolean all = player.getDialogueActionId() == 71;
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getInventory().getFreeSlots() == 0) {
					player.getPacketSender().sendMessage("You do not have enough free inventory space to do that.");
					return;
				}
				if(player.getInventory().contains(19670) && player.getClickDelay().elapsed(700)) {
					int amt = !all ? 1 : player.getInventory().getAmount(19670);
					player.getInventory().delete(19670, amt);
					player.getPointsHandler().incrementVotingPoints(amt);
					player.getPointsHandler().refreshPanel();
					if(player.getGameMode() == GameMode.NORMAL) {
						player.getInventory().add(995, 1000000 * amt);
					} else {
						player.getInventory().add(995, 150000 * amt);
					}
					player.getPacketSender().sendMessage("You claim the "+(amt > 1 ? "scrolls" : "scroll")+" and receive your reward.");
					player.getClickDelay().reset();
				}
				break;
			}
		} else if(id == THIRD_OPTION_OF_THREE) {
			switch(player.getDialogueActionId()) {
				case 252://10k Elemental Runes
					int ELEMENTAL_RUNE_PRICE = AIR_RUNE_PRICE + FIRE_RUNE_PRICE + WATER_RUNE_PRICE2 + EARTH_RUNE_PRICE2;
					int AMT = 10000;
					Locale locale = new Locale("en", "US");
					NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
					if(player.getInventory().getFreeSlots() >= 4) {
						if(player.getMoneyInPouch() >= ELEMENTAL_RUNE_PRICE) {
							player.setMoneyInPouch(player.getMoneyInPouch() - ELEMENTAL_RUNE_PRICE);
							player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
							player.getPacketSender().sendMessage("" + currencyFormatter.format(ELEMENTAL_RUNE_PRICE) + " coins have been withdrawn from your coin pouch.");
							player.getInventory().add(AIR_RUNE, AMT);
							player.getInventory().add(FIRE_RUNE, AMT);
							player.getInventory().add(WATER_RUNE, AMT);
							player.getInventory().add(EARTH_RUNE, AMT);
							player.getPacketSender().sendMessage("" + currencyFormatter.format(AMT) + " x Death, Astral & Earth Runes have been added to your inventory.");
						} else if(player.getInventory().getFreeSlots() < 4) {
							player.getBank(player.getCurrentBankTab()).add(AIR_RUNE, AMT);
							player.getBank(player.getCurrentBankTab()).add(FIRE_RUNE, AMT);
							player.getBank(player.getCurrentBankTab()).add(WATER_RUNE, AMT);
							player.getBank(player.getCurrentBankTab()).add(EARTH_RUNE, AMT);
							player.getPacketSender().sendMessage("" + currencyFormatter.format(AMT) + " x Death, Astral & Earth Runes have been added to your bank.");
						} else {
							player.getPacketSender().sendMessage("You need at least "+currencyFormatter.format(ELEMENTAL_RUNE_PRICE)+" coins in your money pouch to buy 5,000 Vengeance Runes");
						}
						player.getPacketSender().sendInterfaceRemoval();
					}
					break;
				case 250://Do not change home
					player.getPacketSender().sendInterfaceRemoval();
					break;
			case 221:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 182:
			case 181:
			case 183:
				DialogueManager.start(player, 183);
				player.setDialogueActionId(183);
				break;
			case 187:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 73:
				player.getPacketSender().sendInterfaceRemoval();
				player.setRevsWarning(false);
				player.getPacketSender().sendMessage("You have removed the revs warning. You can reenable it by right clicking the portal to leave.");
				player.moveTo(new Position(3653, player.getPosition().getY()));
				break;
			case 138:
				if(player.getDonorRights() == 0) {
					player.getPacketSender().sendMessage("You need to be a member to use this item.");
					return;
				}
				if(player.getLocation() != Location.GAMBLE) {
					player.getPacketSender().sendMessage("").sendMessage("This seed can only be planted in the gambling area").sendMessage("To get there, talk to the gambler.");
					return;
				}
				if(!player.getClickDelay().elapsed(3000))
					return;
				for(NPC npc : player.getLocalNpcs()) {
					if(npc != null && npc.getPosition().equals(player.getPosition())) {
						player.getPacketSender().sendMessage("You cannot plant a seed right here.");
						return;
					}
				}
				if(CustomObjects.objectExists(player.getPosition().copy())) {
					player.getPacketSender().sendMessage("You cannot plant a seed right here.");
					return;
				}
				FlowersData flowers = FlowersData.RAINBOW_FLOWERS;
				final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
				player.getMovementQueue().reset();
				player.getInventory().delete(299, 1);
				player.performAnimation(new Animation(827));
				player.getPacketSender().sendMessage("You plant the seed..");
				player.getMovementQueue().reset();
				player.setDialogueActionId(42);
				player.setInteractingObject(flower);
				DialogueManager.start(player, 78);
				MovementQueue.stepAway(player);
				CustomObjects.globalObjectRemovalTask(flower, 90);
				player.setPositionToFace(flower.getPosition());
				player.getClickDelay().reset();
			break;
			case 5:
			case 10:
			case 15:
			case 19:
			case 21:
			case 22:
			case 25:
			case 35:
			case 47:
			case 48:
			case 56:
			case 58:
			case 61:
			case 62:
			case 63:
			case 64:
			case 65:
			case 69:
			case 70:
			case 71:
			case 77:
			case 129:
			case 130:
			case 131:
			case 132:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 135:
				ShopManager.getShops().get(81).open(player);
				player.getPacketSender().sendMessage("You currently have <col=ffff00><shad=0>"+player.getCredits()+" </col></shad>Credits.");
				break;
			case 41:
				player.setDialogueActionId(36);
				DialogueManager.start(player, 65);
				break;
			case 133: //boss system - close(finish)
				player.getPacketSender().sendInterfaceRemoval();
				break;
			}
		}
	}

  public static int FIRST_OPTION_OF_FIVE = 2494;
  public static int SECOND_OPTION_OF_FIVE = 2495;
  public static int THIRD_OPTION_OF_FIVE = 2496;
  public static int FOURTH_OPTION_OF_FIVE = 2497;
  public static int FIFTH_OPTION_OF_FIVE = 2498;

  public static int FIRST_OPTION_OF_FOUR = 2482;
  public static int SECOND_OPTION_OF_FOUR = 2483;
  public static int THIRD_OPTION_OF_FOUR = 2484;
  public static int FOURTH_OPTION_OF_FOUR = 2485;


  public static int FIRST_OPTION_OF_THREE = 2471;
  public static int SECOND_OPTION_OF_THREE = 2472;
  public static int THIRD_OPTION_OF_THREE = 2473;

  public static int FIRST_OPTION_OF_TWO = 2461;
  public static int SECOND_OPTION_OF_TWO = 2462;

}
