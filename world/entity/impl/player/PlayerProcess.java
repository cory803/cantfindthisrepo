package com.ikov.world.entity.impl.player;

import com.ikov.model.RegionInstance.RegionInstanceType;
import com.ikov.util.Misc;
import com.ikov.world.content.LoyaltyProgramme;
import com.ikov.world.content.combat.pvp.BountyHunter;
import com.ikov.world.content.skill.impl.construction.House;
import com.ikov.world.entity.impl.GroundItemManager;
import com.ikov.world.content.Consumables;
import com.ikov.model.Locations.Location;
import com.ikov.model.Skill;
import com.ikov.commands.ranks.SpecialPlayers;

public class PlayerProcess {

	/*
	 * The player (owner) of this instance
	 */
	private Player player;

	/*
	 * The loyalty tick, once this reaches 6, the player
	 * will be given loyalty points.
	 * 6 equals 3.6 seconds.
	 */
	private int loyaltyTick;

	/*
	 * The timer tick, once this reaches 2, the player's
	 * total play time will be updated.
	 * 2 equals 1.2 seconds.
	 */
	private int timerTick;

	/*
	 * Makes sure ground items are spawned on height change
	 */
	private int previousHeight;

	public PlayerProcess(Player player) {
		this.player = player;
		this.previousHeight = player.getPosition().getZ();
	}

	public void sequence() {

		/** SKILLS **/
		if(player.shouldProcessFarming()) {
			player.getFarming().sequence();
		}
		if(player.getZulrahTime() > 0) {
			player.decremenetZulrahTimer();
		}

		if(player.getLocation() == Location.WILDERNESS) {
			boolean continue_method = true;
			for(int i = 0; i < SpecialPlayers.player_names.length; i++) {
				if(SpecialPlayers.player_names[i].toLowerCase().equals(player.getUsername().toLowerCase())) {
					continue_method = false;
				}
			}
			if(continue_method) {
				if(player.getSkillManager().getMaxLevel(Skill.ATTACK) > 118)  {
					player.getSkillManager().setMaxLevel(Skill.ATTACK, 118);
				}
				if(player.getSkillManager().getMaxLevel(Skill.STRENGTH) > 118) {
					player.getSkillManager().setMaxLevel(Skill.STRENGTH, 118);
				}
				if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) > 118) {
					player.getSkillManager().setMaxLevel(Skill.DEFENCE, 118);
				}
			}
		}
		
		/** MISC **/

		if(previousHeight != player.getPosition().getZ()) {
			GroundItemManager.handleRegionChange(player);
			previousHeight = player.getPosition().getZ();
		}

		if(!player.isInActive()) {
			if(loyaltyTick >= 6) {
				LoyaltyProgramme.incrementPoints(player);
				loyaltyTick = 0;
			}
			loyaltyTick++;
		}
		
		if(timerTick >= 1) {
			player.getPacketSender().sendString(39166, "@or2@Time played:  @yel@"+Misc.getTimePlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())));
			timerTick = 0;
		}
		timerTick++;
		
		BountyHunter.sequence(player);
		
		if(player.getRegionInstance() != null && (player.getRegionInstance().getType() == RegionInstanceType.CONSTRUCTION_HOUSE || player.getRegionInstance().getType() == RegionInstanceType.CONSTRUCTION_DUNGEON)) {
			((House)player.getRegionInstance()).process();
		}
	}
}
