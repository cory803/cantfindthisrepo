package com.ikov.model;

import java.sql.PreparedStatement;

import com.ikov.world.entity.impl.player.Player;
import com.ikov.model.Skill;
 
public class Highscores implements Runnable {

	private Player player;
	
	public Highscores(Player player) {
		this.player = player;
	}
	
	@Override
	public void run() {
		try {
			Database db = new Database("ikov2.org", "spawnsca_ihs", "*?N7k*T{vrR5", "spawnsca_ihs");

			String name = player.getUsername();
			
			if (!db.init()) {
				System.err.println("Failing to update "+name+" highscores. Database could not connect.");
				return;
			}
			
			PreparedStatement stmt1 = db.prepare("DELETE FROM hs_users WHERE username=?");
			stmt1.setString(1, name);
			stmt1.execute();
				
			PreparedStatement stmt2 = db.prepare(generateQuery());
			stmt2.setString(1, name);
			if(player.getRights().isStaff()) {
				stmt2.setInt(2, player.getRights().ordinal());
			} else {
				stmt2.setInt(2, player.getDonorRights() + 20);
			}
			stmt2.setLong(3, player.getSkillManager().getTotalExp());
			
			for (int i = 0; i < 25; i++)
				stmt2.setInt(4 + i, (int)player.getSkillManager().getExperience(Skill.forId(i)));
			stmt2.execute();
			
			db.destroyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO hs_users (");
		sb.append("username, ");
		sb.append("rights, ");
		sb.append("overall_xp, ");
		sb.append("attack_xp, ");
		sb.append("defence_xp, ");
		sb.append("strength_xp, ");
		sb.append("constitution_xp, ");
		sb.append("ranged_xp, ");
		sb.append("prayer_xp, ");
		sb.append("magic_xp, ");
		sb.append("cooking_xp, ");
		sb.append("woodcutting_xp, ");
		sb.append("fletching_xp, ");
		sb.append("fishing_xp, ");
		sb.append("firemaking_xp, ");
		sb.append("crafting_xp, ");
		sb.append("smithing_xp, ");
		sb.append("mining_xp, ");
		sb.append("herblore_xp, ");
		sb.append("agility_xp, ");
		sb.append("thieving_xp, ");
		sb.append("slayer_xp, ");
		sb.append("farming_xp, ");
		sb.append("runecrafting_xp, ");
		sb.append("hunter_xp, ");
		sb.append("construction_xp, ");
		sb.append("summoning_xp, ");
		sb.append("dungeoneering_xp) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		return sb.toString();
	}
	
}