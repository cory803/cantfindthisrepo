package com.ikov.model;

import java.util.Arrays;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.ikov.util.Misc;


/**
 * Represents a player's privilege rights.
 * @author Gabriel Hannason
 */

public enum PlayerRights {

	PLAYER(-1, null, 1, 1),
	MODERATOR(-1, "<col=20B2AA><shad=0>", 1, 1),
	ADMINISTRATOR(-1, "<col=FFFF64><shad=0>", 1, 1, MODERATOR),
	OWNER(-1, "<col=B40404>", 1, 1, ADMINISTRATOR, MODERATOR),
	SUPPORT(-1, "<col=FF0000><shad=0>", 1, 1),
	YOUTUBER(-1, "<col=EE0101><shad=891E19>", 1, 1),
	GLOBAL_MOD(-1, "<col=EE0101><shad=891E19>", 1, 1, MODERATOR),
	COMMUNITY_MANAGER(-1, "<col=EE0101><shad=891E19>", 1, 1),
	WIKI_EDITOR(-1, "<col=EE0101><shad=891E19>", 1, 1),
	WIKI_MANAGER(-1, "<col=EE0101><shad=891E19>", 1, 1),
	STAFF_MANAGER(-1, "<col=EE0101><shad=891E19>", 1, 1);

	PlayerRights(int yellDelaySeconds, String yellHexColorPrefix, double loyaltyPointsGainModifier, double experienceGainModifier, PlayerRights...inherited) {
		this.yellDelay = yellDelaySeconds;
		this.yellHexColorPrefix = yellHexColorPrefix;
		this.loyaltyPointsGainModifier = loyaltyPointsGainModifier;
		this.experienceGainModifier = experienceGainModifier;
		this.inherited = inherited;
	}
	
	public static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(SUPPORT, MODERATOR, ADMINISTRATOR, OWNER, GLOBAL_MOD, COMMUNITY_MANAGER, STAFF_MANAGER);
	
	private final PlayerRights[] inherited;
	
	/*
	 * The yell delay for the rank
	 * The amount of seconds the player with the specified rank must wait before sending another yell message.
	 */
	private int yellDelay;
	private String yellHexColorPrefix;
	private double loyaltyPointsGainModifier;
	private double experienceGainModifier;
	
	public int getYellDelay() {
		return yellDelay;
	}
	
	/*
	 * The player's yell message prefix.
	 * Color and shadowing.
	 */
	
	public String getYellPrefix() {
		return yellHexColorPrefix;
	}
	
	/**
	 * The amount of loyalty points the rank gain per 4 seconds
	 */
	public double getLoyaltyPointsGainModifier() {
		return loyaltyPointsGainModifier;
	}
	
	public double getExperienceGainModifier() {
		return experienceGainModifier;
	}
	
	public boolean inherits(PlayerRights rights) {
		return equals(rights) || Arrays.asList(inherited).contains(rights) || rights.equals(PLAYER) || equals(MODERATOR) && rights.equals(SUPPORT);
	}

	public boolean isStaff() {
		return STAFF.contains(this);
	}
	
	public String getFormatedName() {
		return Misc.toTitleCase(toString());
	}

	/**
	 * Gets the rank for a certain id.
	 * 
	 * @param id	The id (ordinal()) of the rank.
	 * @return		rights.
	 */
	public static PlayerRights forId(int id) {
		for (PlayerRights rights : PlayerRights.values()) {
			if (rights.ordinal() == id) {
				return rights;
			}
		}
		return null;
	}
}
