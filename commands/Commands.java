package com.ikov.commands;

import com.ikov.model.PlayerRights;
import com.ikov.world.content.BankPin;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.commands.ranks.*;
import com.ikov.util.Logs;

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
		if(player.getRights().isStaff()) {
			Logs.write_data(player.getUsername()+ ".txt", "staff_commands", "["+player.getUsername()+"]: ::"+whole_command+"");
		} else {
			Logs.write_data(player.getUsername()+ ".txt", "player_commands", "["+player.getUsername()+"]: ::"+whole_command+"");			
		}
		Members.initiate_command(player, parts, whole_command);
		SpecialPlayers.initiate_command(player, parts, whole_command);
		if(player.getRights() == PlayerRights.OWNER) {
			Owners.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			Administrators.initiate_command(player, parts, whole_command);
		}
		if(player.getRights() == PlayerRights.MODERATOR) {
			Moderators.initiate_command(player, parts, whole_command);
		}	
		if(player.getRights() == PlayerRights.SUPPORT) {
			Supports.initiate_command(player, parts, whole_command);
		}
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

