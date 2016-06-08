package com.runelive.world.content;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.model.Item;
import com.runelive.net.mysql.ThreadedSQLCallback;
import com.runelive.world.entity.impl.player.Player;

public class PlayerLogs {

  /**
   * Fetches system time and formats it appropriately
   * 
   * @return Formatted time
   */
  private static String getTime() {
    Date getDate = new Date();
    String timeFormat = "M/d/yy hh:mma";
    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
    return "[" + sdf.format(getDate) + "]";
  }
  
  /**
   * Logs commands into MYSQL Database
   */
  public static void commands(Player player, String command) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `commands`(`username`, `rights`, `time`, `ip address`, `serial address`, `command`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+player.getRights().ordinal()+"','"+getTime()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+command.replaceAll("[\"\\\'/]", "")+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs trades into MYSQL Database
   */
  public static void trades(Player owner, Player collector, Item traded) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `trades`(`owner`, `collector`, `item name`, `item id`, `item amount`, `time`) VALUES ('"+owner.getUsername().replaceAll("[\"\\\'/]", "")+"','"+collector.getUsername().replaceAll("[\"\\\'/]", "")+"','"+traded.getDefinition().getName().replaceAll("[\"\\\'/]", "")+"','"+traded.getId()+"','"+traded.getAmount()+"','"+getTime()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs drops into MYSQL Database
   */
  public static void drops(Player player, Item drop, String address) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `drops`(`username`, `rights`, `time`, `ip address`, `serial address`, `item address`, `item id`, `item amount`, `item name`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+player.getRights().ordinal()+"','"+getTime()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+address+"','"+drop.getId()+"','"+drop.getAmount()+"','"+drop.getDefinition().getName().replaceAll("[\"\\\'/]", "")+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs player kills into MYSQL Database
   */
  public static void kills(Player killer, Player looser, Item loot) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `kills`(`killer`, `looser`, `time`, `item name`, `item id`, `item amount`) VALUES ('"+killer.getUsername().replaceAll("[\"\\\'/]", "")+"','"+looser.getUsername()+"','"+getTime()+"','"+loot.getDefinition().getName().replaceAll("[\"\\\'/]", "")+"','"+loot.getId()+"','"+loot.getAmount()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs player connections into MYSQL Database
   */
  public static void connections(Player player, String type) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `connections`(`username`, `type`, `time`, `ip address`, `serial address`, `mac address`, `rights`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+type+"','"+getTime()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+player.getMacAddress().replaceAll("[\"\\\'/]", "")+"','"+player.getRights().ordinal()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  } 
  
  /**
   * Logs player donations into MYSQL Database
   */
  public static void donations(Player player, int forumId, int tokens, Item item) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `donations`(`username`, `forumid`, `tokens`, `item name`, `item id`, `item amount`, `ip address`, `serial address`, `time`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+forumId+"','"+tokens+"','"+item.getDefinition().getName().replaceAll("[\"\\\'/]", "")+"','"+item.getId()+"','"+item.getAmount()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+getTime()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  } 
  
  /**
   * Logs player scrolls into MYSQL Database
   */
  public static void scrolls(Player player, int tokens, String forumName) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `scrolls`(`username`, `tokens`, `forum name`, `ip address`, `serial address`, `time`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+tokens+"','"+forumName.replaceAll("[\"\\\'/]", "")+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+getTime()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }   
  /**
   * Logs pickups into MYSQL Database
   */
  public static void pickups(Player player, Item drop, int address) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `pickups`(`username`, `rights`, `time`, `ip address`, `serial address`, `item address`, `item id`, `item amount`, `item name`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+player.getRights().ordinal()+"','"+getTime()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+address+"','"+drop.getId()+"','"+drop.getAmount()+"','"+drop.getDefinition().getName().replaceAll("[\"\\\'/]", "")+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs pins into MYSQL Database
   */
  public static void pins(Player player, String action) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
	StringBuilder builder = new StringBuilder();
	for(int s : player.getBankPinAttributes().getBankPin()) {
	    builder.append(s);
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `pins`(`username`, `rights`, `time`, `ip address`, `serial address`, `action`, `pin`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+player.getRights().ordinal()+"','"+getTime()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+action.replaceAll("[\"\\\'/]", "")+"','"+builder.toString()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  

  /**
   * Logs other data into MYSQL Database
   */
  public static void other(Player player, String action) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `other`(`username`, `rights`, `time`, `ip address`, `serial address`, `action`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+player.getRights().ordinal()+"','"+getTime()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+action.replaceAll("[\"\\\'/]", "")+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs staking data into MYSQL Database
   */
  public static void stake(Player winner, Player looser, Item winning, String owner) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `staking`(`winner`, `looser`, `owner`, `item name`, `item id`, `item amount`, `time`) VALUES ('"+winner.getUsername().replaceAll("[\"\\\'/]", "")+"','"+looser.getUsername().replaceAll("[\"\\\'/]", "")+"','"+owner.replaceAll("[\"\\\'/]", "")+"','"+winning.getDefinition().getName().replaceAll("[\"\\\'/]", "")+"','"+winning.getId()+"','"+winning.getAmount()+"','"+getTime()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs dicing data into MYSQL Database
   */
  public static void dicing(Player player, int roll) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `dicing`(`username`, `time`, `roll`, `ip address`, `serial address`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]","")+"','"+getTime()+"','"+roll+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs flower planting data into MYSQL Database
   */
  public static void plant(Player player, String plant) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `flowers`(`username`, `time`, `plant`, `ip address`, `serial address`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+getTime()+"','"+plant+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  /**
   * Logs flower planting data into MYSQL Database
   */
  public static void npcdrops(Player player, Item drop, String npcName) {
	if(!GameSettings.PLAYER_LOGGING) {
		return;
	}
    GameServer.getLoader().getEngine().submit(() -> {
      try {
		GameServer.getCharacterPool().executeQuery("INSERT INTO `npcdrops`(`username`, `time`, `ip address`, `serial address`, `item name`, `item id`, `item amount`, `npc name`) VALUES ('"+player.getUsername().replaceAll("[\"\\\'/]", "")+"','"+getTime()+"','"+player.getHostAddress()+"','"+player.getSerialNumber()+"','"+drop.getDefinition().getName().replaceAll("[\"\\\'/]", "")+"','"+drop.getId()+"','"+drop.getAmount()+"','"+npcName.replaceAll("[\"\\\'/]", "")+"')", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
