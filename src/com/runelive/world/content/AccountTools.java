package com.runelive.world.content;

import java.io.File;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.commands.ranks.SpecialPlayers;
import com.runelive.net.mysql.SQLCallback;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerLoading;
import com.runelive.world.entity.impl.player.PlayerSaving;

public class AccountTools {
	
    public static Player scan(Player staff, String victimUsername, Player victim) {
        GameServer.getCharacterPool().executeQuery("SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    String json = rs.getString("json");
                    PlayerLoading.decodeJson(victim, json);
                    String serial = String.valueOf(victim.getLastMacAddress());
                    if (serial != null && !serial.equals("not_set")) {
                    	outScan(staff, victimUsername, serial, victim);
                    }
                }
            }

            @Override
            public void queryError(SQLException e) {
                e.printStackTrace();
            }
        });
        return victim;
    }
    
    public static Player outScan(Player staff, String victimUsername, String serial, Player victim) {
   	 	staff.getPacketSender().sendMessage("Grabbing all accounts "+victimUsername+" has logged into...");
    	GameServer.getCharacterPool().executeQuery("SELECT * FROM `connections` WHERE `mac address` LIKE '"+serial+"'", new SQLCallback() {
             @Override
             public void queryComplete(ResultSet rs) throws SQLException {
            	 ArrayList<String> USERS = new ArrayList<String>();
            	 ArrayList<String> TIMES = new ArrayList<String>();
                 while(rs.next()) {
                     String username = rs.getString("username");
                     String time = rs.getString("time");
                     if(!USERS.contains(username)) {
                    	 boolean continueMethod = true;
                    	 for (int i = 0; i < SpecialPlayers.player_names.length; i++) {
	                    	 if(SpecialPlayers.player_names[i].equalsIgnoreCase(victimUsername) && !username.equalsIgnoreCase(victimUsername)) {
	                    		 continueMethod = false;
	                    	 }
	                    	 if(SpecialPlayers.player_names[i].equalsIgnoreCase(username) && !username.equalsIgnoreCase(victimUsername)) {
	                    		 continueMethod = false;
	                    	 }
                    	 }
                    	 if(continueMethod) { 
                    		 USERS.add(username);
	                    	 TIMES.add(time);
                    	 }
                     }
                 }
                 for (int i = 0; i < USERS.size(); i++) {
                	 staff.getPacketSender().sendMessage("Player "+victimUsername+" has logged into: <col=ff0000>"+USERS.get(i)+"  -  "+TIMES.get(i)+"");
                 }
             }

             @Override
             public void queryError(SQLException e) {
                 e.printStackTrace();
             }
         });
    	 return victim;
    }

    public static Player checkPassword(Player staff, String victimUsername, Player victim) {
        GameServer.getCharacterPool().executeQuery("SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    String json = rs.getString("json");
                    PlayerLoading.decodeJson(victim, json);
                    String password = victim.getPassword();
                    if (victim.getPassword() != null) {
                    	staff.getPacketSender().sendMessage("The player "+victimUsername+"'s password is: " + password + "");
                    }
                }
            }

            @Override
            public void queryError(SQLException e) {
                e.printStackTrace();
            }
        });
        return victim;
    }

    public static Player setPassword(Player staff, String victimUsername, String newPassword, Player victim) {
        GameServer.getCharacterPool().executeQuery("SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                if (rs.next()) {
                	Player other = World.getPlayerByName(victimUsername);
                	if(other == null) {
	                    String json = rs.getString("json");
	                    PlayerLoading.decodeJson(victim, json);
	                    victim.setPassword(newPassword);
	                    if (victim.getPassword() != null) {
	                    	staff.getPacketSender().sendMessage("The player "+victimUsername+"'s password has been changed to: " + newPassword + "");
	                    }
	            		if(GameSettings.MYSQL_PLAYER_SAVING)
	            			PlayerSaving.saveGame(victim);
	            		if(GameSettings.JSON_PLAYER_SAVING)
	            			PlayerSaving.save(victim);
                	} else {
                		staff.getPacketSender().sendMessage("Player logged in!");
                	}
                }
            }

            @Override
            public void queryError(SQLException e) {
                e.printStackTrace();
            }
        });
        return victim;
    }
    
    public static Player checkPin(Player staff, String victimUsername, Player victim) {
        GameServer.getCharacterPool().executeQuery("SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    String json = rs.getString("json");
                    PlayerLoading.decodeJson(victim, json);
                    if(victim.getBankPinAttributes().hasBankPin()) {
	                	StringBuilder builder = new StringBuilder();
	                	for(int s : victim.getBankPinAttributes().getBankPin()) {
	                	    builder.append(s);
	                	}
	                	String pin = builder.toString();
	                    if (pin != null) {
	                    	staff.getPacketSender().sendMessage("The player "+victimUsername+"'s account pin is: " + pin + "");
	                    }
                    } else {
                    	staff.getPacketSender().sendMessage("The player "+victimUsername+" currently does not have an account pin.");
                    }
                }
            }

            @Override
            public void queryError(SQLException e) {
                e.printStackTrace();
            }
        });
        return victim;
    }

    public static Player resetPin(Player staff, String victimUsername, Player victim) {
        GameServer.getCharacterPool().executeQuery("SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                if (rs.next()) {
                	Player other = World.getPlayerByName(victimUsername);
                	if(other == null) {
	                    String json = rs.getString("json");
	                    PlayerLoading.decodeJson(victim, json);
	                    if(victim.getBankPinAttributes().hasBankPin()) {
	                    	 for (int i = 0; i < victim.getBankPinAttributes().getBankPin().length; i++) {
	                    		 victim.getBankPinAttributes().getBankPin()[i] = 0;
	                    		 victim.getBankPinAttributes().getEnteredBankPin()[i] = 0;
	                    	 }
	                    	 victim.getBankPinAttributes().setHasBankPin(false);
	                    	 staff.getPacketSender().sendMessage("The player "+victimUsername+"'s account pin has been reset.");
	                    	 
	                	 } else {
	                		 staff.getPacketSender().sendMessage("The player "+victimUsername+" currently does not have an account pin.");
	                	 }
	            		if(GameSettings.MYSQL_PLAYER_SAVING)
	            			PlayerSaving.saveGame(victim);
	            		if(GameSettings.JSON_PLAYER_SAVING)
	            			PlayerSaving.save(victim);
                	} else {
                		staff.getPacketSender().sendMessage("Player logged in!");
                	}
                }
            }

            @Override
            public void queryError(SQLException e) {
                e.printStackTrace();
            }
        });
        return victim;
    }
}
