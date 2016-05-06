package com.runelive.world.content.skill.impl.summoning;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.engine.task.impl.FamiliarSpawnTask;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.GroundItem;
import com.runelive.model.Item;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.Skill;
import com.runelive.model.container.impl.BeastOfBurden;
import com.runelive.model.movement.MovementQueue;
import com.runelive.world.World;
import com.runelive.world.content.skill.impl.summoning.BossPets.BossPet;
import com.runelive.world.entity.impl.GroundItemManager;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.content.Emotes.Skillcape_Data;

/**
 * 
 * @author Jonathan Sirens
 */

public class SummoningScrolls {
	
	public static final Graphic WHITE_GRAPHIC_CAST = new Graphic(1306);
	
	public static final Graphic LIGHT_BROWN_GRAPHIC_CAST = new Graphic(1302);
	
	public static final Graphic BROWN_GRAPHIC_CAST = new Graphic(1311);
	
	public static final Graphic BLUE_GRAPHIC_CAST = new Graphic(1303);
	
	public static final Graphic GREEN_GRAPHIC_CAST = new Graphic(1308);
	
	public static final Graphic ORANGE_GRAPHIC_CAST = new Graphic(1307);
	
	public static final Graphic RED_GRAPHIC_CAST = new Graphic(1316);
	
	public static final Animation DEFAULT_CAST_ANIMATION = new Animation(7660);
	
	public static void bank(Player player, int item) {

	}
}
