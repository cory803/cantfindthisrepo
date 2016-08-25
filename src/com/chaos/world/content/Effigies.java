package com.chaos.world.content;

import com.chaos.model.Animation;
import com.chaos.model.Skill;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

public class Effigies {
	//TODO: Redo effigys
	public static void handleEffigy(Player player, int effigy) {
		if (player == null)
			return;
		if (player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			return;
		}

	}

	public static boolean handleEffigyAction(Player player, int action) {
		if (player.getInteractingItem() == null
				|| player.getInteractingItem() != null && !isEffigy(player.getInteractingItem().getId())) {
			return false;
		}
		switch (action) {
		case 2461:
			if (player.getDialogueActionId() == 49) {
				openEffigy(player, 12);
				return true;
			} else if (player.getDialogueActionId() == 50) {
				openEffigy(player, 20);
				return true;
			} else if (player.getDialogueActionId() == 51) {
				openEffigy(player, 7);
				return true;
			} else if (player.getDialogueActionId() == 52) {
				openEffigy(player, 19);
				return true;
			} else if (player.getDialogueActionId() == 53) {
				openEffigy(player, 9);
				return true;
			} else if (player.getDialogueActionId() == 54) {
				openEffigy(player, 15);
				return true;
			} else if (player.getDialogueActionId() == 55) {
				openEffigy(player, 13);
				return true;
			}
			break;
		case 2462:
			if (player.getDialogueActionId() == 49) {
				openEffigy(player, 16);
				return true;
			} else if (player.getDialogueActionId() == 50) {
				openEffigy(player, 17);
				return true;
			} else if (player.getDialogueActionId() == 51) {
				openEffigy(player, 11);
				return true;
			} else if (player.getDialogueActionId() == 52) {
				openEffigy(player, 10);
				return true;
			} else if (player.getDialogueActionId() == 53) {
				openEffigy(player, 8);
				return true;
			} else if (player.getDialogueActionId() == 54) {
				openEffigy(player, 5);
				return true;
			} else if (player.getDialogueActionId() == 55) {
				openEffigy(player, 14);
				return true;
			}
			break;
		}
		return false;
	}

	public static boolean checkRequirement(Player player, int skillId, int req) {
		if (player.getSkillManager().getCurrentLevel(Skill.forId(skillId)) >= req) {
			return true;
		} else {
			String skill = Misc.formatText(Skill.forId(skillId).name().toLowerCase());
			player.getPacketSender().sendInterfaceRemoval().sendMessage(
					"You need " + Misc.anOrA(skill) + " " + skill + " level of at least " + req + " to do that.");
			return false;
		}
	}

	public static void openEffigy(Player player, int skillId) {
		int[] levelReq = { 91, 93, 95, 97 };
		if (player.getInteractingItem() == null)
			return;
		if (!player.getClickDelay().elapsed(4000)) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (player.getInteractingItem().getId() == 18778) {
			if (checkRequirement(player, skillId, levelReq[0]) && player.getInventory().contains(18778)) {
				player.getInventory().delete(18778, 1);
				player.getInventory().add(18779, 1);
				player.getSkillManager().addExactExperience(Skill.forId(skillId), 15000);
				player.getClickDelay().reset();
				player.performAnimation(new Animation(7368));
				player.getPacketSender().sendInterfaceRemoval();
				player.setEffigy(0);
				player.setInteractingItem(null);
				return;
			}
		}
		if (player.getInteractingItem().getId() == 18779) {
			if (checkRequirement(player, skillId, levelReq[1]) && player.getInventory().contains(18779)) {
				player.getInventory().delete(18779, 1);
				player.getInventory().add(18780, 1);
				player.getSkillManager().addExactExperience(Skill.forId(skillId), 30000);
				player.getClickDelay().reset();
				player.performAnimation(new Animation(7368));
				player.getPacketSender().sendInterfaceRemoval();
				player.setEffigy(0);
				player.setInteractingItem(null);
				return;
			}
		}
		if (player.getInteractingItem().getId() == 18780) {
			if (checkRequirement(player, skillId, levelReq[2]) && player.getInventory().contains(18780)) {
				player.getInventory().delete(18780, 1);
				player.getInventory().add(18781, 1);
				player.getSkillManager().addExactExperience(Skill.forId(skillId), 45000);
				player.getClickDelay().reset();
				player.performAnimation(new Animation(7368));
				player.getPacketSender().sendInterfaceRemoval();
				player.setEffigy(0);
				player.setInteractingItem(null);
				return;
			}
		}
		if (player.getInteractingItem().getId() == 18781) {
			if (checkRequirement(player, skillId, levelReq[3]) && player.getInventory().contains(18781)) {
				player.getInventory().delete(18781, 1);
				player.getInventory().add(18782, 1);
				player.getSkillManager().addExactExperience(Skill.forId(skillId), 60000);
				player.getClickDelay().reset();
				player.performAnimation(new Animation(7368));
				player.getPacketSender().sendInterfaceRemoval();
				player.setEffigy(0);
				player.setInteractingItem(null);
				return;
			}
		}
	}

	public static boolean isEffigy(int item) {
		return item >= 18778 && item <= 18781;
	}
}
