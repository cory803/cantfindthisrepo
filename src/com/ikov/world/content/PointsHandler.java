package com.ikov.world.content;

import com.ikov.world.entity.impl.player.Player;
import com.ikov.GameSettings;

public class PointsHandler {

	private Player p;
	
	public PointsHandler(Player p) {
		this.p = p;
	}
	
	public void reset() {
		dungTokens = commendations = (int) (loyaltyPoints = votingPoints = slayerPoints = pkPoints = 0);
		p.getPlayerKillingAttributes().setPlayerKillStreak(0);
		p.getPlayerKillingAttributes().setPlayerKills(0);
		p.getPlayerKillingAttributes().setPlayerDeaths(0);
		p.getDueling().arenaStats[0] = p.getDueling().arenaStats[1] = 0;
	}

	public PointsHandler refreshPanel() {
		p.getPacketSender().sendString(55079, "@red@Prestige Points: @gre@"+prestigePoints);
		p.getPacketSender().sendString(55080, "@red@Commendations: @gre@ "+commendations);
		p.getPacketSender().sendString(55081, "@red@Loyalty Points: @gre@"+(int)loyaltyPoints);
		p.getPacketSender().sendString(55082, "@red@Dung. Tokens: @gre@ "+dungTokens);
		p.getPacketSender().sendString(55083, "@red@Voting Points: @gre@ "+votingPoints);
		p.getPacketSender().sendString(55084, "@red@Slayer Points: @gre@"+slayerPoints);
		p.getPacketSender().sendString(55085, "@red@Pk Points: @gre@"+pkPoints);
		p.getPacketSender().sendString(55086, "@red@Wilderness Killstreak: @gre@"+p.getPlayerKillingAttributes().getPlayerKillStreak());
		p.getPacketSender().sendString(55087, "@red@Wilderness Kills: @gre@"+p.getPlayerKillingAttributes().getPlayerKills());		
		p.getPacketSender().sendString(55088, "@red@Wilderness Deaths: @gre@"+p.getPlayerKillingAttributes().getPlayerDeaths());
		p.getPacketSender().sendString(55089, "@red@Arena Victories: @gre@"+p.getDueling().arenaStats[0]);
		p.getPacketSender().sendString(55090, "@red@Arena Losses: @gre@"+p.getDueling().arenaStats[1]);
		
		return this;
	}

	private int prestigePoints;
	private int slayerPoints;
	private int commendations;
	private int dungTokens;
	private int pkPoints;
	private double loyaltyPoints;
	private int votingPoints;
	private int achievementPoints;
	
	public int getPrestigePoints() {
		return prestigePoints;
	}
	
	public void setPrestigePoints(int points, boolean add) {
		if(add) {
			if(GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.prestigePoints += points;
		} else {
			this.prestigePoints = points;
		}
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public void setSlayerPoints(int slayerPoints, boolean add) {
		if(add) {
			if(GameSettings.DOUBLE_POINTS) {
				slayerPoints *= 2;
			}
			this.slayerPoints += slayerPoints;
		} else {
			this.slayerPoints = slayerPoints;
		}
	}

	public int getCommendations() {
		return this.commendations;
	}

	public void setCommendations(int commendations, boolean add) {
		if(add) {
			if(GameSettings.DOUBLE_POINTS) {
				commendations *= 2;
			}
			this.commendations += commendations;
		} else {
			this.commendations = commendations;
		}
	}

	public int getLoyaltyPoints() {
		return (int)this.loyaltyPoints;
	}

	public void setLoyaltyPoints(int points, boolean add) {
		if(add) {
			if(GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.loyaltyPoints += points;
		} else {
			this.loyaltyPoints = points;
		}
	}
	
	public void incrementLoyaltyPoints(double amount) {
		if(GameSettings.DOUBLE_POINTS) {
			amount *= 2;
		}
		this.loyaltyPoints += amount;
	}
	
	public int getPkPoints() {
		return this.pkPoints;
	}

	public void setPkPoints(int points, boolean add) {
		if(add) {
			if(GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.pkPoints += points;
		} else {
			this.pkPoints = points;
		}
	}
	
	public int getDungeoneeringTokens() {
		return dungTokens;
	}

	public void setDungeoneeringTokens(int dungTokens, boolean add) {
		if(add)
			this.dungTokens += dungTokens;
		else
			this.dungTokens = dungTokens;
	}
	
	public int getVotingPoints() {
		return votingPoints;
	}
	
	public void setVotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}
	
	public void incrementVotingPoints() {
		this.votingPoints++;
	}
	
	public void incrementVotingPoints(int amt) {
		this.votingPoints += amt;
	}
	
	public void setVotingPoints(int points, boolean add) {
		if(add)
			this.votingPoints += points;
		else
			this.votingPoints = points;
	}
	
	public int getAchievementPoints() {
		return achievementPoints;
	}
	
	public void setAchievementPoints(int points, boolean add) {
		if(add) {
			if(GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.achievementPoints += points;
		} else {
			this.achievementPoints = points;
		}
	}
}
