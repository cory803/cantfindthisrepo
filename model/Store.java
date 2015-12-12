package com.ikov.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.World;

public class Store {

	public static Connection con = null;
	public static Statement stmt;
	public static boolean connectionMade;

	
	public static void create_connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String IP = "spawnscape.net";
			String DB = "spawnsca_store";
			String User = "spawnsca_store";
			String Pass = "bn(6cS#KHAit"; 
			con = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+DB, User, Pass);
			stmt = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void start_store_process(final Player player) {  //Choose which payment options give which things.
		create_connection();
		int item_id = 0;
		int item_amount = 1;
		String item_name = "";
		String name = player.getUsername();
		if(check_store(player.getUsername())) {		
			if(check_store_item(player.getUsername()) == 92530009) {
				item_name = "Scroll of $10";
				item_id = 10943;
				player.getInventory().add(item_id, item_amount);
				player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"+item_name+"</shad></col>, we have added it to your inventory.");
				announce(player, item_name, name);			
				store_item_given(player.getUsername());
			}
			if(check_store_item(player.getUsername()) == 92530010) {
				item_name = "Scroll of $25";
				item_id = 10934;
				player.getInventory().add(item_id, item_amount);
				player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"+item_name+"</shad></col>, we have added it to your inventory.");
				announce(player, item_name, name);			
				store_item_given(player.getUsername());
			}
			if(check_store_item(player.getUsername()) == 92530011) {
				item_name = "Scroll of $50";
				item_id = 10935;
				player.getInventory().add(item_id, item_amount);
				player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"+item_name+"</shad></col>, we have added it to your inventory.");
				announce(player, item_name, name);			
				store_item_given(player.getUsername());
			}
			if(check_store_item(player.getUsername()) == 92530012) {
				item_name = "Scroll of $100";
				item_id = 7629;
				player.getInventory().add(item_id, item_amount);
				player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"+item_name+"</shad></col>, we have added it to your inventory.");
				announce(player, item_name, name);			
				store_item_given(player.getUsername());
			}
		} else {
			player.getPacketSender().sendMessage("<col=ff0000>Your store purchase has not been found.");			
		}
	}
 	
	public static void announce(Player p, String item_name, String name) {
		World.sendMessage("<col=2EA73D><shad=0>[<img=1><col=ffff00><shad=0>"+name+"</col></shad><img=1><col=2EA73D><shad=0>] <col=00627E>has just purchased <col=ff0000>"+item_name+"</col> <col=00627E>from <col=2EA73D>www.spawnscape.net/store<col=00627E>!");	
	}
	
	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stmt.executeQuery(s);
				return rs;
			} else {
				stmt.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			destroy_connection();
		}
		return null;
	}

	public static void destroy_connection() {
		try {
			stmt.close();
			con.close();
		} catch (Exception e) {
		}
	}

	public static boolean check_store(String name) {
        try {
            Statement statement = con.createStatement();
            String query = "SELECT * FROM purchased WHERE username = '" + name + "'";
            ResultSet results = statement.executeQuery(query);
			while(results.next()) {
            int product = results.getInt("item_id");
                if(product > 1) {                     
					return true;
                }       
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
		return false;
	}
	
	public static int check_store_item(String name) {  
        try {
			Statement statement = con.createStatement();
            String query = "SELECT * FROM purchased WHERE username = '" + name + "'";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                int productid = results.getInt("item_id");
                if(productid >= 1) {				                          
					return productid;
                }
			}
        } catch(SQLException e) {
            e.printStackTrace();
        }
		return 0;
	}		
	
	public static boolean store_item_given(String name) {       
		try {
			query("DELETE FROM `purchased` WHERE username = '"+name+"';");
        } catch (Exception e) {
            e.printStackTrace();
			return false;
        }
        return true;
    }	
	
}	
	
