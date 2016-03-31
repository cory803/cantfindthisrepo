package com.ikov.world.content.skill;

import com.ikov.model.Skill;
import com.ikov.model.actions.Action;
import com.ikov.model.actions.ActionHandler;
import com.ikov.world.entity.impl.player.Player;

/**
 * Handles enchanting of interfaces calculations such as combat level and total
 * level.
 * 
 * @author Jonathan Sirens
 * @author High105
 */

public class Enchanting {
	
	enum Data {
		OPAL_REQ(879, 9236, 564, 1, 556, 20),
		SAPPHIRE_REQ(9337, 9240, 564, 2, 555, 4),
		JADE_REQ(9335, 9237, 564, 3, 557, 6),
		PEARL_REQ(880, 9238, 564, 4, 555, 8),
		EMERALD_REQ(9338, 9241, 564, 5, 561, 5),
		RED_TOPAZ_REQ(9336, 9239, 564, 6, 554, 12),
		RUBY_REQ(9339, 9242, 565, 5, 9075, 50),
		DIAMOND_REQ(9340, 9243, 563, 5, 9075, 75),
		DRAGONSTONE_REQ(9341, 9244, 566, 5, 9075, 100),
		ONYX_REQ(9342, 9245, 560, 10, 9075, 115),
		
		OPAL(-16530, 4, 49009),
		SAPPHIRE(-16521, 7, 49017),
		JADE(-16513, 14, 49025),
		PEARL(-16505, 24, 49033),
		EMERALD(-16497, 27, 49041),
		RED_TOPAZ(-16489, 29, 49049),
		RUBY(-16481, 49, 49057),
		DIAMOND(-16473, 57, 49065),
		DRAGONSTONE(-16465, 68, 49073),
		ONYX(-16457, 87, 49081);
		
		private int buttonId, levelRequired, stringId;
		private int boltId, enchantId, firstRune, firstRuneAmount, secondRune, secondRuneAmount;
		
		Data(int boltId, int enchantId, int firstRune, int firstRuneAmount, int secondRune, int secondRuneAmount) {
			this.boltId = boltId;
			this.enchantId = enchantId;
			this.firstRune = firstRune;
			this.firstRuneAmount = firstRuneAmount;
			this.secondRune = secondRune;
			this.secondRuneAmount = secondRuneAmount;
		}
		Data(int buttonId, int levelRequired, int stringId) {
			this.buttonId = buttonId;
			this.levelRequired = levelRequired;
			this.stringId = stringId;
		}

		public int getBoltId() {
			return boltId;
		}
		
		public int getEnchantId() {
			return enchantId;
		}
		
		public int getFirstRune() {
			return firstRune;
		}
		
		public int getFirstRuneAmount() {
			return firstRuneAmount;
		}
		
		public int getSecondRune() {
			return secondRune;
		}
		
		public int getSecondRuneAmount() {
			return secondRuneAmount;
		}
		
		public int getButtonId() {
			return buttonId;
		}
		
		public int getLevelRequired() {
			return levelRequired;
		}
		
		public int getStringId() {
			return stringId;
		}
	}
	
	public static void enchantButtons(Player player, int buttonId) {
		int magicLevel = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		for (Data d : Data.values()) {
			if (d.getButtonId() == buttonId) {
				if (magicLevel >= d.getLevelRequired()) {
					//Proceed with enchanting..
				} else {
					player.getPacketSender().sendMessage("You need a magic level of " + d.getLevelRequired() + " to enchant these bolts.");
				}
			}
		}
	}
	public static void update_interface(Player player) {
		player.getPacketSender().sendInterface(49000);
		int magic_level = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		for (Data d : Data.values()) {
			if (magic_level < d.getLevelRequired()) {
				player.getPacketSender().sendString(d.getStringId(), "@red@Magic " + d.getLevelRequired());
			} else {
				player.getPacketSender().sendString(d.getStringId(), "@gre@Magic " + d.getLevelRequired());
			}
		}
	}
	
	static {
		for (Data d : Data.values()) {
			ActionHandler.getActionHandler().submit(d.getButtonId(), new Action() {

				@Override
				public void handle(Player player) {
					enchantButtons(player, d.getButtonId());
				}
				
			});
		}
	}

} 