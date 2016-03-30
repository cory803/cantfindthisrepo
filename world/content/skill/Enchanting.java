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
		
		Data(int buttonId, int levelRequired, int stringId) {
			this.buttonId = buttonId;
			this.levelRequired = levelRequired;
			this.stringId = stringId;
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