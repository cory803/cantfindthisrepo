package com.runelive.world.content.skill.impl.prayer;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.Item;
import com.runelive.model.Skill;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.Sounds;
import com.runelive.world.content.Sounds.Sound;
import com.runelive.world.entity.impl.player.Player;

/**
 * The prayer skill is based upon burying the corpses of enemies. Obtaining a
 * higher level means more prayer abilities being unlocked, which help out in
 * combat.
 * 
 * @author Gabriel Hannason
 */

public class Prayer {

	public static boolean isBone(int bone) {
		return BonesData.forId(bone) != null;
	}

	public static void buryBone(final Player player, final int itemId) {
		if (!player.getClickDelay().elapsed(2000))
			return;
		final BonesData currentBone = BonesData.forId(itemId);
		if (currentBone == null)
			return;
		if(ItemDefinition.forId(itemId).getName().contains("ashes")) {
			player.getSkillManager().stopSkilling();
			player.getPacketSender().sendInterfaceRemoval();
			player.performAnimation(new Animation(2292));
			/*switch(BonesData.forId(itemId)) {
				case IMPIOUS_ASHES:
					player.performGraphic(new Graphic(56));
					break;
				case ACCURSED_ASHES:
					player.performGraphic(new Graphic(47));
					break;
				case INFERNAL_ASHES:
					player.performGraphic(new Graphic(40));
					break;
			}*/
			player.getPacketSender().sendMessage("You scatter the ashes...");
			final Item bone = new Item(itemId);
			player.getInventory().delete(bone);
			return;
		}
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You dig a hole in the ground..");
		final Item bone = new Item(itemId);
		player.getInventory().delete(bone);
		TaskManager.submit(new Task(3, player, false) {
			@Override
			public void execute() {
				player.getPacketSender().sendMessage("..and bury the " + bone.getDefinition().getName() + ".");
				player.getSkillManager().addExperience(Skill.PRAYER, currentBone.getBuryingXP());
				Sounds.sendSound(player, Sound.BURY_BONE);
				if (currentBone == BonesData.BIG_BONES)
					Achievements.finishAchievement(player, AchievementData.BURY_A_BIG_BONE);
				if (currentBone == BonesData.DRAGON_BONES)
					Achievements.finishAchievement(player, AchievementData.BURY_A_DRAGON_BONE);
				else if (currentBone == BonesData.FROSTDRAGON_BONES) {
					Achievements.doProgress(player, AchievementData.BURY_25_FROST_DRAGON_BONES);
					Achievements.doProgress(player, AchievementData.BURY_500_FROST_DRAGON_BONES);
				}
				stop();
			}
		});
		player.getClickDelay().reset();
	}
}
