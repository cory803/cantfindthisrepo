package com.ikov.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ikov.world.World;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.entity.impl.player.Player;

public class Store {

  public static Connection con = null;
  public static Statement stmt;
  public static boolean connectionMade;


  public static void create_connection() {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      String IP = "192.99.148.171";
      String DB = "ikov2_store";
      String User = "ikov2_root";
      String Pass = "c-W-U,dL=UHQ";
      con = DriverManager.getConnection("jdbc:mysql://" + IP + "/" + DB, User, Pass);
      stmt = con.createStatement();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void start_store_process(final Player player) {
    create_connection();
    int item_id = 0;
    int item_amount = 1;
    String item_name = "";
    String name = player.getUsername();
    if (check_store(player.getUsername())) {
      if (check_store_item(player.getUsername()) == 92530009) {
        item_name = "$10 Payment Scroll";
        item_id = 10943;
        item_amount = check_store_quantity(player.getUsername());
        player.getInventory().add(item_id, item_amount);
        player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"
            + item_name + "</shad></col>, we have added it to your inventory.");
        store_item_given(player.getUsername());
        PlayerLogs.log(player.getUsername(),
            "" + player.getUsername() + " has purchased store item and claimed " + item_name + ".");
        World.sendMessage("<img=4><col=2F5AB7>" + player.getUsername()
            + " has just claimed a <col=9A0032>$10<col=2f5ab7> scroll using ::donate!");
      }
      if (check_store_item(player.getUsername()) == 92530010) {
        item_name = "$25 Payment Scroll";
        item_id = 10934;
        player.getInventory().add(item_id, item_amount);
        player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"
            + item_name + "</shad></col>, we have added it to your inventory.");
        store_item_given(player.getUsername());
        PlayerLogs.log(player.getUsername(),
            "" + player.getUsername() + " has purchased store item and claimed " + item_name + ".");
        World.sendMessage("<img=4><col=2F5AB7>" + player.getUsername()
            + " has just claimed a <col=9A0032>$25<col=2f5ab7> scroll using ::donate!");
      }
      if (check_store_item(player.getUsername()) == 92530011) {
        item_name = "$50 Payment Scroll";
        item_id = 10935;
        player.getInventory().add(item_id, item_amount);
        player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"
            + item_name + "</shad></col>, we have added it to your inventory.");
        store_item_given(player.getUsername());
        PlayerLogs.log(player.getUsername(),
            "" + player.getUsername() + " has purchased store item and claimed " + item_name + ".");
        World.sendMessage("<img=4><col=2F5AB7>" + player.getUsername()
            + " has just claimed a <col=9A0032>$50<col=2f5ab7> scroll using ::donate!");
      }
      if (check_store_item(player.getUsername()) == 92530012) {
        item_name = "$125 Payment Scroll";
        item_id = 7629;
        player.getInventory().add(item_id, item_amount);
        player.getPacketSender().sendMessage("Thank you for purchasing <col=ff0000><shad=0>"
            + item_name + "</shad></col>, we have added it to your inventory.");
        store_item_given(player.getUsername());
        PlayerLogs.log(player.getUsername(),
            "" + player.getUsername() + " has purchased store item and claimed " + item_name + ".");
        World.sendMessage("<img=4><col=2F5AB7>" + player.getUsername()
            + " has just claimed a <col=9A0032>$100<col=2f5ab7> scroll using ::donate!");
      }
    } else {
      DialogueManager.sendStatement(player,
          "Your donation has not been found, it may take up to 24 hours!");
    }
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
      while (results.next()) {
        int product = results.getInt("item_id");
        if (product > 1) {
          return true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static int check_store_item(String name) {
    try {
      Statement statement = con.createStatement();
      String query = "SELECT * FROM purchased WHERE username = '" + name + "'";
      ResultSet results = statement.executeQuery(query);
      while (results.next()) {
        int productid = results.getInt("item_id");
        if (productid >= 1) {
          return productid;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static int check_store_quantity(String name) {
    try {
      Statement statement = con.createStatement();
      String query = "SELECT * FROM purchased WHERE username = '" + name + "'";
      ResultSet results = statement.executeQuery(query);
      while (results.next()) {
        int quantity = results.getInt("quantity");
        if (quantity >= 1) {
          return quantity;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static boolean store_item_given(String name) {
    try {
      query("DELETE FROM `purchased` WHERE username = '" + name + "';");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}

