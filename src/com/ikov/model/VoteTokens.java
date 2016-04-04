package com.ikov.model;

import com.ikov.model.definitions.ItemDefinition;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPanel;
import com.ikov.GameSettings;

public class VoteTokens  {
	
	/**
	* Ikov Vote Tokens
	* @Author Jonathan Sirens
	**/
	
	public static int super_rare_chance = 3000;
	
	public static String global_color = "2EA73D";
	
	public static String global_shad = "0";
	
	public static String website = "::vote";
	
	public static int[] SUPER_RARE_ITEMS = {10330, 10332, 10334, 10336, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352, 14014, 14015, 14016, 14008, 14009, 14010, 14011, 14012, 14013, 21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 1037, 1038, 1040, 1042, 1044, 1046, 1048, 1050, 1053, 1055, 1057, 21024, 21025, 21026, 21035, 21036, 21037, 21038, 21039, 21040, 21041, 21027, 21028, 21029, 21030, 21031, 21032, 21033, 21034, 4084};
	
	public static int[] RARE_ITEMS = {21016, 21017, 21018, 21019, 21372, 21020, 21021, 21022, 21023, 5607, 20000, 20001, 20002, 9177, 1191, 5680, 15069, 15071, 19780, 6570, 19669, 11694, 14484, 11724, 11726, 13887, 13893, 13899, 13905, 15220, 15017, 11696, 6199, 11698, 11700, 18349, 18351, 18353, 18355, 18357, 13738, 13740, 13742, 13744, 15825, 17273};
	
	public static int[] COMMON_ITEMS = {1704, 1201, 10828, 3105, 21045, 21044, 21046, 9185, 4587, 4153, 19111, 4151, 6585, 11732, 15018, 15019, 15020, 6920, 15486, 6889, 11235, 6733, 6735, 6737, 6731, 6914, 1052, 18335, 4716, 4718, 4720, 4722, 4708, 4710, 4712, 4714, 4724, 4726, 4728, 4730, 4745, 4747, 4749, 4751, 4732, 4734, 4736, 4738, 4753, 4755, 4757, 4759};

	public static int random_item(int collection) {
		if(collection == 0) {
			return COMMON_ITEMS[(int) (Math.random() * COMMON_ITEMS.length)];
		} else if(collection == 1) {
			return RARE_ITEMS[(int) (Math.random() * RARE_ITEMS.length)];
		} else if(collection == 2) {
			return SUPER_RARE_ITEMS[(int) (Math.random() * SUPER_RARE_ITEMS.length)];
		}
		return 0;
	}
	
	public static void open_token(Player p, int itemId) {
		if(p.getGameMode() == GameMode.IRONMAN || p.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			int points = 1;
			if(p.getDonorRights() == 1) {
				points += 1;
			} else if(p.getDonorRights() == 2) {
				points += 1;
			} else if(p.getDonorRights() == 3) {
				points += 2;
			} else if(p.getDonorRights() == 4) {
				points += 2;
			} else if(p.getDonorRights() == 5) {
				points += 3;
			}
			if(GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			p.getPointsHandler().incrementVotingPoints(points);
			p.getPacketSender().sendMessage("<img=4><col=2F5AB7>You have received and "+points+" vote points.");
			PlayerPanel.refreshPanel(p);
			p.getInventory().delete(10944, 1);
			return;
		}
		int collection = 0;
		int random_chance = Misc.getRandom(super_rare_chance);
		if(random_chance == super_rare_chance - 1) {
			collection = 2;
		} else if(random_chance >= 0 && random_chance <= 12) {
			collection = 1;
		} else {
			collection = 0;
		}
		int item_id = random_item(collection);
		String name = p.getUsername();
		p.getInventory().delete(10944, 1);
		p.getInventory().add(item_id, 1);
//		int yell_chance = Misc.getRandom(5);
		String item_name = ItemDefinition.forId(item_id).name;
		int points = 1;
		if(p.getDonorRights() == 1) {
			points += 1;
		} else if(p.getDonorRights() == 2) {
			points += 1;
		} else if(p.getDonorRights() == 3) {
			points += 2;
		} else if(p.getDonorRights() == 4) {
			points += 2;
		} else if(p.getDonorRights() == 5) {
			points += 3;
		}
		if(GameSettings.DOUBLE_POINTS) {
			points *= 2;
		}
		if(collection == 2) {
			World.sendMessage("<img=2><col=2F5AB7>The player <shad=0>"+name+"</shad> has received <col=ff0000>"+item_name+" <col=2F5AB7>from ::vote!");
		} else if(collection == 1) {
			World.sendMessage("<img=1><col=2F5AB7>The player <shad=0>"+name+"</shad> has received <col=ff0000>"+item_name+" <col=2F5AB7>from ::vote!");
		}
		p.getPacketSender().sendMessage("<img=4><col=2F5AB7>You have recieved ("+item_name+"), and "+points+" vote points.");
		p.getPointsHandler().incrementVotingPoints(points);
		PlayerPanel.refreshPanel(p);
	}
}