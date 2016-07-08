package com.runelive.commands;

import com.runelive.commands.ranks.Administrators;
import com.runelive.commands.ranks.Developers;
import com.runelive.commands.ranks.ExtremeDonators;
import com.runelive.commands.ranks.GlobalModerators;
import com.runelive.commands.ranks.LegendaryDonators;
import com.runelive.commands.ranks.Managers;
import com.runelive.commands.ranks.Members;
import com.runelive.commands.ranks.Moderators;
import com.runelive.commands.ranks.Owners;
import com.runelive.commands.ranks.RegularDonators;
import com.runelive.commands.ranks.SpecialPlayers;
import com.runelive.commands.ranks.StaffManagers;
import com.runelive.commands.ranks.SuperDonators;
import com.runelive.commands.ranks.Supports;
import com.runelive.commands.ranks.UberDonators;
import com.runelive.commands.ranks.WikiEditors;
import com.runelive.commands.ranks.WikiManagers;
import com.runelive.commands.ranks.YouTubers;
import com.runelive.model.PlayerRights;
import com.runelive.world.content.BankPin;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.entity.impl.player.Player;

/**
 * Initiates a command for each rank/file.
 * 
 * @author Jonathan Sirens
 */

public class Commands {

	public static void initiate_commands(Player player, String[] parts, String whole_command) {
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		PlayerLogs.commands(player, whole_command);

		// Regular Commands
		Members.initiate_command(player, parts, whole_command);
		SpecialPlayers.initiate_command(player, parts, whole_command);

		if (player.getUsername().equalsIgnoreCase("vados")) {
			Developers.initiate_command(player, parts, whole_command);
		}

		// Staff Members
		if (player.getRights() == PlayerRights.OWNER) {
			Owners.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.ADMINISTRATOR) {
			Administrators.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.MODERATOR) {
			Moderators.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.GLOBAL_MOD) {
			GlobalModerators.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.SUPPORT) {
			Supports.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.MANAGER) {
			Managers.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.WIKI_EDITOR) {
			WikiEditors.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.WIKI_MANAGER) {
			WikiManagers.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.STAFF_MANAGER) {
			StaffManagers.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.YOUTUBER) {
			YouTubers.initiate_command(player, parts, whole_command);
			return;
		}

		// Donator Ranks
		if (player.getDonorRights() == 1) {
			RegularDonators.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 2) {
			SuperDonators.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 3) {
			ExtremeDonators.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 4) {
			LegendaryDonators.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 5) {
			UberDonators.initiate_command(player, parts, whole_command);
		}
	}
}
