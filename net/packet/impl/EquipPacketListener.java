package com.ikov.net.packet.impl;

import com.ikov.model.Flag;
import com.ikov.model.Item;
import com.ikov.model.Skill;
import com.ikov.model.Locations.Location;
import com.ikov.model.container.impl.Equipment;
import com.ikov.model.container.impl.Inventory;
import com.ikov.model.definitions.WeaponAnimations;
import com.ikov.model.definitions.WeaponInterfaces;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.util.Misc;
import com.ikov.world.content.BonusManager;
import com.ikov.world.content.Sounds;
import com.ikov.world.content.Sounds.Sound;
import com.ikov.world.content.combat.magic.Autocasting;
import com.ikov.world.content.combat.weapon.CombatSpecial;
import com.ikov.world.content.minigames.impl.Dueling;
import com.ikov.world.content.minigames.impl.Dueling.DuelRule;
import com.ikov.world.entity.impl.player.Player;

/**
 * This packet listener manages the equip action a player
 * executes when wielding or equipping an item.
 * 
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		int id = packet.readShort();
		int slot = packet.readShortA();
		int interfaceId = packet.readShortA();
		if(player.getInterfaceId() > 0 && player.getInterfaceId() != 21172 /* EQUIP SCREEN */) {
			player.getPacketSender().sendInterfaceRemoval();
			//return;
		}
		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
			/*
			 * Making sure slot is valid.
			 */
			if (slot >= 0 && slot <= 28) {
				Item item = player.getInventory().getItems()[slot].copy();
				if(!player.getInventory().contains(item.getId()))
					return;
				/*
				 * Making sure item exists and that id is consistent.
				 */
				if (item != null && id == item.getId()) {
					for (Skill skill : Skill.values()) {
						if(skill == Skill.CONSTRUCTION)
							continue;
						if (item.getDefinition().getRequirement()[skill.ordinal()] > player.getSkillManager().getMaxLevel(skill)) {
							StringBuilder vowel = new StringBuilder();
							if (skill.getName().startsWith("a") || skill.getName().startsWith("e") || skill.getName().startsWith("i") || skill.getName().startsWith("o") || skill.getName().startsWith("u")) {
								vowel.append("an ");
							} else {
								vowel.append("a ");
							}
							player.getPacketSender().sendMessage("You need " + vowel.toString() + Misc.formatText(skill.getName()) + " level of at least " + item.getDefinition().getRequirement()[skill.ordinal()] + " to wear this.");
							return;
						}
					}
					if(item.getId() == 21050) {
						if(player.getSkillManager().getExperience(Skill.ATTACK) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Attack to wear this cape!");
							return;
						}
					}					
					if(item.getId() == 21051) {
						if(player.getSkillManager().getExperience(Skill.DEFENCE) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Defence to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21052) {
						if(player.getSkillManager().getExperience(Skill.STRENGTH) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Strength to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21053) {
						if(player.getSkillManager().getExperience(Skill.CONSTITUTION) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Constitution to wear this cape!");
							return;
						}
					}					
					if(item.getId() == 21054) {
						if(player.getSkillManager().getExperience(Skill.RANGED) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Range to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21055) {
						if(player.getSkillManager().getExperience(Skill.PRAYER) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Prayer to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21056) {
						if(player.getSkillManager().getExperience(Skill.MAGIC) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Magic to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21057) {
						if(player.getSkillManager().getExperience(Skill.COOKING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Cooking to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21058) {
						if(player.getSkillManager().getExperience(Skill.WOODCUTTING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Woodcutting to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21059) {
						if(player.getSkillManager().getExperience(Skill.FLETCHING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Fletching to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21060) {
						if(player.getSkillManager().getExperience(Skill.FISHING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Fishing to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21061) {
						if(player.getSkillManager().getExperience(Skill.FIREMAKING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Firemaking to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21062) {
						if(player.getSkillManager().getExperience(Skill.CRAFTING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Crafting to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21063) {
						if(player.getSkillManager().getExperience(Skill.SMITHING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Smithing to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21064) {
						if(player.getSkillManager().getExperience(Skill.MINING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Mining to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21065) {
						if(player.getSkillManager().getExperience(Skill.HERBLORE) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Herblore to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21066) {
						if(player.getSkillManager().getExperience(Skill.AGILITY) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Agility to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21067) {
						if(player.getSkillManager().getExperience(Skill.THIEVING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Thieving to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21068) {
						if(player.getSkillManager().getExperience(Skill.SLAYER) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Slayer to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21069) {
						if(player.getSkillManager().getExperience(Skill.FARMING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Farming to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21070) {
						if(player.getSkillManager().getExperience(Skill.RUNECRAFTING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Runecrafting to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21071) {
						if(player.getSkillManager().getExperience(Skill.CONSTRUCTION) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Construction to wear this cape!");
							return;
						}
					}
					if(item.getId() == 21072) {
						if(player.getSkillManager().getExperience(Skill.HUNTER) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Hunter to wear this cape!");
							return;
						}
					}	
					if(item.getId() == 21073) {
						if(player.getSkillManager().getExperience(Skill.SUMMONING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Summoning to wear this cape!");
							return;
						}
					}
					if(item.getId() == 19709) {
						if(player.getSkillManager().getExperience(Skill.DUNGEONEERING) < 500000000) {
							player.getPacketSender().sendMessage("You need atleast 500 million experience in Dungeoneering to wear this cape!");
							return;
						}
					}
					int equipmentSlot = item.getDefinition().getEquipmentSlot();
					Item equipItem = player.getEquipment().forSlot(equipmentSlot).copy();
					if(player.getLocation() == Location.DUEL_ARENA) {
						for(int i = 10; i < player.getDueling().selectedDuelRules.length; i++) {
							if(player.getDueling().selectedDuelRules[i]) {
								DuelRule duelRule = DuelRule.forId(i);
								if(equipmentSlot == duelRule.getEquipmentSlot() || duelRule == Dueling.DuelRule.NO_SHIELD && item.getDefinition().isTwoHanded()) {
									player.getPacketSender().sendMessage("The rules that were set do not allow this item to be equipped.");
									return;
								}
							}
						}
						if(player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
							if(equipmentSlot == Equipment.WEAPON_SLOT || item.getDefinition().isTwoHanded()) {
								player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
								return;
							}
						}
					}
					/*
					NEX ARMOURS
					if (equipItem.getId() == 14008 || equipItem.getId() == 14009
							|| equipItem.getId() == 14010 || equipItem.getId() == 14011 
							|| equipItem.getId() == 14012 || equipItem.getId() == 14013 
							|| equipItem.getId() == 14014 || equipItem.getId() == 14015 
							|| equipItem.getId() == 14016) {
						int newLevel = player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION)+player.calculateMaxLifePoints();
						player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, newLevel, true);
						player.getPacketSender().sendMessage("Your new hp is: " + player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION));
					}*/
					if (player.hasStaffOfLightEffect() && equipItem.getDefinition().getName().toLowerCase().contains("staff of light")) {
						player.setStaffOfLightEffect(-1);
						player.getPacketSender().sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
					}
					if (equipItem.getDefinition().isStackable() && equipItem.getId() == item.getId()) {
						int amount = equipItem.getAmount() + item.getAmount() <= Integer.MAX_VALUE ? equipItem.getAmount() + item.getAmount() : Integer.MAX_VALUE;
						player.getInventory().delete(item);
						player.getEquipment().getItems()[equipmentSlot].setAmount(amount);
						equipItem.setAmount(amount);
						player.getEquipment().refreshItems();
					} else {
						if (item.getDefinition().isTwoHanded() && item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
							int slotsNeeded = 0;
							if (player.getEquipment().isSlotOccupied(Equipment.SHIELD_SLOT) && player.getEquipment().isSlotOccupied(Equipment.WEAPON_SLOT)) {
								slotsNeeded++;
							}
							if (player.getInventory().getFreeSlots() >= slotsNeeded) {
								Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
								player.getInventory().setItem(slot, equipItem);
								player.getInventory().add(shield);
								player.getEquipment().delete(shield);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().full();
								return;
							}
						} else if (equipmentSlot == Equipment.SHIELD_SLOT && player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().isTwoHanded()) {
							player.getInventory().setItem(slot, player.getEquipment().getItems()[Equipment.WEAPON_SLOT]);
							player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(-1));
							player.getEquipment().setItem(Equipment.SHIELD_SLOT, item);
							resetWeapon(player);
						} else {
							if (item.getDefinition().getEquipmentSlot() == equipItem.getDefinition().getEquipmentSlot() && equipItem.getId() != -1) {
								if(player.getInventory().contains(equipItem.getId())) {
									player.getInventory().delete(item);
									player.getInventory().add(equipItem);
								} else
									player.getInventory().setItem(slot, equipItem);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().setItem(slot, new Item(-1, 0));
								player.getEquipment().setItem(item.getDefinition().getEquipmentSlot(), item);
							}
						}
					}
					if (equipmentSlot == Equipment.WEAPON_SLOT) {
						resetWeapon(player);
					} else if(equipmentSlot == Equipment.RING_SLOT && item.getId() == 2570) {
						player.getPacketSender().sendMessage("<img=10> <col=996633>Warning! The Ring of Life special effect does not work in the Wilderness or").sendMessage("<col=996633> Duel Arena.");
					}

					if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 4153) {
						player.getCombatBuilder().cooldown(false);
					}

					player.setCastSpell(null);
					BonusManager.update(player);
					player.getEquipment().refreshItems();
					player.getInventory().refreshItems();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Sounds.sendSound(player, Sound.EQUIP_ITEM);
				}
			}
			break;
		}
	}

	public static void resetWeapon(Player player) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		WeaponInterfaces.assign(player, weapon);
		WeaponAnimations.assign(player, weapon);
		if(player.getAutocastSpell() != null || player.isAutocast()) {
			Autocasting.resetAutocast(player, true);
			player.getPacketSender().sendMessage("Autocast spell cleared.");
		}
		player.setSpecialActivated(false);
		CombatSpecial.updateBar(player);
	}

	public static final int OPCODE = 41;
}