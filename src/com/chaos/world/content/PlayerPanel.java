package com.chaos.world.content;

import com.chaos.util.Misc;
import com.chaos.util.Stopwatch;
import com.chaos.world.World;
import com.chaos.world.content.Well.WellOfGoodness;
import com.chaos.world.content.skill.impl.slayer.SlayerTasks;
import com.chaos.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		int kdr = 0;
		if (player.getPlayerKillingAttributes().getPlayerDeaths() > 0) {
			kdr = player.getPlayerKillingAttributes().getPlayerKills() / player.getPlayerKillingAttributes().getPlayerDeaths();
		} else {
		    kdr = player.getPlayerKillingAttributes().getPlayerKills();
        }
		player.getPacketSender().sendString(55072, "  @whi@[Population]");
		player.getPacketSender().sendString(55073, "   >- Players online: @cha@"+ World.getPlayers().size());
		player.getPacketSender().sendString(55074, "   >- Staff online: @cha@"+ World.staffOnline());
		player.getPacketSender().sendString(55075, "  @whi@[Personal]");
		player.getPacketSender().sendString(55076, "   >- Title: @cha@None");
//		player.getPacketSender().sendString(55077, "   >- Play Time: @cha@" + player.getTotalPlayTime());
		player.getPacketSender().sendString(55078, "   >- Game mode: @cha@ " + player.getGameModeAssistant().getModeName());
		player.getPacketSender().sendString(55079, "   >- Rank: @cha@ " + player.getStaffRights().getTitle());
		player.getPacketSender().sendString(55080, "   >- Donator: @cha@ " + player.getDonatorRights().getTitle());
		player.getPacketSender().sendString(55081, "  @whi@[Points]");
		player.getPacketSender().sendString(55082, "   >- Pk points: @cha@ " + player.getPointsHandler().getPkPoints());
		player.getPacketSender().sendString(55083, "   >- Vote points: @cha@ " + player.getPointsHandler().getVotingPoints());
		player.getPacketSender().sendString(55084, "   >- Slayer points: @cha@ " + player.getPointsHandler().getSlayerPoints());
		player.getPacketSender().sendString(55085, "   >- Commedations: @cha@ " + player.getPointsHandler().getCommendations());
		player.getPacketSender().sendString(55086, "   >- Dung tokens: @cha@ " + player.getPointsHandler().getDungeoneeringTokens());
		player.getPacketSender().sendString(55087, "  @whi@[PvP]");
		player.getPacketSender().sendString(55088, "   >- KDR: @cha@ " + kdr);
		player.getPacketSender().sendString(55089, "   >- Kills: @cha@ " + player.getPlayerKillingAttributes().getPlayerKills());
		player.getPacketSender().sendString(55090, "   >- Deaths: @cha@ " + player.getPlayerKillingAttributes().getPlayerDeaths());
		player.getPacketSender().sendString(55091, "   >- Killstreak: @cha@ " + player.getPlayerKillingAttributes().getPlayerKillStreak());
		player.getPacketSender().sendString(55092, "   >- Duel Victories: @cha@ " + player.getDueling().arenaStats[0]);
		player.getPacketSender().sendString(55093, "   >- Duel Losses: @cha@ " + player.getDueling().arenaStats[1]);
		player.getPacketSender().sendString(55094, "  @whi@[Slayer]");
		player.getPacketSender().sendString(55095, "   >- Slayer Master: @cha@" + Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));
		if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
			player.getPacketSender().sendString(55096, "   >- Task: @cha@" + Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " ")));
		} else {
			player.getPacketSender().sendString(55096, "   >- Task: @cha@" + Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " ")) + "s");
		}
		player.getPacketSender().sendString(55097, "   >- Task Amount: @cha@" + player.getSlayer().getAmountToSlay());
		player.getPacketSender().sendString(55098, "   >- Task Streak: @cha@" + player.getSlayer().getTaskStreak());
		if (player.getSlayer().getDuoPartner() != null) {
			player.getPacketSender().sendString(55099, "   >- Duo Partner: @cha@" + player.getSlayer().getDuoPartner() + "");
		} else {
			player.getPacketSender().sendString(55099, "   >- Duo Partner: @cha@: None");
		}
		player.getPacketSender().sendString(55100, "  @whi@[Wells]");

		if (WellOfGoodness.isActive("exp")) {
		} else {
		}
		if (WellOfGoodness.isActive("drops")) {
		} else {
		}
		if (WellOfGoodness.isActive("pkp")) {
		} else {
		}
			player.getPacketSender().sendString(i, " ");
		}
	}

}
