package com.chaos.world.content.skill.impl.farming.seed;

import com.chaos.model.Animation;
import com.chaos.model.Item;
import com.chaos.world.content.skill.impl.farming.patch.Patch;

/**
 * 
 * @author relex lawl
 */
public interface SeedType {
	
	public SeedClass getSeedClass();
		
	public int getLevelRequirement();
	
	public int getToolId();
	
	public Animation getAnimation();
	
	public Item getSeed();
	
	public int[] getGrowthTime();
		
	public Item[] getRewards();
	
	public Item[] getProtectionFee();
	
	public int[] getExperience();

	public int[] getValues();
	
	public int getHarvestAmount(Patch patch);
}
