package com.runelive.commands;

import com.runelive.commands.ranks.*;
import com.runelive.model.PlayerRights;
import com.runelive.world.content.BankPin;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.entity.impl.player.Player;
import org.scripts.kotlin.content.commands.staff.AdministratorCommands;
import org.scripts.kotlin.content.commands.staff.ModeratorCommands;
import org.scripts.kotlin.content.commands.staff.ServerSupports;
import org.scripts.kotlin.content.commands.player.*;

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
		PlayerCmds.initiate_command(player, parts, whole_command);

		// Staff Members & Developers
		if (player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.DEVELOPER) {
			AdministratorCommands.Owner.initiate_command(player, parts, whole_command);
			AdministratorCommands.Managers.initiate_command(player, parts, whole_command);
			AdministratorCommands.Administrators.initiate_command(player, parts, whole_command);
			ModeratorCommands.GlobalModerators.initiate_command(player, parts, whole_command);
			ModeratorCommands.Moderators.initiate_command(player, parts, whole_command);
			ServerSupports.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.MANAGER || player.getRights() == PlayerRights.STAFF_MANAGER) {
			AdministratorCommands.Managers.initiate_command(player, parts, whole_command);
			AdministratorCommands.Administrators.initiate_command(player, parts, whole_command);
			ModeratorCommands.GlobalModerators.initiate_command(player, parts, whole_command);
			ModeratorCommands.Moderators.initiate_command(player, parts, whole_command);
			ServerSupports.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.ADMINISTRATOR) {
			AdministratorCommands.Administrators.initiate_command(player, parts, whole_command);
			ModeratorCommands.GlobalModerators.initiate_command(player, parts, whole_command);
			ModeratorCommands.Moderators.initiate_command(player, parts, whole_command);
			ServerSupports.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.MODERATOR) {
			ModeratorCommands.Moderators.initiate_command(player, parts, whole_command);
			ServerSupports.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.GLOBAL_MOD) {
			ModeratorCommands.GlobalModerators.initiate_command(player, parts, whole_command);
			ModeratorCommands.Moderators.initiate_command(player, parts, whole_command);
			ServerSupports.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.SUPPORT) {
			ServerSupports.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.WIKI_EDITOR) {
			Wiki.WikiEditors.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.WIKI_MANAGER) {
			Wiki.WikiEditors.initiate_command(player, parts, whole_command);
			Wiki.WikiManagers.initiate_command(player, parts, whole_command);
		}
		if (player.getRights() == PlayerRights.YOUTUBER) {
			PlayerCmds.initiate_command(player, parts, whole_command);
			YouTubers.initiate_command(player, parts, whole_command);
			Donator.RegularDonator.initiate_command(player, parts, whole_command);
			Donator.SuperDonator.initiate_command(player, parts, whole_command);
			Donator.ExtremeDonator.initiate_command(player, parts, whole_command);
			Donator.LegendaryDonator.initiate_command(player, parts, whole_command);
			Donator.UberDonator.initiate_command(player, parts, whole_command);
			return;
		}

		// Donator Ranks
		if (player.getDonorRights() == 1) {
			Donator.RegularDonator.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 2) {
			Donator.RegularDonator.initiate_command(player, parts, whole_command);
			Donator.SuperDonator.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 3) {
			Donator.RegularDonator.initiate_command(player, parts, whole_command);
			Donator.SuperDonator.initiate_command(player, parts, whole_command);
			Donator.ExtremeDonator.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 4) {
			Donator.RegularDonator.initiate_command(player, parts, whole_command);
			Donator.SuperDonator.initiate_command(player, parts, whole_command);
			Donator.ExtremeDonator.initiate_command(player, parts, whole_command);
			Donator.LegendaryDonator.initiate_command(player, parts, whole_command);
		}
		if (player.getDonorRights() == 5) {
			Donator.RegularDonator.initiate_command(player, parts, whole_command);
			Donator.SuperDonator.initiate_command(player, parts, whole_command);
			Donator.ExtremeDonator.initiate_command(player, parts, whole_command);
			Donator.LegendaryDonator.initiate_command(player, parts, whole_command);
			Donator.UberDonator.initiate_command(player, parts, whole_command);
		}
	}
}
