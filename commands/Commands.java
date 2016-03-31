package com.ikov.commands;

import com.ikov.model.PlayerRights;
import com.ikov.world.content.BankPin;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.commands.ranks.*;
import com.ikov.util.Logs;
import com.ikov.world.content.PlayerLogs;

/**
 * Initiates a command for each rank/file.
 * 
 * @author Jonathan Sirens
 */

public class Commands {

	public static void initiate_commands(Player player, String[] parts, String whole_command) {
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" has done command "+whole_command+"");
		
		//Regular Commands
		Members.initiate_command(player, parts, whole_command);
		SpecialPlayers.initiate_command(player, parts, whole_command);
		
		//Staff Members
		if(player.getRights() == PlayerRights.OWNER) {
			Owners.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			Administrators.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.MODERATOR) {
			Moderators.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.GLOBAL_MOD) {
			GlobalModerators.initiate_command(player, parts, whole_command);
		}	
		if(player.getRights() == PlayerRights.SUPPORT) {
			Supports.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.COMMUNITY_MANAGER) {
			CommunityManagers.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.WIKI_EDITOR) {
			WikiEditors.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.WIKI_MANAGER) {
			WikiManagers.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.STAFF_MANAGER) {
			StaffManagers.initiate_command(player, parts, whole_command);
		}
		
		//Donator Ranks
		if(player.getDonorRights() == 1) {
			RegularDonators.initiate_command(player, parts, whole_command);
		}	
		if(player.getDonorRights() == 2) {
			SuperDonators.initiate_command(player, parts, whole_command);
		}
		if(player.getDonorRights() == 3) {
			ExtremeDonators.initiate_command(player, parts, whole_command);
		}
		if(player.getDonorRights() == 4) {
			LegendaryDonators.initiate_command(player, parts, whole_command);
		}
		if(player.getDonorRights() == 5) {
			UberDonators.initiate_command(player, parts, whole_command);
		}
	}
}

